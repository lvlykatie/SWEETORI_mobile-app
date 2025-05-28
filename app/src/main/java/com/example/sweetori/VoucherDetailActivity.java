package com.example.sweetori;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sweetori.dto.response.ResVoucherDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class VoucherDetailActivity extends AppCompatActivity {

    ImageView btnAccount;
    ImageView btnHome;
    ImageView btnCart;
    ImageView btnNoti;
    ImageView btnVoucher;
    TextView tvVoucherCode, tvDiscountAmount, tvVoucherDuration;
    Button btnCopy;
    private ResVoucherDTO.VoucherData voucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voucher_detail);
        EdgeToEdge.enable(this);

        // Component
        btnAccount = findViewById(R.id.btnAccount);
        btnHome = findViewById(R.id.btnHome);
        btnCart = findViewById(R.id.btnCart);
        btnNoti = findViewById(R.id.btnNoti);
        btnVoucher = findViewById(R.id.btnVoucher1);

        tvVoucherCode = findViewById(R.id.tvVoucherCode);
        tvDiscountAmount = findViewById(R.id.tvDiscountAmount);
        tvVoucherDuration = findViewById(R.id.tvVoucherDuration);
        btnCopy = findViewById(R.id.btnCopy);

        voucher = (ResVoucherDTO.VoucherData) getIntent().getSerializableExtra("voucher");

        if (voucher != null) {
            tvVoucherCode.setText(voucher.getCode());
            tvDiscountAmount.setText(String.format(Locale.getDefault(), "Sale: %dđ", voucher.getDiscountAmount().intValue()));

            // Format thời gian validFrom - validTo
            try {
                DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm", Locale.ENGLISH);

                LocalDateTime from = LocalDateTime.parse(voucher.getValidFrom(), inputFormat);
                LocalDateTime to = LocalDateTime.parse(voucher.getValidTo(), inputFormat);

                String duration = String.format("%s – %s", from.format(outputFormat), to.format(outputFormat));
                tvVoucherDuration.setText(duration);
            } catch (Exception e) {
                tvVoucherDuration.setText("Invalid date");
                e.printStackTrace();
            }

            btnCopy.setOnClickListener(v -> {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                        getSystemService(CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Voucher Code", voucher.getCode());
                clipboard.setPrimaryClip(clip);

                // Thông báo
                android.widget.Toast.makeText(this, "Code copied: " + voucher.getCode(), android.widget.Toast.LENGTH_SHORT).show();
            });
        }

        // Intent setup (phải nằm trong onCreate)
        btnAccount.setOnClickListener(v -> startActivity(new Intent(this, AccountActivity.class)));
        btnHome.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        btnCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        btnNoti.setOnClickListener(v -> startActivity(new Intent(this, NotiActivity.class)));
        btnVoucher.setOnClickListener(v -> startActivity(new Intent(this, VoucherActivity.class)));
    }
}
