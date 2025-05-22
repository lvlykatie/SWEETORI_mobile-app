package com.example.sweetori;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweetori.adapter.AddToBagAdapter;
import com.example.sweetori.dto.response.ResCartDTO.CartDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class AddToBagActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AddToBagAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_to_bag);

        // Lấy JSON string từ intent
        String selectedItemsJson = getIntent().getStringExtra("selectedItems");

        // Chuyển JSON thành List<CartDetail>
        Type listType = new TypeToken<List<CartDetail>>() {}.getType();
        List<CartDetail> selectedItems = new Gson().fromJson(selectedItemsJson, listType);

        // Thiết lập RecyclerView
        recyclerView = findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddToBagAdapter(selectedItems, null); // null nếu không cần click listener
        recyclerView.setAdapter(adapter);
    }
}
