# Non Blocking Api:
Desigined api purely on callBack approche. 
Blocking api will impact the scalability of the application. 
This design approch will address this problem

![](vertex-non-blocking-api/diagram/non-blocking.png)

**More Info**: https://vertx.io/docs/vertx-web/java/


# High Performance Inter-Thread Messaging Library:
https://github.com/ShrinivasShetty/ReactiveIO/tree/main/vertex-non-blocking-api/src/main/kotlin/com/example/highthroughput

**More Info**: https://lmax-exchange.github.io/disruptor/disruptor.html

# ReactiveIO:

**ProjectReactor**: https://projectreactor.io/

**Kotlin flow**: https://kotlinlang.org/docs/flow.html

Making mongo IO call without blocking the caller thread. 
https://github.com/ShrinivasShetty/ReactiveIO/blob/main/vertex-non-blocking-api/src/main/kotlin/com/example/ReactiveMongoRepo.kt

# Api batch update:
**Network transaction time**:
 IO call ecexution time includes network call. i,e time to reach request from web system to IO system.
 Api TPS will also consider Network transaction time. 
 
 
 #### Insted of calling IO for each api request, Any way to make this done in batch. Without impacting the end user.
     ####  overall Network transaction time will get reduced.
     ####  good impact to scalability can be achived
     
    
![](vertex-non-blocking-api/diagram/batchUpdate.png)
