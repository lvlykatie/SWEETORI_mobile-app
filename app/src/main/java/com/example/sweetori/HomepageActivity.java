package com.example.sweetori;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.sweetori.content.ProductFetching;
import com.example.sweetori.dto.response.ResLoginDTO;
import com.example.sweetori.dto.response.ResProductDTO;
import com.example.sweetori.dto.response.ResUserDTO;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomepageActivity extends AppCompatActivity {
    ImageView btnAccount, btnHome, btnCart, btnNoti, btnVoucher;
    TextView btnMore, tvUserName;
    HorizontalScrollView bannerScrollView;
    LinearLayout bannerContainer;
    private EditText searchInput;
    private ImageView searchIcon;
    private ProductAdapter productAdapter;
    private ResUserDTO currentUser;
    // best sellerAdd commentMore actions
    private RecyclerView recyclerHighlyRec;
    private ProductAdapter bestSellerAdapter;
    private static final int HIGHLY_REC_LIMIT = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Kiểm tra token, chuyển tới SignIn nếu null
        Pair<String, Integer> accessTokenWithUserId = SharedPref.getAccessTokenWithUserId(HomepageActivity.this);

        if (accessTokenWithUserId.first == null) {
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
        tvUserName = findViewById(R.id.tvUserName);

        // Ánh xạ banner
        bannerScrollView = findViewById(R.id.bannerScrollView);
        bannerContainer  = findViewById(R.id.bannerContainer);

        // Intent cho footer
        currentUser = SharedPref.getUser(this);
        if (currentUser != null && currentUser.getFirstName() != null) {
            tvUserName.setText("Hello, " + currentUser.getFirstName());
        } else {
            tvUserName.setText("Guest");
        }
        setUpProductFetching();
        btnAccount.setOnClickListener(v -> {
            Intent account = new Intent(HomepageActivity.this, AccountActivity.class);// user là ResUserDTO
            startActivity(account);
        });
        btnCart.setOnClickListener(v -> {
            Intent cart = new Intent(HomepageActivity.this, CartActivity.class);
            startActivity(cart);
        });
        btnNoti.setOnClickListener(v ->{
            Intent noti = new Intent(HomepageActivity.this, NotiActivity.class);
            startActivity(noti);
        });
        btnVoucher.setOnClickListener(v ->{
            Intent voucher = new Intent(HomepageActivity.this, VoucherActivity.class);
            startActivity(voucher);
        });
        btnMore.setOnClickListener(v ->{
            Intent more = new Intent(HomepageActivity.this, ProductActivity.class);
            startActivity(more);
        });
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

        // —— Phần mới: ánh xạ container và gán click listener cho Categories & Brands ——
        LinearLayout categoryContainer = findViewById(R.id.categoryContainer1);
        LinearLayout categoryContainer2 = findViewById(R.id.categoryContainer2);
        LinearLayout brandContainer    = findViewById(R.id.brandContainer);

        View.OnClickListener onFilterClick = v -> {
            // v là LinearLayout item, childAt(1) là TextView chứa tên
            TextView tv = (TextView)((ViewGroup)v).getChildAt(1);
            String q = tv.getText().toString().trim();
            Intent intent = new Intent(HomepageActivity.this, ProductActivity.class);
            intent.putExtra("searchQuery", q);
            startActivity(intent);
        };

        // Đăng ký cho từng mục trong categoryContainer
        for (int i = 0; i < categoryContainer.getChildCount(); i++) {
            categoryContainer.getChildAt(i).setOnClickListener(onFilterClick);
        }
        for (int i = 0; i < categoryContainer2.getChildCount(); i++) {
            categoryContainer2.getChildAt(i).setOnClickListener(onFilterClick);
        }
        // Đăng ký cho từng mục trong brandContainer
        for (int i = 0; i < brandContainer.getChildCount(); i++) {
            brandContainer.getChildAt(i).setOnClickListener(onFilterClick);
        }
        // Ánh xạ RecyclerView highly recommendAdd commentMore actions
        recyclerHighlyRec = findViewById(R.id.recyclerBestSeller);
        LinearLayoutManager lm = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        recyclerHighlyRec.setLayoutManager(lm);
        bestSellerAdapter = new ProductAdapter(new ArrayList<>(), this);
        recyclerHighlyRec.setAdapter(bestSellerAdapter);
        recyclerHighlyRec.setNestedScrollingEnabled(false);
        recyclerHighlyRec.setHasFixedSize(true);


        loadBestSellerProducts();
    }

    private void loadBestSellerProducts() {
        APIClient.getClientWithToken(
                        SharedPref.getAccessTokenWithUserId(this).first
                ).create(ProductFetching.class)
                .getAllProducts()
                .enqueue(new Callback<APIResponse<ResProductDTO>>() {
                    @Override
                    public void onResponse(Call<APIResponse<ResProductDTO>> call,
                                           Response<APIResponse<ResProductDTO>> res) {
                        if (!res.isSuccessful() || res.body() == null) return;
//                        List<ResProductDTO.ProductData> all = res.body().getData().getProducts();
                        List<ResProductDTO.ProductData> all = res.body().getData().getData();
                        // Sắp xếp theo avgRate giảm dần, rồi quantity giảm dần
                        Collections.sort(all, (a, b) -> Double.compare(b.getAvgRate(), a.getAvgRate()));
                        // Lấy top N
                        List<ResProductDTO.ProductData> top = all.size() > HIGHLY_REC_LIMIT
                                ? all.subList(0, HIGHLY_REC_LIMIT)
                                : all;
                        // Cập nhật adapter
                        bestSellerAdapter.updateProductList(top);
                    }

                    @Override
                    public void onFailure(Call<APIResponse<ResProductDTO>> call, Throwable t) {
                        // Log hoặc hiển thị lỗi
                    }
                });
    }

    private void setUpProductFetching() {
        ProductFetching productFetching = APIClient.getClient().create(ProductFetching.class);
        productFetching.getAllProducts().enqueue(new Callback<APIResponse<ResProductDTO>>() {
            @Override
            public void onResponse(Call<APIResponse<ResProductDTO>> call, Response<APIResponse<ResProductDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResProductDTO resProductDTO = response.body().getData();
                    if (resProductDTO != null && resProductDTO.getData() != null) {
                        ResProductDTO.ProductDataManager.getInstance().setProductList(resProductDTO.getData());
                        Log.d(TAG, "Loaded product data: " + resProductDTO.getData().size() + " items.");
                    } else {
                        Log.w(TAG, "Product data is empty or null.");
                    }
                } else {
                    Log.e(TAG, "API response error. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<APIResponse<ResProductDTO>> call, Throwable t) {
                Log.e(TAG, "API call failed: " + t.getMessage(), t);
            }
        });
    }
}