package com.example.curs.repository;

import androidx.lifecycle.LiveData;
import com.example.curs.dao.BusinessCardDao;
import com.example.curs.models.BusinessCard;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BusinessCardRepository {
    private final ExecutorService executorService;
    private final BusinessCardDao businessCardDao;
    private final LiveData<List<BusinessCard>> allBusinessCards;

    public BusinessCardRepository(BusinessCardDao businessCardDao) {
        this.businessCardDao = businessCardDao;
        allBusinessCards = businessCardDao.getAllBusinessCards();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<BusinessCard>> getAllBusinessCards() {
        return allBusinessCards;
    }

    public void insert(BusinessCard businessCard) {
        executorService.execute(() -> businessCardDao.insert(businessCard));
    }

    public void update(BusinessCard businessCard) {
        executorService.execute(() -> businessCardDao.update(businessCard));
    }

    public void delete(BusinessCard businessCard) {
        executorService.execute(() -> businessCardDao.delete(businessCard));
    }

    public void deleteAll() {
        executorService.execute(businessCardDao::deleteAll);
    }
}
