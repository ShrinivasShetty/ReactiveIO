package com.example.verteximpl

import com.example.handlers.Myhandlers
import io.vertx.core.http.HttpClientOptions
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.RequestOptions
import io.vertx.core.json.Json
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.TimeoutHandler
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import io.vertx.kotlin.coroutines.dispatcher
import io.vertx.rxjava.core.http.HttpClient
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.reactive.awaitFirst
import reactor.core.publisher.Mono
import reactor.core.publisher.MonoSink
import java.util.concurrent.Executors


class EventLoopVerticles : CoroutineVerticle(){
    private val channel = Channel<RoutingContext>(400000)
    var reactiveClient: HttpClient?= null
    private var consumerDispatcher: ExecutorCoroutineDispatcher = Executors.newFixedThreadPool(1) { runnable ->
        Thread(runnable).apply {
            name = "channel-consumer-thread-pool"
        }
    }.asCoroutineDispatcher()

    private var channelDispatcher: ExecutorCoroutineDispatcher = Executors.newFixedThreadPool(1) { runnable ->
        Thread(runnable).apply {
            name = "channel-consumer-thread-pool"
        }
    }.asCoroutineDispatcher()


    override suspend fun start() {
        val myhandlers = Myhandlers()
        val router = routes(myhandlers)
        val reactivevertex = io.vertx.rxjava.core.Vertx.vertx()
        var hoption = HttpClientOptions()
        hoption.maxPoolSize = 500
        reactiveClient = reactivevertex.createHttpClient(hoption)
        vertx.createHttpServer()
            .requestHandler(router) // Start listening
            .listen(8081) // Print the port
            .onComplete { println("HttpSever started at ${it.result().actualPort()}") }
            .await()


        /**
         * launching consumer for channel
         */
        withContext(consumerDispatcher) {
            for (consumerId in 1..2) {
                launch {
                    channelConsumer(myhandlers)
                }
            }
        }
    }

    private suspend fun channelConsumer(handlers: Myhandlers) {
        for (data in channel) {
            withContext(vertx.dispatcher()) {
                //Add logic for batch update//
                data.response().end(handlers.nonBlockingFunction())
            }
        }
    }

    private suspend fun reactiveCall2() {
        for (i in 1..1500) {
            CoroutineScope(consumerDispatcher).launch {
                val reuest = RequestOptions()
                    .setHost("localhost")
                    .setPort(8081)
                    .setURI("/check")
                    .setMethod(HttpMethod.GET)
                val nn = Mono.create { s: MonoSink<String> ->
                    val ff = reactiveClient?.rxRequest(reuest)
                        ?.flatMap { it.rxSend() }
                        ?.subscribe({ response ->
                            response.body {
                                val ff = String(it.result().bytes)
                                s.success(ff)
                            }
                        }) { error ->
                            System.out.println("Something went wrong ")
                        }
                }.awaitFirst()
            }
        }
    }

    override suspend fun stop() {
        super.stop()
    }

    private fun routes(handlers: Myhandlers): Router {
        val router = Router.router(vertx)
        /**
         * Batch update from Api
         * all the http request send to kotlin channel.
         * channelConsumer() is the consumer for channel
         * It works as publisher subscriber model
         * Can launch number of consumer
         *
         * In The consumer we can add batches to save the records in DB
         *
         * This will avoid to many IO calls for each http request, Result Scalability will increase.
         *
         * Ex: Insert Person() to DB
         *
         * instead of hitting db for each http request.
         * Can batch this and save in batch (unnest() in poostgress)
         *
         */
        router.get("/chanel-queue-api")
            .produces("application/json")
            .handler {
                CoroutineScope(channelDispatcher).launch {
                        channel.send(it)
                }
            }


        router.get("/verify")
            .produces("application/json")
            .handler(TimeoutHandler.create(1000))
            .coroutineHandler {
                GlobalScope.launch {
                    it.response().end(Json.encode( reactiveCall2()))
                }
            }

        /**
         * Non Blocking api
         * nonBlockingFunction() has delay of 10s, But the caller api thread will not get blocked, Suspend from the job and take other http request from pool
         *
         * Advantages:
         *   One thread with multiple job
         *   scalability of application good. Since no blocking of http threads, will handel more request
         *   Avoid horizontal scaling
         *
         *   Non-blocking function:  wait-->delay(Duration.ofSeconds(10))
         *   Blocking function:  wait ---> Thread.sleep(10000)
         *
         *   In non-blocking api always non-blocking function is recommended.
         *
         */
        router.get("/nonblocking-update-data")
            .produces("application/json")
            //.handler(TimeoutHandler.create(10000))
            .coroutineHandler {
                println("The current thread is....${Thread.currentThread().name}")
                it.response().end(handlers.nonBlockingFunction())
            }

        /**
         * Same as /nonblocking-update-data
         * Instead of delay(Duration.ofSeconds(10)) used Mongo non-blocking save function
         */
        router.get("/nonblocking-mongo-save")
            .produces("application/json")
            .coroutineHandler {
                it.response().end(handlers.nonBlockingMongoSave())
            }
        return router
    }

    fun Route.coroutineHandler(fn: suspend (RoutingContext) -> Unit) = handler {
        CoroutineScope(it.vertx().dispatcher()).launch{
            try {
                fn(it)
            } catch (e: Exception) {
                it.fail(e)
            }
        }
    }

}



