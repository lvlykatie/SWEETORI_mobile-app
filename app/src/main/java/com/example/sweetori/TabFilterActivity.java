package com.example.sweetori;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class TabFilterActivity extends AppCompatActivity {
    private ImageButton btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabfilter);

        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> {
            finish(); // sẽ gọi overridePendingTransition trong ProductActivity.finish()
        });
    }

    @Override
    public void finish() {
        super.finish();
        // Như trên, animation sẽ thực thi ở ProductActivity.finish()
    }
}
