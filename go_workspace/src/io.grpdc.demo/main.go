package main

import (
	"context"
	"google.golang.org/grpc"
	demo "io.grpdc.demo/generated"
	"log"
	"time"
)

func main() {

	connection, connectionError := grpc.Dial("localhost:50051", grpc.WithInsecure())
	if connectionError != nil {
		log.Fatalf("did not connect: %v", connectionError)
	}

	defer func() {
		if closeError := connection.Close(); closeError != nil {
			log.Fatalf("did not closed connection: %v", closeError)
		}
	}()

	callContext, cancel := context.WithTimeout(context.Background(), time.Second)
	defer cancel()

	message := &demo.Message{Text: "Hello from Gopher", Timestamp: time.Now().UnixNano() / 1e6}

	slackClient := demo.NewSlackClient(connection)
	_, callError := slackClient.SendMessage(callContext, message)
	if callError != nil {
		log.Fatalf("Got error response: %v", callError)
	}

	log.Printf("Got empty response")

}
