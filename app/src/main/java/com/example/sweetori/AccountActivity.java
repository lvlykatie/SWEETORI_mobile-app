package com.example.sweetori;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.graphics.drawable.GradientDrawable;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.Arrays;
import java.util.List;


public class AccountActivity extends AppCompatActivity {

    Button btnGeneral, btnPurchase, btnSupport;
    FrameLayout tabContent;
    LinearLayout btnLogOut;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);
        EdgeToEdge.enable(this);

        btnGeneral = findViewById(R.id.tabGeneral);
        btnPurchase = findViewById(R.id.tabPurchase);
        btnSupport = findViewById(R.id.tabSupport);
        tabContent = findViewById(R.id.tabContent);

        // Mặc định hiển thị tab General
        showTab(R.layout.tab_general);

        btnGeneral.setOnClickListener(v -> {
            highlightTab(btnGeneral);
            showTab(R.layout.tab_general);

            btnLogOut = findViewById(R.id.btnLogOut);

            //Lắng nghe sự kiện click
            btnLogOut.setOnClickListener(logOutV -> {
                // Chuyển đến ShoppingActivity
                Intent logout = new Intent(AccountActivity.this, SignInActivity.class);
                startActivity(logout);
            });
        });

        btnPurchase.setOnClickListener(v -> {
            highlightTab(btnPurchase);
            showTab(R.layout.tab_purchase);
        });

        btnSupport.setOnClickListener(v -> {
            highlightTab(btnSupport);
            showTab(R.layout.tab_support);
        });

        btnLogOut = findViewById(R.id.btnLogOut);

        //Lắng nghe sự kiện click
        btnLogOut.setOnClickListener(v -> {
            Intent logout = new Intent(AccountActivity.this, SignInActivity.class);
            startActivity(logout);
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
        List<Button> tabs = Arrays.asList(btnGeneral, btnPurchase, btnSupport);

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