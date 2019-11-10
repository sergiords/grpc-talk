# [gRPC](https://grpc.io)

## Code generation
For Java / Kotlin:
```
./gradlew generateProto
```

For Go:
```
protoc -I src/main/proto src/main/proto/demo.proto --go_out=plugins=grpc:go_workspace/src/io.grpdc.demo/generated
```

## Build
For Java / Kotlin:
```
./gradlew build
```

For Go:
```
go build -o build io.grpdc.demo
```

## Run
For Go:
```
go run io.grpdc.demo
```
or 
```
./build/io.grpdc.demo
```

System properties for Java logging configuration:
```
-Duser.language=en \
-Djava.util.logging.config.file=./src/main/resources/logging.properties
```
