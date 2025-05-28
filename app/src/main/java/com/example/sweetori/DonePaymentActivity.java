package com.example.sweetori;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DonePaymentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.done_payments);

        // Ánh xạ view
        TextView txtDateShipping = findViewById(R.id.txtDateShipping);
        TextView txtShipping = findViewById(R.id.txtShipping);
        TextView txtVoucher = findViewById(R.id.txtVoucher);
        TextView txtPaymentMethod = findViewById(R.id.txtPaymentMethod);
        TextView txtxTotal = findViewById(R.id.txtxTotal);
        TextView txtItems = findViewById(R.id.txtItem);
        TextView txtProductDiscount = findViewById(R.id.txtProductDiscount);
        TextView txtItemCount = findViewById(R.id.txtItemCount);
        Button btnDone = findViewById(R.id.btnDone);

        // Lấy dữ liệu từ Intent
        String date = getIntent().getStringExtra("date");
        String discount = getIntent().getStringExtra("discount");
        String shippingMethod = getIntent().getStringExtra("shippingMethod");
        String voucher = getIntent().getStringExtra("voucher");
        String paymentMethod = getIntent().getStringExtra("paymentMethod");
        String totalPrice = getIntent().getStringExtra("totalPrice");
        String item = getIntent().getStringExtra("item");
        String itemCount = getIntent().getStringExtra("itemCount");


        // Hiển thị dữ liệu
        txtDateShipping.setText(date != null ? date : "N/A");
        txtShipping.setText(shippingMethod != null ? shippingMethod : "N/A");
        txtVoucher.setText(voucher != null ? voucher : "N/A");
        txtPaymentMethod.setText(paymentMethod != null ? paymentMethod : "N/A");
        txtxTotal.setText(totalPrice != null ? totalPrice : "N/A");
        txtItems.setText(item != null ? item : "N/A");
        txtProductDiscount.setText(discount != null ? discount : "N/A");
        txtItemCount.setText(itemCount != null ? itemCount : "0");

        btnDone.setOnClickListener(v -> {
            Intent intent = new Intent(DonePaymentActivity.this, HomepageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // đóng Activity hiện tại
        });

    }
}