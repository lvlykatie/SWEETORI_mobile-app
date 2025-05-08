package com.example.sweetori;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WishlistActivity extends AppCompatActivity {
    ImageView btnAccount;
    ImageView btnHome;
    ImageView btnCart;
    ImageView btnNoti;
    ImageView btnVoucher;
    Button delete;
    LinearLayout editList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishlist);
        EdgeToEdge.enable(this);

        //Component
        btnAccount = findViewById(R.id.btnAccount);
        btnHome = findViewById(R.id.btnHome);
        btnCart = findViewById(R.id.btnCart);
        btnNoti = findViewById(R.id.btnNoti);
        btnVoucher = findViewById(R.id.btnVoucher);
        delete = findViewById(R.id.delete);
        editList = findViewById(R.id.editList);

        //Intent
        btnAccount.setOnClickListener(v -> {
            Intent account = new Intent(this, AccountActivity.class);
            startActivity(account);
        });
        btnHome.setOnClickListener(v -> {
            Intent home = new Intent(this, MainActivity.class);
            startActivity(home);
        });
        btnCart.setOnClickListener(v -> {
            Intent cart = new Intent(this, CartActivity.class);
            startActivity(cart);
        });
        btnNoti.setOnClickListener(v -> {
            Intent noti = new Intent(this, NotiActivity.class);
            startActivity(noti);
        });
        btnVoucher.setOnClickListener(v -> {
            Intent voucher = new Intent(this, VoucherActivity.class);
            startActivity(voucher);
        });
    }
}