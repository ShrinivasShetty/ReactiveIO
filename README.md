# ReactiveIO


# Non Blocking Api:
Desigined api purely on callBack approche. 
Blocking api will impact the scalability of the application. 
This design approch will address this problem
 
https://github.com/ShrinivasShetty/ReactiveIO/tree/main/vertex-non-blocking-api/diagram

# High Performance Inter-Thread Messaging Library:
https://github.com/ShrinivasShetty/ReactiveIO/tree/main/vertex-non-blocking-api/src/main/kotlin/com/example/highthroughput

**More Info**: https://lmax-exchange.github.io/disruptor/disruptor.html


Integration of Reactive Db call and Kotling flow.
Call Mongo as in suspended scope. (Make mongo repo as Publisher and this app as subscriber)
Use kotlin flow as High through output file reactive stream.
