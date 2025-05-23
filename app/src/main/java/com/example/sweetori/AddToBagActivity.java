package com.example.sweetori;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweetori.adapter.AddToBagAdapter;
import com.example.sweetori.dto.response.ResCartDetailDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class AddToBagActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AddToBagAdapter adapter;
    Button checkoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_to_bag);

        checkoutBtn = findViewById(R.id.checkoutBtn);
        checkoutBtn.setOnClickListener(v -> {
            Intent noti = new Intent(AddToBagActivity.this, PaymentsActivity.class);
            startActivity(noti);
        });


        // Lấy JSON string từ intent
        String selectedItemsJson = getIntent().getStringExtra("selectedItems");

        // Chuyển JSON thành List<ResCartDetailDTO>
        Type listType = new TypeToken<List<ResCartDetailDTO>>() {}.getType();
        List<ResCartDetailDTO> selectedItems = new Gson().fromJson(selectedItemsJson, listType);

        // Thiết lập RecyclerView
        recyclerView = findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddToBagAdapter(selectedItems, null);
        recyclerView.setAdapter(adapter);
    }
}
