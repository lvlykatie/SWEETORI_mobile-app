package com.example.sweetori;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweetori.adapter.ProductAdapter;
import com.example.sweetori.dto.response.ResProductDTO;

import java.util.ArrayList;
import java.util.List;

public class HomepageActivity extends AppCompatActivity {
    ImageView btnAccount, btnHome, btnCart, btnNoti, btnVoucher;
    TextView btnMore;
    HorizontalScrollView bannerScrollView;
    LinearLayout bannerContainer;
    private EditText searchInput;
    private ImageView searchIcon;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Kiểm tra token, chuyển tới SignIn nếu null
        String token = SharedPref.getAccessToken(this);
        if (token == null) {
            Intent loginIntent = new Intent(this, SignInActivity.class);
            loginIntent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
            );
            startActivity(loginIntent);
            finish();
            return;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        EdgeToEdge.enable(this);

        // Ánh xạ footer
        btnAccount = findViewById(R.id.btnAccount);
        btnHome    = findViewById(R.id.btnHome);
        btnCart    = findViewById(R.id.btnCart);
        btnNoti    = findViewById(R.id.btnNoti);
        btnVoucher = findViewById(R.id.btnVoucher);
        btnMore    = findViewById(R.id.btnMore);

        // Ánh xạ banner
        bannerScrollView = findViewById(R.id.bannerScrollView);
        bannerContainer  = findViewById(R.id.bannerContainer);

        // Intent cho footer
        btnAccount.setOnClickListener(v ->
                startActivity(new Intent(this, AccountActivity.class))
        );
        btnHome.setOnClickListener(v ->
                startActivity(new Intent(this, HomepageActivity.class))
        );
        btnCart.setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class))
        );
        btnNoti.setOnClickListener(v ->
                startActivity(new Intent(this, NotiActivity.class))
        );
        btnVoucher.setOnClickListener(v ->
                startActivity(new Intent(this, VoucherActivity.class))
        );
        btnMore.setOnClickListener(v ->
                startActivity(new Intent(this, ProductActivity.class))
        );

        // Auto-scroll banner
        bannerScrollView.post(() -> {
            int childCount = bannerContainer.getChildCount();
            final int[] currentIndex = {0};
            final Runnable[] runnable = new Runnable[1];
            runnable[0] = new Runnable() {
                @Override
                public void run() {
                    if (currentIndex[0] >= childCount) currentIndex[0] = 0;
                    View currentCard = bannerContainer.getChildAt(currentIndex[0]);
                    int scrollStep = currentCard.getWidth()
                            + ((LinearLayout.LayoutParams) currentCard.getLayoutParams()).rightMargin;
                    bannerScrollView.smoothScrollTo(scrollStep * currentIndex[0] + scrollStep, 0);
                    currentIndex[0]++;
                    new android.os.Handler().postDelayed(this, 3000);
                }
            };
            new android.os.Handler().postDelayed(runnable[0], 7000);
        });

        // Ánh xạ search
        searchInput = findViewById(R.id.searchInput);
        searchIcon  = findViewById(R.id.searchIcon);

        // --- CHỈNH 1: chuyển search sang ProductActivity ---
        searchIcon.setOnClickListener(v -> {
            String query = searchInput.getText()
                    .toString()
                    .trim();
            Intent intent = new Intent(HomepageActivity.this, ProductActivity.class);
            intent.putExtra("searchQuery", query);
            startActivity(intent);
        });
    }
}
