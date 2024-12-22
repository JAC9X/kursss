package com.example.curs.ui.main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.curs.CursApplication;
import com.example.curs.databinding.ActivityViewCardsBinding;
import com.example.curs.ui.viewmodel.BusinessCardViewModel;
import com.example.curs.ui.viewmodel.BusinessCardViewModelFactory;

public class ViewCardsActivity extends AppCompatActivity {

    private ActivityViewCardsBinding binding;
    private BusinessCardViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewCardsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CursApplication application = (CursApplication) getApplication();
        BusinessCardViewModelFactory factory = new BusinessCardViewModelFactory(application.getRepository());
        viewModel = new ViewModelProvider(this, factory).get(BusinessCardViewModel.class);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        BusinessCardAdapter adapter = new BusinessCardAdapter(this);
        binding.recyclerView.setAdapter(adapter);

        viewModel.getAllBusinessCards().observe(this, cards -> {
            if (cards != null) {
                adapter.submitList(cards);
            }
        });

        // Настройка кнопки для возврата к созданию визиток
        binding.backButton.setOnClickListener(v -> finish());
    }
}
