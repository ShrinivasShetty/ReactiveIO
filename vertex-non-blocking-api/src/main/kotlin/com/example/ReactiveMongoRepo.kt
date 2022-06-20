package com.example

import com.mongodb.BasicDBObject
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import com.mongodb.reactivestreams.client.MongoCollection
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import org.bson.Document


class ReactiveMongoRepo {
    private val credential = MongoCredential.createScramSha1Credential(
        "coms",
        "coms",
        "wTor1mzDFxfd^0u".toCharArray())
    private val MongoClient: MongoClient = MongoClients.create(
        MongoClientSettings.builder()
            .credential(credential)
            .applyToClusterSettings{
                builder -> builder.hosts(listOf(ServerAddress("localhost", 27017)))}
            .build()
    )

    private val collection: MongoCollection<Document> = MongoClient.getDatabase("coms").getCollection("Test")
    //var mongoOps = ReactiveMongoTemplate(MongoClients.create(), "database")

    suspend fun save(person: Person) {
        val doc = Document()
        doc.append("id", person.id)
        doc.append("_id", person.id)
        doc.append("name", person.name)
        doc.append("age", person.age)
        collection.insertOne(doc).awaitFirst()
    }

    suspend fun update(person: Person) {
        val filter = Document()
        val doc2= Document()
        val doc3= Document()
        val increases = Document()
        increases.append("$"+"inc", doc2.append("age", 1))
        increases.append("$"+"set", doc3.append("name", "dd"))
        collection.updateOne(filter.append("_id", person.id), increases).awaitFirst()
    }
}

suspend fun main() {
    val repo = ReactiveMongoRepo()
    repo.update(Person("111", "test", 1))
    println("saved...")
}
