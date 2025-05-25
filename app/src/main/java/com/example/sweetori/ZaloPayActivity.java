package com.example.sweetori;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ZaloPayActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Nhận URL từ Intent
        String url = getIntent().getStringExtra("url");

        if (url != null) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }

        // Kết thúc Activity này sau khi mở trình duyệt
        finish();
    }
}
