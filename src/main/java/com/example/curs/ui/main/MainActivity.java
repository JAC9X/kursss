package com.example.curs.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.curs.CursApplication;
import com.example.curs.R;
import com.example.curs.databinding.ActivityMainBinding;
import com.example.curs.models.BusinessCard;
import com.example.curs.ui.viewmodel.BusinessCardViewModel;
import com.example.curs.ui.viewmodel.BusinessCardViewModelFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private BusinessCardViewModel viewModel;
    private static final String TAG = "MainActivity";
    private String selectedTemplate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Удаление заголовка активности
        setTitle("");

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CursApplication application = (CursApplication) getApplication();
        BusinessCardViewModelFactory factory = new BusinessCardViewModelFactory(application.getRepository());
        viewModel = new ViewModelProvider(this, factory).get(BusinessCardViewModel.class);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        BusinessCardAdapter adapter = new BusinessCardAdapter(this);
        binding.recyclerView.setAdapter(adapter);

        // Настройка спиннера шаблонов
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.templates_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.templateSpinner.setAdapter(spinnerAdapter);

        binding.templateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTemplate = (String) parent.getItemAtPosition(position);
                Log.d(TAG, "Selected template: " + selectedTemplate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        binding.saveButton.setOnClickListener(v -> {
            String name = binding.name.getText().toString();
            String company = binding.company.getText().toString();
            String position = binding.position.getText().toString();
            String phone = binding.phone.getText().toString();
            String email = binding.email.getText().toString();

            BusinessCard businessCard = new BusinessCard(name, company, position, phone, email, selectedTemplate);
            viewModel.insert(businessCard);
            Log.d(TAG, "Inserted new BusinessCard: " + name + ", " + company + ", " + position + ", " + phone + ", " + email + ", " + selectedTemplate);
        });

        viewModel.getAllBusinessCards().observe(this, cards -> {
            if (cards != null) {
                adapter.submitList(cards);
                Log.d(TAG, "Data loaded: " + cards.size() + " items");
            } else {
                Log.d(TAG, "No data available");
            }
        });

        // Настройка кнопки для очистки истории
        binding.clearButton.setOnClickListener(v -> {
            viewModel.deleteAll();
            Log.d(TAG, "All business cards deleted.");
        });

        // Настройка кнопки для просмотра визиток
        binding.viewCardsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ViewCardsActivity.class);
            startActivity(intent);
        });
    }
}
