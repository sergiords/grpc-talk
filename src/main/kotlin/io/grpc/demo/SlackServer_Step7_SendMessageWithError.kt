@file:Suppress("DuplicatedCode", "ClassName")

package io.grpc.demo

import com.company.slack.Empty
import com.company.slack.Message
import com.company.slack.SlackGrpc
import io.grpc.ServerBuilder
import io.grpc.Status
import io.grpc.StatusException
import io.grpc.stub.StreamObserver
import java.util.logging.Logger.getLogger

private val logger = getLogger("step7")

fun main() {

    val server = ServerBuilder.forPort(50051)
        .addService(SlackService_SendMessageWithError())
        .build()
        .start()

    logger.info { "Server started, listening on ${server.port} 🚀" }

    server.awaitShutdown()

}

class SlackService_SendMessageWithError : SlackGrpc.SlackImplBase() {

    override fun sendMessage(value: Message, responseObserver: StreamObserver<Empty>) {

        // throw Exception("some description")

        val statusException = StatusException(Status.INTERNAL.withDescription("some description"))

        responseObserver.onError(statusException)

    }

}
