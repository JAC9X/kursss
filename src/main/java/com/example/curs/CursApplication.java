package com.example.curs;

import android.app.Application;
import com.example.curs.database.AppDatabase;
import com.example.curs.repository.BusinessCardRepository;

public class CursApplication extends Application {
    private AppDatabase database;
    private BusinessCardRepository repository;

    @Override
    public void onCreate() {
        super.onCreate();
        database = AppDatabase.getDatabase(this);
        repository = new BusinessCardRepository(database.businessCardDao());
    }

    public BusinessCardRepository getRepository() {
        return repository;
    }
}

