package com.example.curs.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.curs.models.BusinessCard;
import com.example.curs.repository.BusinessCardRepository;
import java.util.List;

public class BusinessCardViewModel extends ViewModel {
    private final BusinessCardRepository repository;
    private final LiveData<List<BusinessCard>> allBusinessCards;

    public BusinessCardViewModel(BusinessCardRepository repository) {
        this.repository = repository;
        allBusinessCards = repository.getAllBusinessCards();
    }

    public LiveData<List<BusinessCard>> getAllBusinessCards() {
        return allBusinessCards;
    }

    public void insert(BusinessCard businessCard) {
        repository.insert(businessCard);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
