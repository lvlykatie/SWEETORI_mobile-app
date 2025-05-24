package com.example.sweetori;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sweetori.content.MomoFetching;
import com.example.sweetori.content.PaymentFetching;
import com.example.sweetori.APIResponse;
import com.example.sweetori.dto.request.ReqMomoDTO;
import com.example.sweetori.dto.request.ReqPaymentDTO;
import com.example.sweetori.dto.response.ResMomoDTO;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {

    private TextView item, dateTextView, shipping, discount, voucher_discount, total;
    RadioButton radioCOD, radioMomo, radioZaloPay, radioVNPAY;
    Button orderButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payments);

        item = findViewById(R.id.item);
        shipping = findViewById(R.id.shipping);
        discount = findViewById(R.id.discount);
        voucher_discount = findViewById(R.id.voucher);
        total = findViewById(R.id.total);
        dateTextView = findViewById(R.id.date);
        radioCOD = findViewById(R.id.radio_cod);
        radioMomo = findViewById(R.id.radio_momo);
        radioZaloPay = findViewById(R.id.radio_zalopay);
        radioVNPAY = findViewById(R.id.radio_vnpay);
        orderButton = findViewById(R.id.orderButton);
        Pair<String, Integer> accessTokenWithUserId = SharedPref.getAccessTokenWithUserId(PaymentActivity.this);

        View.OnClickListener radioClickListener = v -> {
            radioCOD.setChecked(false);
            radioMomo.setChecked(false);
            radioZaloPay.setChecked(false);
            radioVNPAY.setChecked(false);

            ((RadioButton) v).setChecked(true);
        };

        radioCOD.setOnClickListener(radioClickListener);
        radioMomo.setOnClickListener(radioClickListener);
        radioZaloPay.setOnClickListener(radioClickListener);
        radioVNPAY.setOnClickListener(radioClickListener);


        orderButton.setOnClickListener(v -> {
            String paymentMethod = "";
            Long totalPriceDouble = 0L;
            try {
                // Xử lý chuỗi tiền: loại bỏ VND, dấu cách, dấu chấm
                String cleanedTotal = total.getText().toString()
                        .replace("VND", "")
                        .replace(",", "")
                        .replace(".", "")
                        .replaceAll("\\s+", "");  // loại bỏ khoảng trắng thừa nếu có

                totalPriceDouble = Long.parseLong(cleanedTotal);
                Log.d("PaymentActivity", "Total price parsed: " + totalPriceDouble);
            } catch (NumberFormatException e) {
                Toast.makeText(PaymentActivity.this, "Invalid total price value", Toast.LENGTH_SHORT).show();
                Log.e("PaymentActivity", "Failed to parse total price: " + e.getMessage());
                return;
            }

            OffsetDateTime now = OffsetDateTime.now();
            String isoTime = now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME); // chuẩn ISO8601

            if (radioCOD.isChecked()) {
                paymentMethod = "COD";
                ReqPaymentDTO request_product = new ReqPaymentDTO(
                        paymentMethod,
                        paymentMethod,
                        totalPriceDouble,
                        isoTime
                );
                PaymentFetching api = APIClient.getClientWithToken(accessTokenWithUserId.first).create(PaymentFetching.class);
                Call<Void> call = api.addpayments(request_product);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(PaymentActivity.this, "Payment successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PaymentActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(PaymentActivity.this, "Connection error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (radioMomo.isChecked()) {
                paymentMethod = "MoMo";

                MomoFetching momoapi = APIClient.getClientWithToken(accessTokenWithUserId.first).create(MomoFetching.class);
                ReqMomoDTO order = new ReqMomoDTO("Payment for order ABC", totalPriceDouble);

                Call<APIResponse<ResMomoDTO>> call = momoapi.momopayment(order);
                call.enqueue(new Callback<APIResponse<ResMomoDTO>>() {
                    @Override
                    public void onResponse(Call<APIResponse<ResMomoDTO>> call, Response<APIResponse<ResMomoDTO>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String payUrl = response.body().getData().getPaymentUrl();
                            Log.d("MoMo", "Payment URL: " + payUrl);
                            Intent intent = new Intent(PaymentActivity.this, MomoActivity.class);
                            intent.putExtra("url", payUrl);
                            startActivity(intent);
                        } else {
                            Toast.makeText(PaymentActivity.this, "Failed to get payment URL", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponse<ResMomoDTO>> call, Throwable t) {
                        Log.e("MoMo", "Network error", t);
                        Toast.makeText(PaymentActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            } else if (radioZaloPay.isChecked()) {
                paymentMethod = "ZaloPay";
            } else if (radioVNPAY.isChecked()) {
                paymentMethod = "VNPAY";
            }

            if (paymentMethod.isEmpty()) {
                Toast.makeText(PaymentActivity.this, "Please select a payment method", Toast.LENGTH_SHORT).show();
                return;
            }
        });


        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String itemStr = getIntent().getStringExtra("item");
        String discountStr = getIntent().getStringExtra("discount");
        String voucherStr = getIntent().getStringExtra("voucher");
        String shippingStr = getIntent().getStringExtra("shipping");
        String total_Price = getIntent().getStringExtra("total_Price");

        // Set text
        dateTextView.setText(currentDate);
        item.setText(itemStr);
        discount.setText(discountStr);
        voucher_discount.setText(voucherStr);
        shipping.setText(shippingStr);
        total.setText(total_Price);

    }
}
