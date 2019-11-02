package io.grpc.demo

import com.company.slack.Message
import com.google.common.io.BaseEncoding
import java.io.ByteArrayOutputStream
import java.util.logging.Logger

private val logger = Logger.getLogger("main")

private val baseEncoding = BaseEncoding.base16().upperCase()

fun main() {

    val byteOutputStream = ByteArrayOutputStream()

    Message.newBuilder()
        .setTimestamp(System.currentTimeMillis())
        .setText("world")
        .build()
        .writeTo(byteOutputStream)

    logger.info { baseEncoding.encode(byteOutputStream.toByteArray()) }

}
