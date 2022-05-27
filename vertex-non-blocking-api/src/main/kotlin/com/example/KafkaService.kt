package com.example

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.config.SslConfigs
import org.apache.kafka.common.quota.ClientQuotaEntity.CLIENT_ID
import java.math.BigInteger
import java.time.Duration
import java.util.*

class KafkaService {
}

fun main() {
    val props = Properties()
    props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "kafka-ttc-app.prod.target.com:9093"
    props[ConsumerConfig.GROUP_ID_CONFIG] = "apex_test"
    props[ConsumerConfig.CLIENT_ID_CONFIG] = "apex_test"
    props[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = "false"
    props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
    props[ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG] = "1000"
    props[ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG] = "30000"
    props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = "org.apache.kafka.common.serialization.StringDeserializer"
    props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = "org.apache.kafka.common.serialization.StringDeserializer"
    if (true) {
        props[CommonClientConfigs.SECURITY_PROTOCOL_CONFIG] = "SSL"
        props[SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG] = "/Users/z0045tk/Documents/Coro_new/secrest/target.truststore.jks"
        props[SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG] = "changeit"
        props[SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG] ="/Users/z0045tk/Documents/Coro_new/secrest/server.jks"
        props[SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG]= "changeit"
        props[SslConfigs.SSL_KEY_PASSWORD_CONFIG] ="changeit"
        props[SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG] =""
    }
    val consumer =  KafkaConsumer<String, String>(props)
    consumer.subscribe(listOf("apex-merch-wagon-wheel"))
    val pollDuration = Duration.ofMillis(1000L)
    while (true) {
        try {
            val record = consumer.poll(pollDuration)
            if (record.isEmpty) {
                println("emty data/...")
            } else
                println(record)
        }
        catch (e: Exception){
            println("error.......")
        }
    }



}