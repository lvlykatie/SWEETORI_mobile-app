package com.example.sweetori;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class RegisterActivity extends AppCompatActivity {
    Button btnSignUp;
    TextView btnSignInNow;
    TextView btnPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signup);

        //Ánh xạ component
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignInNow = findViewById(R.id.btnSignInNow);
        btnPolicy = findViewById(R.id.btnPolicy);

        //Intent
        btnSignUp.setOnClickListener(v -> {
            // Chuyển đến ShoppingActivity
            Intent signIn = new Intent(RegisterActivity.this, SignInActivity.class);
            startActivity(signIn);
        });
        btnSignInNow.setOnClickListener(v -> {
            // Chuyển đến ShoppingActivity
            Intent signIn = new Intent(RegisterActivity.this, SignInActivity.class);
            startActivity(signIn);
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signup), (v, insets) -> {
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