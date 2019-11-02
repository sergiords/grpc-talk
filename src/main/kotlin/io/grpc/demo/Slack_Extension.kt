package io.grpc.demo

import io.grpc.ManagedChannel
import io.grpc.Server
import java.util.concurrent.TimeUnit
import java.util.logging.Logger
import kotlin.concurrent.thread

private val logger = Logger.getLogger("server")

fun Server.awaitShutdown() {

    Runtime.getRuntime().addShutdownHook(thread(start = false) {
        logger.info("Server is shutting down...")
        shutdown()
    })

    awaitTermination()

}

fun ManagedChannel.awaitShutdown(timeout: Long = 10L, timeUnit: TimeUnit = TimeUnit.SECONDS) {

    Runtime.getRuntime().addShutdownHook(thread(start = false) {
        logger.info("Client is shutting down...")
        shutdown()
    })

    awaitTermination(timeout, timeUnit)

}
