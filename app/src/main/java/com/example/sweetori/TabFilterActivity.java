package com.example.sweetori;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TabFilterActivity extends AppCompatActivity {
    private ImageButton btnClose;
    private Button btnApply, btnClear;
    // Categories
    private Button btnFoundation, btnBlush, btnLipstick, btnMascara, btnPowder, btnConcealer, btnHighlighter, btnContour,
            btnEyePalette, btnEyebrows, btnEyeliner, btnEyelashes, btnLipPencil, btnFixingSpray, btnBrush, btnSponge ;
    // Brands
    private Button btnJudydoll, btnColorkey, btnGlamrrQ, btnIPKN, btnMaybelline, btnMerzy, btnBlackrouge, btnRomand, btnLemonade;
    // Rate & Price
    private RadioGroup rgRate, rgPrice;
    // Lưu tạm lựa chọn
    private ArrayList<String> selCategories = new ArrayList<>();
    private ArrayList<String> selBrands     = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabfilter);

        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> {
            finish(); // sẽ gọi overridePendingTransition trong ProductActivity.finish()
        });
        // phan filter
        btnApply       = findViewById(R.id.btnApplyFilter);
        btnClear       = findViewById(R.id.btnClearFilter);
        rgRate         = findViewById(R.id.rgRate);
        rgPrice        = findViewById(R.id.rgPrice);
// Ví dụ ánh xạ 2 category + 2 brand, bạn làm tương tự với các nút còn lại
        btnFoundation  = findViewById(R.id.btnFoundation);
        btnBlush       = findViewById(R.id.btnBlush);
        btnLipstick     = findViewById(R.id.btnLipstick);
        btnMascara      = findViewById(R.id.btnMascara);
        btnPowder       = findViewById(R.id.btnPowder);
        btnConcealer    = findViewById(R.id.btnConcealer);
        btnHighlighter  = findViewById(R.id.btnHighlighter);
        btnContour      = findViewById(R.id.btnContour);
        btnEyePalette   = findViewById(R.id.btnEyePalette);
        btnEyebrows     = findViewById(R.id.btnEyebrows);
        btnEyeliner     = findViewById(R.id.btnEyeliner);
        btnEyelashes    = findViewById(R.id.btnEyelashes);
        btnLipPencil    = findViewById(R.id.btnLipPencil);
        btnFixingSpray  = findViewById(R.id.btnFixingSpray);
        btnBrush        = findViewById(R.id.btnBrush);
        btnSponge       = findViewById(R.id.btnSponge);

        btnMaybelline   = findViewById(R.id.btnMaybelline);
        btnRomand       = findViewById(R.id.btnRomand);
        btnGlamrrQ      = findViewById(R.id.btnGlamrrQ);
        btnMerzy        = findViewById(R.id.btnMerzy);
        btnBlackrouge   = findViewById(R.id.btnBlackrouge);
        btnLemonade     = findViewById(R.id.btnLemonade);
        btnIPKN         = findViewById(R.id.btnIPKN);
        btnJudydoll    = findViewById(R.id.btnJudydoll);
        btnColorkey    = findViewById(R.id.btnColorkey);

        // 1. Toggle chọn category
        View.OnClickListener catToggle = v -> {
            Button b = (Button)v;
            String txt = b.getText().toString();
            if (selCategories.contains(txt)) {
                selCategories.remove(txt);
                b.setBackgroundTintList(getResources().getColorStateList(android.R.color.white));
                b.setTextColor(getResources().getColor(R.color.color01));
            } else {
                selCategories.add(txt);
                b.setBackgroundTintList(getResources().getColorStateList(R.color.color02)); // color02 ví dụ highlight
                b.setTextColor(getResources().getColor(android.R.color.white));
            }
        };
        btnFoundation.setOnClickListener(catToggle);
        btnBlush     .setOnClickListener(catToggle);
        btnLipstick    .setOnClickListener(catToggle);
        btnMascara     .setOnClickListener(catToggle);
        btnPowder      .setOnClickListener(catToggle);
        btnConcealer   .setOnClickListener(catToggle);
        btnHighlighter .setOnClickListener(catToggle);
        btnContour     .setOnClickListener(catToggle);
        btnEyePalette  .setOnClickListener(catToggle);
        btnEyebrows    .setOnClickListener(catToggle);
        btnEyeliner    .setOnClickListener(catToggle);
        btnEyelashes   .setOnClickListener(catToggle);
        btnLipPencil   .setOnClickListener(catToggle);
        btnFixingSpray .setOnClickListener(catToggle);
        btnBrush       .setOnClickListener(catToggle);
        btnSponge      .setOnClickListener(catToggle);

        // 2. Toggle chọn brand
        View.OnClickListener brandToggle = v -> {
            Button b = (Button)v;
            String txt = b.getText().toString();
            if (selBrands.contains(txt)) {
                selBrands.remove(txt);
                b.setBackgroundTintList(getResources().getColorStateList(android.R.color.white));
                b.setTextColor(getResources().getColor(R.color.color01));
            } else {
                selBrands.add(txt);
                b.setBackgroundTintList(getResources().getColorStateList(R.color.color02));
                b.setTextColor(getResources().getColor(android.R.color.white));
            }
        };
        btnJudydoll.setOnClickListener(brandToggle);
        btnColorkey.setOnClickListener(brandToggle);
        btnMaybelline.setOnClickListener(brandToggle);
        btnRomand.setOnClickListener(brandToggle);
        btnGlamrrQ.setOnClickListener(brandToggle);
        btnMerzy.setOnClickListener(brandToggle);
        btnBlackrouge.setOnClickListener(brandToggle);
        btnLemonade.setOnClickListener(brandToggle);
        btnIPKN.setOnClickListener(brandToggle);

        // Clear all
        btnClear.setOnClickListener(v -> {
            // reset danh sách
            selCategories.clear();
            selBrands.clear();
            rgRate.clearCheck();
            rgPrice.clearCheck();
            // reset UI: bạn có thể gọi lại catToggle/brandToggle cho từng nút để set background về white
            recreate(); // đơn giản reload layout
        });

        // Apply filter: trả về cho ProductActivity
        btnApply.setOnClickListener(v -> {
            Intent data = new Intent();
            data.putStringArrayListExtra("FILTER_CATEGORIES", selCategories);
            data.putStringArrayListExtra("FILTER_BRANDS", selBrands);

            int rateId = rgRate.getCheckedRadioButtonId();
            int rate = 0;
            if (rateId == R.id.rb5star) rate = 5;
            else if (rateId == R.id.rb4star) rate = 4;
            else if (rateId == R.id.rb3star) rate = 3;
            else if (rateId == R.id.rb2star) rate = 2;
            else if (rateId == R.id.rb1star) rate = 1;
            data.putExtra("FILTER_RATE", rate);

            int priceId = rgPrice.getCheckedRadioButtonId();
            String priceOrder = priceId == R.id.rbLowToHigh
                    ? "LOW_HIGH" : "HIGH_LOW";
            data.putExtra("FILTER_PRICE", priceOrder);

            // Trả về dữ liệu
            setResult(RESULT_OK, data);
            finish();
        });

    }

    @Override
    public void finish() {
        super.finish();
        // Như trên, animation sẽ thực thi ở ProductActivity.finish()
    }
}
