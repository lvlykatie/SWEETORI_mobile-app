package com.example.sweetori;

import android.os.Bundle;
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
        TextView txtItemCount = findViewById(R.id.txtItemCount);

        // Lấy dữ liệu từ Intent
        String date = getIntent().getStringExtra("date");
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
        txtItemCount.setText("Item (" + (itemCount != null ? itemCount : "N/A") + ")");
    }
}