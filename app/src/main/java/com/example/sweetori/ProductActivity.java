package com.example.sweetori;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweetori.adapter.ProductAdapter;
import com.example.sweetori.content.ProductFetching;
import com.example.sweetori.dto.response.ResProductDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {

    private ImageView btnAccount, btnHome, btnCart, btnNoti, btnVoucher;
    private RecyclerView productItemRecyclerView;
    private ProductAdapter productAdapter;
    private List<ResProductDTO.ProductData> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product);

        // Ánh xạ view
        btnAccount = findViewById(R.id.btnAccount);
        btnHome = findViewById(R.id.btnHome);
        btnCart = findViewById(R.id.btnCart);
        btnNoti = findViewById(R.id.btnNoti);
        btnVoucher = findViewById(R.id.btnVoucher1);
        productItemRecyclerView = findViewById(R.id.productItemRecyclerView);

        // Cài đặt RecyclerView
        productItemRecyclerView.setHasFixedSize(true);
        productItemRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(productList, ProductActivity.this);
        productItemRecyclerView.setAdapter(productAdapter);

        List<ResProductDTO.ProductData> cachedProductList = ResProductDTO.ProductDataManager.getInstance().getProductList();
        if (cachedProductList != null && !cachedProductList.isEmpty()) {
            productList.clear();
            productList.addAll(cachedProductList);
            productAdapter.notifyDataSetChanged();
            Log.d("PRODUCT_ACTIVITY", "Loaded cached data. Items: " + productList.size());
        } else {
            Log.e("PRODUCT_ACTIVITY", "No cached data available");
        }
        // Điều hướng các button
        btnAccount.setOnClickListener(v -> startActivity(new Intent(this, AccountActivity.class)));
        btnHome.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        btnCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        btnNoti.setOnClickListener(v -> startActivity(new Intent(this, NotiActivity.class)));
        btnVoucher.setOnClickListener(v -> startActivity(new Intent(this, VoucherActivity.class)));
    }
}