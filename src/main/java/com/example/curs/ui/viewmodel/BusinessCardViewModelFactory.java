package com.example.curs.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.curs.repository.BusinessCardRepository;

public class BusinessCardViewModelFactory implements ViewModelProvider.Factory {
    private final BusinessCardRepository repository;

    public BusinessCardViewModelFactory(BusinessCardRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(BusinessCardViewModel.class)) {
            return (T) new BusinessCardViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

