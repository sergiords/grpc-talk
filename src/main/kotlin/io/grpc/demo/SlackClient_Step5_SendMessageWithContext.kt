@file:Suppress("DuplicatedCode")

package io.grpc.demo

import com.company.slack.Empty
import com.company.slack.Message
import com.company.slack.SlackGrpc
import io.grpc.*
import io.grpc.stub.StreamObserver
import java.util.logging.Level
import java.util.logging.Logger

private val logger = Logger.getLogger("step5")

fun main() {

    val managedChannel = ManagedChannelBuilder
        .forAddress("localhost", 50051)
        .usePlaintext()
        .build()

    val stub = SlackGrpc.newStub(managedChannel)
        .withInterceptors(SlackClientInterceptor())

    val jwtContext = Context.current()
        .withValue(jwtContextKey, "some_jwt")

    jwtContext.run {

        val message = Message.newBuilder()
            .setTimestamp(System.currentTimeMillis())
            .setText("Hello world")
            .build()

        stub.sendMessage(message, object : StreamObserver<Empty> {

            override fun onNext(value: Empty) {
                logger.info { "Server sent an empty response" }
            }

            override fun onError(t: Throwable) {
                logger.log(Level.SEVERE, t) { "Server sent an error" }
            }

            override fun onCompleted() {
                logger.info("Server sent end of stream")
            }

        })

    }

    managedChannel.awaitShutdown()

}

class SlackClientInterceptor : ClientInterceptor {

    override fun <I, O> interceptCall(
        method: MethodDescriptor<I, O>,
        callOptions: CallOptions,
        next: Channel
    ) = object : ForwardingClientCall<I, O>() {

        val delegate = next.newCall(method, callOptions)

        override fun delegate(): ClientCall<I, O> = delegate

        override fun start(responseListener: Listener<O>, metadata: Metadata) {

            val jwt = jwtContextKey.get() ?: throw IllegalArgumentException("No JWT found in context")

            metadata.put(jwtMetadataKey, jwt)

            super.start(responseListener, metadata)
        }

    }

}
