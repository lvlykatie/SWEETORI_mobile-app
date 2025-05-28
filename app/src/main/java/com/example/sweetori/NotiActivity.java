package com.example.sweetori;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweetori.adapter.NotifyAdapter;
import com.example.sweetori.content.ProductFetching;
import com.example.sweetori.dto.response.ResLoginDTO;
import com.example.sweetori.dto.response.ResProductDTO;
import com.example.sweetori.dto.response.ResUserDTO;
import com.google.gson.Gson;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotiActivity extends AppCompatActivity {
    ImageView btnAccount;
    ImageView btnHome;
    ImageView btnCart;
    ImageView btnNoti;
    ImageView btnVoucher;

    Button btnPersonal, btnDiscount;
    FrameLayout tabContent;
    TextView txtHello;
    RecyclerView notifyRecyclerView;
    private ResUserDTO currentUser;
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
        txtHello = findViewById(R.id.txtHello);
        notifyRecyclerView = findViewById(R.id.notifyRecyclerView);
        notifyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load user name from SharedPreferences
        currentUser = SharedPref.getUser(this);

        if (currentUser != null && currentUser.getFirstName() != null) {
            txtHello.setText("Hello, " + currentUser.getFirstName());
        } else {
            txtHello.setText("Guest");
        }

        // Intent for bottom buttons
        btnAccount.setOnClickListener(v -> startActivity(new Intent(this, AccountActivity.class)));
        btnHome.setOnClickListener(v -> startActivity(new Intent(this, HomepageActivity.class)));
        btnCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        btnVoucher.setOnClickListener(v -> startActivity(new Intent(this, VoucherActivity.class)));

        // Tabs
        btnPersonal.setOnClickListener(v -> {
            highlightTab(btnPersonal);
            tabContent.setVisibility(View.GONE);
            notifyRecyclerView.setVisibility(View.VISIBLE);
            getData();
        });

        btnDiscount.setOnClickListener(v -> {
            highlightTab(btnDiscount);
            notifyRecyclerView.setVisibility(View.GONE);
            tabContent.setVisibility(View.VISIBLE);
            showTab(R.layout.tab_discount);
        });

        // Mặc định chọn tab Personal khi mở activity
        highlightTab(btnPersonal);
        btnPersonal.performClick();
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
        List<Button> tabs = Arrays.asList(btnPersonal, btnDiscount);
        for (Button tab : tabs) {
            tab.setBackground(null);
            tab.setTextColor(ContextCompat.getColor(this, R.color.black));
        }
        activeTab.setBackground(getRoundedBackground(ContextCompat.getColor(this, R.color.color02)));
        activeTab.setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    private void getData() {
        Pair<String, Integer> accessTokenWithUserId = SharedPref.getAccessTokenWithUserId(NotiActivity.this);
        if (accessTokenWithUserId == null) {
            Log.e("ProductAPI", "Token hoặc UserId không hợp lệ");
            return;
        }

        ProductFetching api = APIClient.getClientWithToken(accessTokenWithUserId.first).create(ProductFetching.class);
        api.getAllProducts().enqueue(new Callback<APIResponse<ResProductDTO>>() {
            @Override
            public void onResponse(Call<APIResponse<ResProductDTO>> call, Response<APIResponse<ResProductDTO>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<ResProductDTO.ProductData> products = response.body().getData().getData();

                    List<ResProductDTO.ProductData> filteredProducts = new ArrayList<>();
                    for (ResProductDTO.ProductData product : products) {
                        if (product != null && product.getDiscount() != null
                                && isWithinDiscountPeriod(product.getDiscount().getStartDate(), product.getDiscount().getEndDate())) {
                            filteredProducts.add(product);
                        }
                    }
                    NotifyAdapter adapter = new NotifyAdapter(filteredProducts);
                    adapter.setOnItemClickListener(product -> {
                        Intent intent = new Intent(NotiActivity.this, ProductDetailActivity.class);
                        intent.putExtra("product", product);
                        startActivity(intent);
                    });
                    notifyRecyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(NotiActivity.this, "Không có dữ liệu sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse<ResProductDTO>> call, Throwable t) {
                Toast.makeText(NotiActivity.this, "Lỗi khi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isWithinDiscountPeriod(String startDateStr, String endDateStr) {
        try {
            LocalDate startDate = OffsetDateTime.parse(startDateStr).toLocalDate();
            LocalDate endDate = OffsetDateTime.parse(endDateStr).toLocalDate();
            LocalDate currentDate = LocalDate.now();
            return (currentDate.isEqual(startDate) || currentDate.isAfter(startDate)) &&
                    (currentDate.isEqual(endDate) || currentDate.isBefore(endDate));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
