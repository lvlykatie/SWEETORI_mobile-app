package com.example.sweetori;

import android.content.Intent;
import android.os.Bundle;
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

public class HomepageActivity extends AppCompatActivity {
    ImageView btnAccount;
    ImageView btnHome;
    ImageView btnCart;
    ImageView btnNoti;
    ImageView btnVoucher;
    HorizontalScrollView bannerScrollView;
    LinearLayout bannerContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String token = SharedPref.getAccessToken(this);
        if (token == null) {
            Intent loginIntent = new Intent(this, SignInActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
            finish(); // Không cho user quay lại Homepage
            return; // Dừng không chạy tiếp
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        EdgeToEdge.enable(this);

        //Ánh xạ component
        //Footer
        btnAccount = findViewById(R.id.btnAccount);
        btnHome = findViewById(R.id.btnHome);
        btnCart = findViewById(R.id.btnCart);
        btnNoti = findViewById(R.id.btnNoti);
        btnVoucher = findViewById(R.id.btnVoucher);

        bannerScrollView = findViewById(R.id.bannerScrollView);
        bannerContainer = findViewById(R.id.bannerContainer);

        //Intent
        btnAccount.setOnClickListener(v -> {
            Intent account = new Intent(HomepageActivity.this, AccountActivity.class);
            startActivity(account);
        });
        btnHome.setOnClickListener(v -> {
            Intent home = new Intent(HomepageActivity.this, HomepageActivity.class);
            startActivity(home);
        });
        btnCart.setOnClickListener(v -> {
            Intent cart = new Intent(HomepageActivity.this, CartActivity.class);
            startActivity(cart);
        });
        btnNoti.setOnClickListener(v -> {
            Intent noti = new Intent(HomepageActivity.this, NotiActivity.class);
            startActivity(noti);
        });
        btnVoucher.setOnClickListener(v -> {
            Intent voucher = new Intent(HomepageActivity.this, VoucherActivity.class);
            startActivity(voucher);
        });

        final Runnable[] runnable = new Runnable[1];
        final int[] scrollX = {0};


        bannerScrollView.post(() -> { // <-- Đúng tên biến đã khai báo
            int childCount = bannerContainer.getChildCount();
            final int[] currentIndex = {0};

            runnable[0] = new Runnable() {
                @Override
                public void run() {
                    if (currentIndex[0] >= childCount) currentIndex[0] = 0;

                    View currentCard = bannerContainer.getChildAt(currentIndex[0]);
                    int scrollStep = currentCard.getWidth()
                            + ((LinearLayout.LayoutParams) currentCard.getLayoutParams()).rightMargin;

                    scrollX[0] += scrollStep;
                    bannerScrollView.smoothScrollTo(scrollX[0], 0);
                    currentIndex[0]++;

                    new android.os.Handler().postDelayed(this, 3000); // hoặc khai báo handler ở ngoài
                }
            };
            new android.os.Handler().postDelayed(runnable[0], 7000);
        });

    }
}