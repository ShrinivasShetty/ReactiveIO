package com.example.verteximpl

import io.vertx.core.eventbus.Message
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.kotlin.coroutines.receiveChannelHandler
import io.vertx.kotlin.coroutines.toReceiveChannel
import kotlinx.coroutines.time.delay
import java.time.Duration

class EventBusVerticle : CoroutineVerticle() {
    override suspend fun start() {
//        delay(Duration.ofSeconds(1))
//        awaitResult<String> {
//
//            //delay(Duration.ofSeconds(1))
//            vertx.eventBus().consumer<String>("test.api"){
//                it.reply("Done")
//
//            }
//        }

//        vertx.receiveChannelHandler<String>().apply {
//            this.
//        }
        val adapter = vertx.receiveChannelHandler<Message<Int>>()


        vertx.eventBus().localConsumer<Int>("test.api").handler(adapter)

        for (record in adapter) {
            delay(Duration.ofSeconds(1))
            println("I am done...")
            record.reply("hai")
        }
    }
}