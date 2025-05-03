package com.example.sweetori;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomepageActivity extends AppCompatActivity {
    ImageView btnAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String token = SharedPref.getAccessToken(this);
        if (token == null) {
            Intent loginIntent = new Intent(this, SignInActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
            finish(); // Không cho user quay lại Homepage
            return; // Dừng không chạy tiếp
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        EdgeToEdge.enable(this);

        //Ánh xạ component
        btnAccount = findViewById(R.id.btnAccount);

        //Intent
        btnAccount.setOnClickListener(v -> {
            Intent account = new Intent(HomepageActivity.this, AccountActivity.class);
            startActivity(account);
        });

    }
}