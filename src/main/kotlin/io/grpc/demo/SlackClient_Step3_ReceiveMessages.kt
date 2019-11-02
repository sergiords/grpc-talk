@file:Suppress("DuplicatedCode")

package io.grpc.demo

import com.company.slack.Empty
import com.company.slack.Message
import com.company.slack.SlackGrpc
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import java.time.Instant
import java.util.logging.Level
import java.util.logging.Logger

private val logger = Logger.getLogger("step3")

fun main() {

    val managedChannel = ManagedChannelBuilder
        .forAddress("localhost", 50051)
        .usePlaintext()
        .build()

    val stub = SlackGrpc.newStub(managedChannel)

    val empty = Empty.getDefaultInstance()

    stub.receiveMessages(empty, object : StreamObserver<Message> {

        override fun onNext(value: Message) {
            logger.info { "Server sent message ${value.text} at ${Instant.ofEpochMilli(value.timestamp)}" }
        }

        override fun onError(t: Throwable) {
            logger.log(Level.SEVERE, t) { "Server sent an error" }
        }

        override fun onCompleted() {
            logger.info { "Server sent end of stream" }
        }

    })

    managedChannel.awaitShutdown()

}
