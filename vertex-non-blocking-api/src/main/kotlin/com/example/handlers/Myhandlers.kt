package com.example.handlers

import com.example.Person
import com.example.ReactiveMongoRepo
import kotlinx.coroutines.time.delay
import java.time.Duration

class Myhandlers {
    private val mongoRepo = ReactiveMongoRepo()

    suspend fun nonBlockingFunction(): String{
        delay(Duration.ofSeconds(10))
        return "${Thread.currentThread().name}"
    }

    suspend fun nonBlockingMongoSave(): String{
        mongoRepo.save(Person("111", "sss", 10))
        return "SAVED"
    }

}