package com.example.myapplication.ui.viewmodels;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.data.model.Message;
import com.example.myapplication.data.repository.MessageRepository;

public class MainViewModel extends ViewModel {
    MessageRepository dataSource;
    MutableLiveData<String> audioUri = new MutableLiveData<>();

    public MainViewModel(MessageRepository dataSource) {
        this.dataSource = dataSource;
    }

    public void sendMessage(String audio) {
        dataSource.sendMessage(audio);
    }

    public LiveData<Message> getMessageLive() {
        return dataSource.getMessage();
    }

    public void setDataSource(MessageRepository dataSource) {
        this.dataSource = dataSource;
    }

    public MutableLiveData<String> getAudioUri() {
        return audioUri;
    }

    public void setAudioUri(String audioUri) {
        this.audioUri.postValue(audioUri);
    }
}
