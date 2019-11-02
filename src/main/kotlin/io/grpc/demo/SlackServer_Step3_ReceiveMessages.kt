@file:Suppress("DuplicatedCode", "ClassName")

package io.grpc.demo

import com.company.slack.Empty
import com.company.slack.Message
import com.company.slack.SlackGrpc
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import java.util.logging.Logger.getLogger

private val logger = getLogger("step3")

fun main() {

    val server = ServerBuilder.forPort(50051)
        .addService(SlackService_ReceiveMessage())
        .build()
        .start()

    logger.info { "Server started, listening on ${server.port} 🚀" }

    server.awaitShutdown()

}


class SlackService_ReceiveMessage : SlackGrpc.SlackImplBase() {

    override fun receiveMessages(request: Empty, responseObserver: StreamObserver<Message>) {

        responseObserver.onNext(
            Message.newBuilder()
                .setText("Message from server #1")
                .setTimestamp(System.currentTimeMillis())
                .build()
        )

        Thread.sleep(1_000)

        responseObserver.onNext(
            Message.newBuilder()
                .setText("Message from server #2")
                .setTimestamp(System.currentTimeMillis())
                .build()
        )

        responseObserver.onCompleted()

    }

}
