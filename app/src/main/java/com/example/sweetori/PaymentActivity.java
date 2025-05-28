package com.example.sweetori;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import com.example.sweetori.content.CartFetching;
import com.example.sweetori.content.MomoFetching;
import com.example.sweetori.content.PaymentFetching;
import com.example.sweetori.APIResponse;
import com.example.sweetori.dto.request.ReqCheckoutDTO;
import com.example.sweetori.dto.request.ReqMomoDTO;
import com.example.sweetori.dto.request.ReqPaymentDTO;
import com.example.sweetori.dto.request.ReqVNpayDTO;
import com.example.sweetori.dto.request.ReqZalopayDTO;
import com.example.sweetori.dto.response.ResCartDetailDTO;
import com.example.sweetori.dto.response.ResMomoDTO;
import com.example.sweetori.dto.response.ResPaymentDTO;
import com.example.sweetori.dto.response.ResVNpayDTO;
import com.example.sweetori.dto.response.ResZalopayDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {

    private TextView item, dateTextView, shipping, discount, voucher_discount, total, itemCount;
    RadioButton radioCOD, radioMomo, radioZaloPay, radioVNPAY;
    Button orderButton;
    private ActivityResultLauncher<Intent> customTabLauncher;
    private static final int PAYMENT_SUCCESS_REQUEST = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payments);

        // Khởi tạo view
        item = findViewById(R.id.item);
        itemCount = findViewById(R.id.itemCount);
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

        // Lấy thông tin từ SharedPref
        Pair<String, Integer> accessTokenWithUserId = SharedPref.getAccessTokenWithUserId(PaymentActivity.this);

        // Hiển thị thông tin đơn hàng
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String itemStr = getIntent().getStringExtra("item");
        String itemCountStr = getIntent().getStringExtra("itemCount");
        String discountStr = getIntent().getStringExtra("discount");
        String voucherStr = getIntent().getStringExtra("voucher");
        String shippingStr = getIntent().getStringExtra("shipping");
        String total_Price = getIntent().getStringExtra("total_Price");
        String voucherCode = getIntent().getStringExtra("voucher_code");
        int voucher_id = getIntent().getIntExtra("voucherId", -1);
        int deliveryId = getIntent().getIntExtra("selectedDeliveryId", -1);
        String productListJson = getIntent().getStringExtra("productListJson");
        List<ResCartDetailDTO> productList = new Gson().fromJson(productListJson, new TypeToken<List<ResCartDetailDTO>>(){}.getType());
        String selectedItemsJson = getIntent().getStringExtra("selectedItems");
        Type listType = new TypeToken<List<ResCartDetailDTO>>() {}.getType();
        List<ResCartDetailDTO> bestVoucherList = new Gson().fromJson(selectedItemsJson, listType);

        dateTextView.setText(currentDate);
        item.setText(itemStr);
        discount.setText(discountStr);
        voucher_discount.setText(voucherStr);
        shipping.setText(shippingStr);
        total.setText(total_Price);
        itemCount.setText("Item (" + itemCountStr + ")");

        // Xử lý radio button
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

        // Khởi tạo ActivityResultLauncher cho VNPAY
        customTabLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        navigateToDonePayment();
                    }
                }
        );

        // Xử lý nút Order
        orderButton.setOnClickListener(v -> {
            String paymentMethod = "";
            Long totalPrice = 0L;

            try {
                String cleanedTotal = total.getText().toString()
                        .replace("VND", "")
                        .replace(",", "")
                        .replace(".", "")
                        .replaceAll("\\s+", "");

                totalPrice = Long.parseLong(cleanedTotal);
                Log.d("PaymentActivity", "Parsed total price: " + totalPrice);
            } catch (NumberFormatException e) {
                Toast.makeText(PaymentActivity.this, "Invalid total price", Toast.LENGTH_SHORT).show();
                Log.e("PaymentActivity", "Failed to parse total price: " + e.getMessage());
                return;
            }


            OffsetDateTime now = OffsetDateTime.now();
            String isoTime = now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

            if (radioCOD.isChecked()) {
                paymentMethod = "COD";
                int paymentId = 1;

                List<ReqCheckoutDTO.ProductWithQuantity> productWithQuantityList = new ArrayList<>();
                List<Integer> setVoucherIds = new ArrayList<>();

                if (voucher_id != -1) {
                    setVoucherIds.add(voucher_id);
                }

                for (ResCartDetailDTO item : productList) {
                    productWithQuantityList.add(
                            new ReqCheckoutDTO.ProductWithQuantity(item.getProduct().getProductId(), item.getQuantity())
                    );
                }

                ReqCheckoutDTO request = new ReqCheckoutDTO();
                request.setUserId(accessTokenWithUserId.second);
                request.setProductWithQuantityList(productWithQuantityList);
                request.setDeliveryId(deliveryId);
                request.setPaymentId(paymentId);
                request.setVoucherIds(setVoucherIds);

                CartFetching apiService = APIClient.getClientWithToken(accessTokenWithUserId.first).create(CartFetching.class);
                Call<Void> call = apiService.paymentCash(request);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(PaymentActivity.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                            navigateToDonePayment();
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
                ReqMomoDTO order = new ReqMomoDTO("Order payment", totalPrice);

                Call<APIResponse<ResMomoDTO>> call = momoapi.momopayment(order);
                call.enqueue(new Callback<APIResponse<ResMomoDTO>>() {
                    @Override
                    public void onResponse(Call<APIResponse<ResMomoDTO>> call, Response<APIResponse<ResMomoDTO>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String payUrl = response.body().getData().getPaymentUrl();
                            Intent intent = new Intent(PaymentActivity.this, MomoActivity.class);
                            intent.putExtra("url", payUrl);
                            startActivityForResult(intent, PAYMENT_SUCCESS_REQUEST);
                        } else {
                            Toast.makeText(PaymentActivity.this, "Failed to retrieve MoMo payment URL", Toast.LENGTH_SHORT).show();
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

                PaymentFetching zalopayApi = APIClient.getClientWithToken(accessTokenWithUserId.first).create(PaymentFetching.class);
                ReqZalopayDTO zaloOrder = new ReqZalopayDTO("Order payment", totalPrice);

                Call<APIResponse<ResZalopayDTO>> zalopayCall = zalopayApi.zalopayment(zaloOrder);
                zalopayCall.enqueue(new Callback<APIResponse<ResZalopayDTO>>() {
                    @Override
                    public void onResponse(Call<APIResponse<ResZalopayDTO>> call, Response<APIResponse<ResZalopayDTO>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String paymentUrl = response.body().getData().getPaymentUrl();
                            Intent intent = new Intent(PaymentActivity.this, ZaloPayActivity.class);
                            intent.putExtra("url", paymentUrl);
                            startActivityForResult(intent, PAYMENT_SUCCESS_REQUEST);
                        } else {
                            Toast.makeText(PaymentActivity.this, "ZaloPay connection error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponse<ResZalopayDTO>> call, Throwable t) {
                        Toast.makeText(PaymentActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (radioVNPAY.isChecked()) {
                paymentMethod = "VNPAY";

                PaymentFetching vnpayApi = APIClient.getClientWithToken(accessTokenWithUserId.first)
                        .create(PaymentFetching.class);
                ReqVNpayDTO vnOrder = new ReqVNpayDTO("Order payment", totalPrice);
                Call<APIResponse<ResVNpayDTO>> call = vnpayApi.vnpaypayment(vnOrder);
                call.enqueue(new Callback<APIResponse<ResVNpayDTO>>() {
                    @Override
                    public void onResponse(Call<APIResponse<ResVNpayDTO>> call, Response<APIResponse<ResVNpayDTO>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String paymentUrl = response.body().getData().getPaymentUrl();
                            Intent intent = new Intent(PaymentActivity.this, VNPayActivity.class);
                            intent.putExtra("url", paymentUrl);
                            startActivityForResult(intent, PAYMENT_SUCCESS_REQUEST);
                        } else {
                            Toast.makeText(PaymentActivity.this,
                                    "VNPay connection error: " + response.code(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponse<ResVNpayDTO>> call, Throwable t) {
                        Toast.makeText(PaymentActivity.this,
                                "VNPay network error: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            if (paymentMethod.isEmpty()) {
                Toast.makeText(PaymentActivity.this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYMENT_SUCCESS_REQUEST && resultCode == RESULT_OK) {
            navigateToDonePayment();
        }
    }

    private void navigateToDonePayment() {
        Intent intent = new Intent(PaymentActivity.this, DonePaymentActivity.class);

        // Truyền tất cả dữ liệu cần hiển thị
        intent.putExtra("date", dateTextView.getText().toString());
        intent.putExtra("shippingMethod", shipping.getText().toString());
        intent.putExtra("discount", discount.getText().toString());
        intent.putExtra("voucher", voucher_discount.getText().toString());
        intent.putExtra("totalPrice", total.getText().toString());
        intent.putExtra("paymentMethod", getPaymentMethodName()); // Thêm phương thức thanh toán
        intent.putExtra("item", item.getText().toString());
        intent.putExtra("itemCount", itemCount.getText().toString());

        startActivity(intent);
        finish();
    }

    // Hàm lấy tên phương thức thanh toán
    private String getPaymentMethodName() {
        if (radioCOD.isChecked()) return "COD";
        if (radioMomo.isChecked()) return "MoMo";
        if (radioZaloPay.isChecked()) return "ZaloPay";
        if (radioVNPAY.isChecked()) return "VNPAY";
        return "Unknown";
    }

    public interface PaymentCallback {
        void onSuccess(int paymentId);
        void onError(String errorMessage);
    }
}