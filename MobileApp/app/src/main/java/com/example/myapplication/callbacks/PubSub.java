package com.example.myapplication.callbacks;

public interface PubSub {
    void subscribe(String subscription);

    void publish(String topicName, String message) throws InterruptedException;
}
