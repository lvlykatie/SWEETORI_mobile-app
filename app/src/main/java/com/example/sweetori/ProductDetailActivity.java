package com.example.sweetori;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView btnAccount;
    ImageView btnHome;
    ImageView btnCart;
    ImageView btnNoti;
    ImageView btnVoucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);
        EdgeToEdge.enable(this);

        //Component
        btnAccount = findViewById(R.id.btnAccount);
        btnHome = findViewById(R.id.btnHome);
        btnCart = findViewById(R.id.btnCart);
        btnNoti = findViewById(R.id.btnNoti);
        btnVoucher = findViewById(R.id.btnVoucher1);

        //Intent
        btnAccount.setOnClickListener(v -> {
            Intent account = new Intent(ProductDetailActivity.this, AccountActivity.class);
            startActivity(account);
        });
        btnHome.setOnClickListener(v -> {
            Intent home = new Intent(ProductDetailActivity.this, MainActivity.class);
            startActivity(home);
        });
        btnCart.setOnClickListener(v -> {
            Intent cart = new Intent(ProductDetailActivity.this, CartActivity.class);
            startActivity(cart);
        });
        btnNoti.setOnClickListener(v -> {
            Intent noti = new Intent(ProductDetailActivity.this, NotiActivity.class);
            startActivity(noti);
        });
        btnVoucher.setOnClickListener(v -> {
            Intent voucher = new Intent(ProductDetailActivity.this, VoucherActivity.class);
            startActivity(voucher);
        });
    }
}