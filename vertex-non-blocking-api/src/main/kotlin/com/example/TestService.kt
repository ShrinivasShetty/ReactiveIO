package com.example

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.Executors

class TestService(private val client: HttpClient, private val mongoRepo: ReactiveMongoRepo) {
    private val pool = Executors.newScheduledThreadPool(1).asCoroutineDispatcher()
    suspend fun getData() = client.request("http://localhost:8080") {
              // Configure request parameters exposed by HttpRequestBuilder
         }.toString()


    suspend fun start(){
        withContext(pool) {
            simple().collect()
        }
        println("saved...")
    }

    private suspend fun simple(): Flow<MockEvent> = flow {
        for (i in 1..10000) {
            emit(MockEvent(i, null))
        }
    }.buffer(100).map {
        //read
        //Update//

        //More than one actor//

        //splitting the issue//

        //distinct communication_id//



        var data = GlobalScope.async {

            //Fetch the data//
            //cont 1

            //Upadte the commication Obeject//
            mongoRepo.save(Person(it.incrementor.toString(), "cc", it.incrementor))
        }
        it.differed = data
      //  mongoRepo.save(Person(it.incrementor.toString(), "cc", it.incrementor))
        it
    }.buffer(1000).map {
        it.differed!!.await()
        it
    }.buffer(1000).map {
        println("Completed....")
        it
    }.buffer(1000).map {
        //Step 5
        it
    }


}

data class MockEvent(val incrementor: Int, var differed: Deferred<Unit>?= null)