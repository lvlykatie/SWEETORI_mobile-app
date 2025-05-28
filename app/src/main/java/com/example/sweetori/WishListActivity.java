package com.example.sweetori;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweetori.adapter.WishListAdapter;
import com.example.sweetori.content.WishlistFetching;
import com.example.sweetori.dto.response.PaginationWrapper;
import com.example.sweetori.dto.response.ResLoginDTO;
import com.example.sweetori.dto.response.ResWishListDTO;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishListActivity extends AppCompatActivity {
    ImageView btnAccount, btnHome, btnCart, btnNoti, btnVoucher;
    Button delete;
    RecyclerView wishlistRecyclerView;
    WishListAdapter wishListAdapter;
    private List<ResWishListDTO> wishlist;
    LinearLayout edit_List;
    TextView txtHello;
    private boolean isEditMode = false;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishlist);
        EdgeToEdge.enable(this);

        // Bind views
        btnAccount = findViewById(R.id.btnAccount);
        btnHome = findViewById(R.id.btnHome);
        btnCart = findViewById(R.id.btnCart);
        btnNoti = findViewById(R.id.btnNoti);
        btnVoucher = findViewById(R.id.btnVoucher);
        edit_List = findViewById(R.id.edit_List);
        txtHello = findViewById(R.id.txtHello);
        wishlistRecyclerView = findViewById(R.id.wishlistRecyclerView);

        // Display user name
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userJson = prefs.getString("user", null);

        if (userJson != null) {
            ResLoginDTO.UserLogin user = new Gson().fromJson(userJson, ResLoginDTO.UserLogin.class);
            txtHello.setText("Hello, " + user.getFirstName());
        } else {
            txtHello.setText("Guest");
        }

        // Navigation buttons
        btnAccount.setOnClickListener(v -> startActivity(new Intent(this, AccountActivity.class)));
        btnHome.setOnClickListener(v -> startActivity(new Intent(this, HomepageActivity.class)));
        btnCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        btnNoti.setOnClickListener(v -> startActivity(new Intent(this, NotiActivity.class)));
        btnVoucher.setOnClickListener(v -> startActivity(new Intent(this, VoucherActivity.class)));

        edit_List.setOnClickListener(v -> {
            isEditMode = !isEditMode;
            wishListAdapter.setEditMode(isEditMode);
        });


        fetchWishlistData();
    }

    private void fetchWishlistData() {
        Pair<String, Integer> accessTokenWithUserId = SharedPref.getAccessTokenWithUserId(this);
        String userId = "user:" + accessTokenWithUserId.second;

        WishlistFetching apiService = APIClient.getClientWithToken(accessTokenWithUserId.first).create(WishlistFetching.class);
        apiService.getwishlist(userId).enqueue(new Callback<APIResponse<PaginationWrapper<ResWishListDTO>>>() {
            @Override
            public void onResponse(Call<APIResponse<PaginationWrapper<ResWishListDTO>>> call, Response<APIResponse<PaginationWrapper<ResWishListDTO>>> response) {
                if (response.isSuccessful() && response.body() != null &&
                        response.body().getData() != null &&
                        response.body().getData().getData() != null) {

                    wishlist = response.body().getData().getData();

                    Log.d("WishListActivity", "Wishlist fetched successfully, total items: " + wishlist.size());

                    if (wishlist.isEmpty()) {
                        wishlistRecyclerView.setVisibility(View.GONE);
                        Toast.makeText(WishListActivity.this, "Your wishlist is empty.", Toast.LENGTH_SHORT).show();
                    } else {
                        wishlistRecyclerView.setVisibility(View.VISIBLE);
                        setupRecyclerView();
                    }

                } else {
                    Log.e("WishListActivity", "Failed to retrieve wishlist from API.");
                }
            }

            @Override
            public void onFailure(Call<APIResponse<PaginationWrapper<ResWishListDTO>>> call, Throwable t) {
                Log.e("WishListActivity", "API call failed: " + t.getMessage());
            }
        });
    }

    private void setupRecyclerView() {
        wishListAdapter = new WishListAdapter(
                wishlist,
                (item, position) -> {
                    // Open product detail
                    Intent intent = new Intent(WishListActivity.this, ProductDetailActivity.class);
                    intent.putExtra("product", item.getProduct());
                    startActivity(intent);
                },
                (item, position) -> {
                    // Delete wishlist item
                    deleteWishlistItem(item.getFavoriteId(), position);
                },
                WishListActivity.this
        );

        wishlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        wishlistRecyclerView.setAdapter(wishListAdapter);
    }

    private void deleteWishlistItem(int wishlistId, int position) {
        Pair<String, Integer> token = SharedPref.getAccessTokenWithUserId(this);
        WishlistFetching api = APIClient.getClientWithToken(token.first).create(WishlistFetching.class);

        api.deleteWishlistItem(wishlistId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    wishlist.remove(position);
                    wishListAdapter.notifyItemRemoved(position);
                    wishListAdapter.notifyItemRangeChanged(position, wishlist.size());

                    if (wishlist.isEmpty()) {
                        wishlistRecyclerView.setVisibility(View.GONE);
                        Toast.makeText(WishListActivity.this, "Your wishlist is empty.", Toast.LENGTH_SHORT).show();
                    }

                    Log.d("WishListActivity", "Item successfully removed from wishlist.");
                } else {
                    Log.e("WishListActivity", "Failed to remove item from wishlist.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("WishListActivity", "Error while deleting wishlist item: " + t.getMessage());
            }
        });
    }
}
