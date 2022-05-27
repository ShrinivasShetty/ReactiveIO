package com.example.highthroughput


import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.Duration
import java.util.concurrent.Executors

/**
 * efficient replace of disruptor pattern
 * step1 to step4 execute independently
 * but maintain the sequence (FIFO)
 * Good option in kafkaConsumer. Process the message in efficient way with maintained order
 *
 */
suspend fun main(){
   withContext(Executors.newSingleThreadExecutor().asCoroutineDispatcher()) {
       nonBlockingHighThroughputPipeline().collect()
   }

}


/**
 * This is running in single thread
 * Since this pipeline is non blockig one thread is more efficient
 */
suspend fun nonBlockingHighThroughputPipeline(): Flow<Int> = flow {
    for (i in 1..10) {
        emit(i)
    }
}.buffer(1000).map {
    //Step1
    println("first Flow starte$it+${Thread.currentThread().name}")
    it + 2
}.buffer(1000).map {
    //Step2
    println("second Flow starte$it+${Thread.currentThread().name}")
    kotlinx.coroutines.time.delay(Duration.ofSeconds(10))
    it
}.buffer(1000).map {
    //Step3
    println("third Flow starte$it+${Thread.currentThread().name}")
    it
}.buffer(1000).map {
    //Step4
    println("fourth Flow starte$it+${Thread.currentThread().name}")
    it
}

