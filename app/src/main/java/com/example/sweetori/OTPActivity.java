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

public class OTPActivity extends AppCompatActivity {
    Button btnConfirm;
    TextView btnResend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpass_otp);
        EdgeToEdge.enable(this);

        //Ánh xạ component
        btnConfirm = findViewById(R.id.btnConfirm);
        btnResend = findViewById(R.id.btnResend);

        //Intent
        btnConfirm.setOnClickListener(v -> {
            // Chuyển đến ShoppingActivity
            Intent createPass = new Intent(OTPActivity.this, CreatePassActivity.class);
            startActivity(createPass);
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.forgetpass_otp), (v, insets) -> {
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