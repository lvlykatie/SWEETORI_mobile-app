package com.example.sweetori;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweetori.adapter.ProductAdapter;
import com.example.sweetori.dto.response.ResProductDTO;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    private ImageView btnAccount, btnHome, btnCart, btnNoti, btnVoucher;
    private RecyclerView productItemRecyclerView;
    private ProductAdapter productAdapter;
    private List<ResProductDTO.ProductData> productList = new ArrayList<>();
    private EditText searchInput;
    private ImageView searchIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product);

        // Ánh xạ view
        btnAccount            = findViewById(R.id.btnAccount);
        btnHome               = findViewById(R.id.btnHome);
        btnCart               = findViewById(R.id.btnCart);
        btnNoti               = findViewById(R.id.btnNoti);
        btnVoucher            = findViewById(R.id.btnVoucher1);
        productItemRecyclerView = findViewById(R.id.productItemRecyclerView);
        searchInput           = findViewById(R.id.searchInput);
        searchIcon            = findViewById(R.id.searchIcon);

        // Setup RecyclerView
        productItemRecyclerView.setHasFixedSize(true);
        productItemRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(productList, this);
        productItemRecyclerView.setAdapter(productAdapter);

        // Load cached data vào list gốc và adapter
        List<ResProductDTO.ProductData> cached = ResProductDTO.ProductDataManager
                .getInstance().getProductList();
        if (cached != null && !cached.isEmpty()) {
            productList.clear();
            productList.addAll(cached);
            productAdapter.notifyDataSetChanged();
            Log.d("PRODUCT_ACTIVITY", "Loaded cached data. Items: " + productList.size());
        } else {
            Log.e("PRODUCT_ACTIVITY", "No cached data available");
        }

        // Footer navigation (giữ nguyên)
        btnAccount.setOnClickListener(v -> startActivity(new Intent(this, AccountActivity.class)));
        btnHome   .setOnClickListener(v -> startActivity(new Intent(this, HomepageActivity.class)));
        btnCart   .setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        btnNoti   .setOnClickListener(v -> startActivity(new Intent(this, NotiActivity.class)));
        btnVoucher.setOnClickListener(v -> startActivity(new Intent(this, VoucherActivity.class)));

        // --- CHỈNH 2: nhận query từ Homepage ---
        String incomingQuery = getIntent().getStringExtra("searchQuery");
        if (incomingQuery != null) {
            // hiển thị trong EditText
            searchInput.setText(incomingQuery);
            // thực thi lọc ngay
            applyFilter(incomingQuery.trim().toLowerCase());
        }

        // --- CHỈNH 3: xử lý search nội bộ ---
        searchIcon.setOnClickListener(v -> {
            String q = searchInput.getText().toString().trim().toLowerCase();
            applyFilter(q);
        });
    }

    // Hàm lọc dùng chung
    private void applyFilter(String q) {
        List<ResProductDTO.ProductData> tmp = ResProductDTO.ProductDataManager
                .getInstance().getProductList();
        List<ResProductDTO.ProductData> filtered = new ArrayList<>();
        if (q.isEmpty()) {
            filtered.addAll(tmp);
        } else {
            for (ResProductDTO.ProductData p : tmp) {
                String name = p.getProductName();
                if (name != null && name.toLowerCase().contains(q)) {
                    filtered.add(p);
                }
            }
        }
        productAdapter.updateProductList(filtered);
    }
}
