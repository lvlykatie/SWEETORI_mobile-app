package com.example.sweetori;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sweetori.content.ProductFetching;
import com.example.sweetori.dto.response.ResProductDTO;
import com.google.android.gms.ads.MobileAds;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button btnGoShopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduce);

        btnGoShopping = findViewById(R.id.btnGoShopping);
        Pair<String, Integer> accessTokenWithUserId = SharedPref.getAccessTokenWithUserId(MainActivity.this);


        btnGoShopping.setOnClickListener(v -> {
            Intent intent = (accessTokenWithUserId.first != null)
                    ? new Intent(MainActivity.this, HomepageActivity.class)
                    : new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
            if (accessTokenWithUserId.first != null) finish(); // Only finish MainActivity if logged in
        });

        setUpProductFetching();
        initializeMobileAds();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.introduce), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
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

    private void initializeMobileAds() {
        MobileAds.initialize(this, initializationStatus -> {
            // Optional: Handle Mobile Ads initialization callback
        });
    }
}
