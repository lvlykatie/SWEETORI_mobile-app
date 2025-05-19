package com.example.sweetori;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
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
import android.widget.Toast;

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

    ImageView btnAccount;
    ImageView btnHome;
    ImageView btnCart;
    ImageView btnNoti;
    ImageView btnVoucher;
    Button btnGeneral, btnPurchase, btnSupport;
    FrameLayout tabContent;
    LinearLayout btnLogOut;
    LinearLayout btnResetPass;
    LinearLayout btn_wishlist;


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
        btnAccount = findViewById(R.id.btnAccount);
        btnHome = findViewById(R.id.btnHome);
        btnCart = findViewById(R.id.btnCart);
        btnNoti = findViewById(R.id.btnNoti);
        btnVoucher = findViewById(R.id.btnVoucher);
        btnLogOut = findViewById(R.id.btnLogOut);
        btn_wishlist = findViewById(R.id.btn_wishlist);

        btnHome.setOnClickListener(v -> {
            Intent home = new Intent(AccountActivity.this, HomepageActivity.class);
            startActivity(home);
        });
        btnCart.setOnClickListener(v -> {
            Intent cart = new Intent(AccountActivity.this, CartActivity.class);
            startActivity(cart);
        });
        btnNoti.setOnClickListener(v -> {
            Intent noti = new Intent(AccountActivity.this, NotiActivity.class);
            startActivity(noti);
        });
        btnVoucher.setOnClickListener(v -> {
            Intent voucher = new Intent(AccountActivity.this, VoucherActivity.class);
            startActivity(voucher);
        });
        // Mặc định hiển thị tab General
        showTab(R.layout.tab_general);

        btnGeneral.setOnClickListener(v -> {
            highlightTab(btnGeneral);
            showTab(R.layout.tab_general);

            //Lắng nghe sự kiện click
            btnLogOut.setOnClickListener(v1 -> {
                String accessToken = SharedPref.getAccessToken(AccountActivity.this);

                AuthFetching authFetching = APIClient.getClientWithToken(accessToken).create(AuthFetching.class);

                authFetching.logout().enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(AccountActivity.this, "Sign out successfully!", Toast.LENGTH_SHORT).show();

                        SharedPref.clearTokens(AccountActivity.this);

                        Intent loginIntent = new Intent(AccountActivity.this, SignInActivity.class);
                        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear back stack
                        startActivity(loginIntent);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(AccountActivity.this, "Lỗi khi đăng xuất: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });

            btn_wishlist.setOnClickListener(v1 -> {
                Intent intent = new Intent(AccountActivity.this, WishlistActivity.class);
                startActivity(intent);
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

        btn_wishlist = findViewById(R.id.btn_wishlist);
        btn_wishlist.setOnClickListener(v -> {
            Intent wishlist = new Intent(AccountActivity.this, WishlistActivity.class);
            startActivity(wishlist);
        });

        btnVoucher.setOnClickListener(v -> {
            Intent voucher = new Intent(AccountActivity.this, VoucherActivity.class);
            startActivity(voucher);
        });

        btnResetPass = findViewById(R.id.btn_reset_password);
        btnResetPass.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, CreatePassActivity.class);
            startActivity(intent);
        });

        btnLogOut = findViewById(R.id.btnLogOut);

        //Lắng nghe sự kiện click
        btnLogOut.setOnClickListener(v -> {
            String accessToken = SharedPref.getAccessToken(AccountActivity.this);

            AuthFetching authFetching = APIClient.getClientWithToken(accessToken).create(AuthFetching.class);

            authFetching.logout().enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast.makeText(AccountActivity.this, "Sign out successfully!", Toast.LENGTH_SHORT).show();

                    SharedPref.clearTokens(AccountActivity.this);

                    Intent loginIntent = new Intent(AccountActivity.this, SignInActivity.class);
                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear back stack
                    startActivity(loginIntent);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(AccountActivity.this, "Lỗi khi đăng xuất: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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