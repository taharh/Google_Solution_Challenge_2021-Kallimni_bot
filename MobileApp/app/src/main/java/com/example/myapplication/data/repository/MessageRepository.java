package com.example.myapplication.data.repository;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.data.model.Message;

public interface MessageRepository {
    LiveData<Message> getMessage();
    void sendMessage(String audio);
}
