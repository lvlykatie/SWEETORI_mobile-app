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

public class ForgetEmailActivity extends AppCompatActivity {
    Button btnSendOTP;
    TextView btnSignInNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpass_email);
        EdgeToEdge.enable(this);

        //Ánh xạ component
        btnSendOTP = findViewById(R.id.btnSendOTP);
        btnSignInNow = findViewById(R.id.btnSignInNow);

        //Intent
        btnSendOTP.setOnClickListener(v -> {
            // Chuyển đến ShoppingActivity
            Intent OTP = new Intent(ForgetEmailActivity.this, OTPActivity.class);
            startActivity(OTP);
        });
        btnSignInNow.setOnClickListener(v -> {
            // Chuyển đến ShoppingActivity
            Intent signIn = new Intent(ForgetEmailActivity.this, SignInActivity.class);
            startActivity(signIn);
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.forgetpass_email), (v, insets) -> {
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