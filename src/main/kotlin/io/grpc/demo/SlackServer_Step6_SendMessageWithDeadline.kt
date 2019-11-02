@file:Suppress("DuplicatedCode", "ClassName")

package io.grpc.demo

import com.company.slack.Empty
import com.company.slack.Message
import com.company.slack.SlackGrpc
import io.grpc.Context
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import java.util.concurrent.TimeUnit
import java.util.logging.Logger.getLogger

private val logger = getLogger("step6")

fun main() {

    val server = ServerBuilder.forPort(50051)
        .addService(SlackServer_SendMessageWithDeadline())
        .build()
        .start()

    logger.info { "Server started, listening on ${server.port} 🚀" }

    server.awaitShutdown()

}

class SlackServer_SendMessageWithDeadline : SlackGrpc.SlackImplBase() {

    override fun sendMessage(value: Message, responseObserver: StreamObserver<Empty>) {

        val timeRemaining = Context.current().deadline.timeRemaining(TimeUnit.MILLISECONDS)

        logger.info { "Client sent message with time remaining: $timeRemaining" }

        responseObserver.onNext(Empty.getDefaultInstance())
        responseObserver.onCompleted()

    }

}
