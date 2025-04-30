package com.example.sweetori;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AccountActivity extends AppCompatActivity {
    LinearLayout btnLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);
        EdgeToEdge.enable(this);

        //Ánh xạ component
        btnLogOut = findViewById(R.id.btnLogOut);

        //Lắng nghe sự kiện click
        btnLogOut.setOnClickListener(v -> {
            // Chuyển đến ShoppingActivity
            Intent logout = new Intent(AccountActivity.this, SignInActivity.class);
            startActivity(logout);
        });
    }
}