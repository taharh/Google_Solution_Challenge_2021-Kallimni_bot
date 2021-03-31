package com.example.myapplication.ui.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.data.repository.MessageRepository;
import com.example.myapplication.ui.viewmodels.MainViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private MessageRepository data;

    public ViewModelFactory(MessageRepository data) {
        this.data = data;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class))
            return (T) new MainViewModel(data);
        throw new IllegalArgumentException("Unsupported ViewModel type");
    }
}
