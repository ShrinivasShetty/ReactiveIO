package com.example.plugins

import com.example.TestService
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
//        get("/getData") {
//            testService.start()
//            call.respondText(testService.getData())
//        }
    }



}
