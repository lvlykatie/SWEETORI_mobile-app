package com.example.sweetori;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

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