package com.example.curs.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.curs.models.BusinessCard;

import java.util.List;

@Dao
public interface BusinessCardDao {
    @Insert
    void insert(BusinessCard businessCard);

    @Update
    void update(BusinessCard businessCard);

    @Delete
    void delete(BusinessCard businessCard);

    @Query("DELETE FROM business_cards")
    void deleteAll();

    @Query("SELECT * FROM business_cards ORDER BY name ASC")
    LiveData<List<BusinessCard>> getAllBusinessCards();
}
