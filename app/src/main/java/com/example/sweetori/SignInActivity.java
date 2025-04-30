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

public class SignInActivity extends AppCompatActivity {
    Button btnSignIn;
    TextView btnForgot;
    TextView btnRegisterNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        EdgeToEdge.enable(this);

        //Ánh xạ component
        btnSignIn = findViewById(R.id.btnSignIn);
        btnForgot = findViewById(R.id.btnForgot);
        btnRegisterNow = findViewById(R.id.btnRegisterNow);

        //Intent
        btnSignIn.setOnClickListener(v -> {
            // Chuyển đến ShoppingActivity
            Intent homepage = new Intent(SignInActivity.this, HomepageActivity.class);
            startActivity(homepage);
        });
        btnForgot.setOnClickListener(v -> {
            // Chuyển đến ShoppingActivity
            Intent forgetEmail = new Intent(SignInActivity.this, ForgetEmailActivity.class);
            startActivity(forgetEmail);
        });
        btnRegisterNow.setOnClickListener(v -> {
            // Chuyển đến ShoppingActivity
            Intent register = new Intent(SignInActivity.this, RegisterActivity.class);
            startActivity(register);
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
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