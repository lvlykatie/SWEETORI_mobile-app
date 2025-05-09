package com.example.sweetori;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.List;

public class NotiActivity extends AppCompatActivity {
    ImageView btnAccount;
    ImageView btnHome;
    ImageView btnCart;
    ImageView btnNoti;
    ImageView btnVoucher;

    Button btnPersonal, btnDiscount;
    FrameLayout tabContent;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify);
        EdgeToEdge.enable(this);

        //Component
        btnAccount = findViewById(R.id.btnAccount);
        btnHome = findViewById(R.id.btnHome);
        btnCart = findViewById(R.id.btnCart);
        btnNoti = findViewById(R.id.btnNoti);
        btnVoucher = findViewById(R.id.btnVoucher);
        btnPersonal = findViewById(R.id.btnPersonal);
        btnDiscount = findViewById(R.id.btnDiscount);
        tabContent = findViewById(R.id.tabContent);

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

        showTab(R.layout.tab_personal);

        btnPersonal.setOnClickListener(v -> {
            highlightTab(btnPersonal);
            showTab(R.layout.tab_personal);

        });

        btnDiscount.setOnClickListener(v -> {
            highlightTab(btnDiscount);
            showTab(R.layout.tab_discount);

        });
    }

    private void showTab(int layoutResId) {
        View view = getLayoutInflater().inflate(layoutResId, null);
        tabContent.removeAllViews();
        tabContent.addView(view);
    }

    private GradientDrawable getRoundedBackground(int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(60);
        return drawable;
    }
    private void highlightTab(Button activeTab) {
        // Tạo danh sách các tab để dễ dàng xử lý
        List<Button> tabs = Arrays.asList(btnPersonal, btnDiscount);

        // Reset tất cả tab về trong suốt và thay đổi màu chữ về mặc định
        for (Button tab : tabs) {
            tab.setBackground(null);
            tab.setTextColor(ContextCompat.getColor(this, R.color.black));}

        // Đặt màu nền cho tab đang được chọn
        activeTab.setBackground(getRoundedBackground(
                ContextCompat.getColor(this, R.color.color02)
        ));
        activeTab.setTextColor(ContextCompat.getColor(this, R.color.white));}

}