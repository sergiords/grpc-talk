@file:Suppress("DuplicatedCode")

package io.grpc.demo

import com.company.slack.Message
import com.company.slack.SlackGrpc
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import java.time.Instant
import java.util.logging.Level
import java.util.logging.Logger

private val logger = Logger.getLogger("step4")

fun main() {

    val managedChannel = ManagedChannelBuilder
        .forAddress("localhost", 50051)
        .usePlaintext()
        .build()

    val stub = SlackGrpc.newStub(managedChannel)

    val sendObserver = stub.receiveAndSendMessages(object : StreamObserver<Message> {

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

    sendObserver.onNext(buildMessage(1))
    Thread.sleep(1_000)
    sendObserver.onNext(buildMessage(2))
    sendObserver.onCompleted()

    managedChannel.awaitShutdown()

}

private fun buildMessage(id: Int) = Message.newBuilder()
    .setText("Message from client #$id")
    .setTimestamp(System.currentTimeMillis())
    .build()
