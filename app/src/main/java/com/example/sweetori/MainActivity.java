package com.example.sweetori;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sweetori.content.ProductFetching;
import com.example.sweetori.dto.response.ResProductDTO;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Button btnGoShopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.introduce);
        btnGoShopping = findViewById(R.id.btnGoShopping);
        String token = SharedPref.getAccessToken(MainActivity.this);
        btnGoShopping.setOnClickListener(v -> {
            if (token != null) {
                // Nếu token tồn tại, chuyển đến HomePageActivity
                Intent homeIntent = new Intent(MainActivity.this, HomepageActivity.class);
                startActivity(homeIntent);
                finish(); // Kết thúc MainActivity
            } else {
                // Nếu không có token, chuyển đến SignInActivity
                Intent signIn = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(signIn);
            }
        });
        ProductFetching productFetching = APIClient.getClient().create(ProductFetching.class);
        productFetching.getAllProducts().enqueue(new retrofit2.Callback<APIResponse<ResProductDTO>>() {
            @Override
            public void onResponse(Call<APIResponse<ResProductDTO>> call, Response<APIResponse<ResProductDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResProductDTO resProductDTO = response.body().getData();
                    if (resProductDTO != null && resProductDTO.getProductData() != null) {
                        ResProductDTO.ProductDataManager.getInstance().setProductList(resProductDTO.getProductData());
                        Log.d("MAIN_ACTIVITY", "Data loaded. Items: " + resProductDTO.getProductData().size());
                    } else {
                        Log.e("MAIN_ACTIVITY", "Product data is null");
                    }
                } else {
                    Log.e("MAIN_ACTIVITY", "API call failed. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<APIResponse<ResProductDTO>> call, Throwable t) {
                Log.e("MAIN_ACTIVITY", "API call failed: " + t.getMessage());
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.introduce), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo Mobile Ads SDK
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                // Callback khi khởi tạo xong (có thể để trống nếu chưa cần)
            }
        });
    }

}