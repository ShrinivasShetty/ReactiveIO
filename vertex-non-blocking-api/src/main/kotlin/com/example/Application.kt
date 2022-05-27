package com.example

import com.example.verteximpl.EventLoopVerticles
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import io.vertx.core.eventbus.EventBusOptions
import java.util.concurrent.TimeUnit


fun main() {
    val mongoRepo = ReactiveMongoRepo()
    val option = DeploymentOptions()
    option.instances = 1
    //SingleThreadEventLoop
    val bus = EventBusOptions()
    bus.sendBufferSize = 20000
    bus.receiveBufferSize = 20000
    val voptions =  VertxOptions()
    voptions.maxEventLoopExecuteTime = TimeUnit.SECONDS.toNanos(1)
    voptions.eventBusOptions = bus
    val vertx = Vertx.vertx(voptions)
    vertx.deployVerticle(EventLoopVerticles())

}
