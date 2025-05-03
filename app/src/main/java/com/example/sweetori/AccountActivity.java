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

import com.example.sweetori.content.AuthFetching;

import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


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
            String accessToken = SharedPref.getAccessToken(AccountActivity.this);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request request = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer " + accessToken)
                                .build();
                        return chain.proceed(request);
                    }).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://spring-shop.onrender.com/auth/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            AuthFetching authFetching = retrofit.create(AuthFetching.class);

            authFetching.logout().enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    SharedPref.clearTokens(AccountActivity.this);

                    Intent loginIntent = new Intent(AccountActivity.this, SignInActivity.class);
                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear back stack
                    startActivity(loginIntent);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
//                        // Có thể hiển thị lỗi, nhưng vẫn nên xoá token
//                        SharedPref.clearTokens(HomepageActivity.this);
//                        Intent loginIntent = new Intent(HomepageActivity.this, SignInActivity.class);
//                        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(loginIntent);
                }
            });
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