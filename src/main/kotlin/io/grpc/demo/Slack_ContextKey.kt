package io.grpc.demo

import io.grpc.Context
import io.grpc.Metadata

val jwtMetadataKey: Metadata.Key<String> = Metadata.Key.of<String>("JWT", Metadata.ASCII_STRING_MARSHALLER)

val jwtContextKey: Context.Key<String> = Context.key<String>("JWT")
