@file:Suppress("DuplicatedCode", "ClassName")

package io.grpc.demo

import com.company.slack.Empty
import com.company.slack.Message
import com.company.slack.SlackGrpc
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import java.time.Instant
import java.util.logging.Logger.getLogger

private val logger = getLogger("step1")

fun main() {

    val server = ServerBuilder.forPort(50051)
        .addService(SlackService_SendMessage())
        .build()
        .start()

    logger.info { "Server started, listening on ${server.port} 🚀" }

    server.awaitShutdown()

}

class SlackService_SendMessage : SlackGrpc.SlackImplBase() {

    override fun sendMessage(value: Message, responseObserver: StreamObserver<Empty>) {

        logger.info { "Client sent message ${value.text} at ${Instant.ofEpochMilli(value.timestamp)}" }

        responseObserver.onNext(Empty.newBuilder().build())
        responseObserver.onCompleted()

    }

}
