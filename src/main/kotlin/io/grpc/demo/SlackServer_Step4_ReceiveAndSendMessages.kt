@file:Suppress("DuplicatedCode", "ClassName")

package io.grpc.demo

import com.company.slack.Message
import com.company.slack.SlackGrpc
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import java.time.Instant
import java.util.logging.Level
import java.util.logging.Logger.getLogger

private val logger = getLogger("step4")

fun main() {

    val server = ServerBuilder.forPort(50051)
        .addService(SlackService_ReceiveAndSendMessages())
        .build()
        .start()

    logger.info { "Server started, listening on ${server.port} 🚀" }

    server.awaitShutdown()

}


class SlackService_ReceiveAndSendMessages : SlackGrpc.SlackImplBase() {

    override fun receiveAndSendMessages(responseObserver: StreamObserver<Message>) = object : StreamObserver<Message> {

        override fun onNext(value: Message) {

            logger.info { "Client sent message ${value.text} at ${Instant.ofEpochMilli(value.timestamp)}" }

            responseObserver.onNext(
                Message.newBuilder()
                    .setText("Response for ${value.text}")
                    .setTimestamp(System.currentTimeMillis())
                    .build()
            )

        }

        override fun onError(t: Throwable) {
            logger.log(Level.SEVERE, t) { "Client sent an error" }
        }

        override fun onCompleted() {
            logger.info { "Client sent an end of stream" }
            responseObserver.onCompleted()
        }

    }

}
