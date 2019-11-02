@file:Suppress("DuplicatedCode", "ClassName")

package io.grpc.demo

import com.company.slack.Empty
import com.company.slack.Message
import com.company.slack.SlackGrpc
import io.grpc.*
import io.grpc.stub.StreamObserver
import java.util.logging.Logger.getLogger

private val logger = getLogger("step5")

fun main() {

    val server = ServerBuilder.forPort(50051)
        .addService(SlackService_SendMessageWithContext())
        .intercept(SlackServerInterceptor())
        .build()
        .start()

    logger.info { "Server started, listening on ${server.port} 🚀" }

    server.awaitShutdown()

}


class SlackService_SendMessageWithContext : SlackGrpc.SlackImplBase() {

    override fun sendMessage(value: Message, responseObserver: StreamObserver<Empty>) {

        val jwt = jwtContextKey.get() ?: throw IllegalArgumentException("No JWT found in context")

        logger.info { "Client sent message with jwt: $jwt" }

        responseObserver.onNext(Empty.getDefaultInstance())
        responseObserver.onCompleted()

    }

}

class SlackServerInterceptor : ServerInterceptor {

    override fun <I, O> interceptCall(
        call: ServerCall<I, O>,
        metadata: Metadata,
        next: ServerCallHandler<I, O>
    ): ServerCall.Listener<I> {

        val jwt = metadata.get<String>(jwtMetadataKey)

        val context = Context.current()
            .withValue(jwtContextKey, jwt)

        return Contexts.interceptCall(context, call, metadata, next)

    }

}
