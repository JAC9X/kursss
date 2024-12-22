package com.example.curs.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "business_cards")
public class BusinessCard {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private final String name;
    private final String company;
    private final String position; // Добавлено поле должности
    private final String phone;
    private final String email;
    private final String template; // Добавлено поле шаблона

    public BusinessCard(String name, String company, String position, String phone, String email, String template) {
        this.name = name;
        this.company = company;
        this.position = position;
        this.phone = phone;
        this.email = email;
        this.template = template;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }

    public String getPosition() {
        return position;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getTemplate() {
        return template;
    }
}
