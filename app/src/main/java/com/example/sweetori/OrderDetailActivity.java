package com.example.sweetori;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweetori.adapter.OrderItemAdapter;
import com.example.sweetori.content.FeeAndDiscountFetching;
import com.example.sweetori.dto.response.ResFeeAndDiscountDTO;
import com.example.sweetori.dto.response.ResOrderDTO;
import com.example.sweetori.dto.response.ResOrderDetailDTO;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.recyclerview.widget.LinearLayoutManager;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView tvOrderStatus, tvDate, tvShippingName, tvPhoneNumber, tvShippingAddress, tvItemCount, tvItemPrice, tvShippingCost, tvProductDiscount, tvVoucherDiscount, tvPaymentMethod, tvTotalPrice;
    private LinearLayout layoutButtons;
    private Button btnCancelOrder, btnNotReceived, btnReceived, btnRepurchase, btnReview;

    private ResOrderDTO order;
    private RecyclerView rvOrderItems;
    private OrderItemAdapter orderItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.order_detail);

        initViews();

        order = (ResOrderDTO) getIntent().getSerializableExtra("order");
        Log.d("OrderDetailActivity", "order = " + order); // Kiểm tra xem có null không

        orderItemAdapter = new OrderItemAdapter(order.getListOfOrderdetails(), this);
        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        rvOrderItems.setAdapter(orderItemAdapter);

        tvOrderStatus.setText(order.getStatus());
        tvDate.setText(formatDate(order.getDate()));
        tvShippingName.setText(order.getDelivery().getName());
        tvPhoneNumber.setText(order.getUser().getPhoneNumber());
        tvShippingAddress.setText(order.getUser().getShippingAddress());

        tvItemCount.setText("Item (" + order.getListOfOrderdetails().size() + ")");
        tvItemPrice.setText(String.format("%,.0f VND", calculateTotalSellingPrice(order.getListOfOrderdetails())));
        tvShippingCost.setText(String.format("%,.0f VND", order.getDelivery().getShippingCost()));

        fetchFeeAndDiscount(order.getOrderId(), this);

        tvPaymentMethod.setText(order.getPayment().getName());
        tvTotalPrice.setText(String.format("%,.0f VND", order.getTotal()));
        String status = order.getStatus();
        tvOrderStatus.setText(status);
        showButtonsByStatus(status);
    }

    private void fetchFeeAndDiscount(int orderId, Context context) {
        String accessToken = SharedPref.getAccessTokenWithUserId(context).first;
        if (accessToken == null) {
            Log.e("fetchFeeAndDiscount", "Access token is null");
            return;
        }

        FeeAndDiscountFetching service = APIClient.getClientWithToken(accessToken).create(FeeAndDiscountFetching.class);

        Call<APIResponse<List<ResFeeAndDiscountDTO>>> call = service.getFee(orderId);

        call.enqueue(new Callback<APIResponse<List<ResFeeAndDiscountDTO>>>() {
            @Override
            public void onResponse(Call<APIResponse<List<ResFeeAndDiscountDTO>>> call, Response<APIResponse<List<ResFeeAndDiscountDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    double productDiscount = 0;
                    double voucherDiscount = 0;

                    for (ResFeeAndDiscountDTO fee : response.body().getData()) {
                        String desc = fee.getDescription() != null ? fee.getDescription().toLowerCase() : "";
                        if (desc.contains("voucher")) {
                            voucherDiscount += fee.getAmount();
                        } else if (desc.contains("sản phẩm")) {
                            productDiscount += fee.getAmount();
                        }
                    }

                    tvProductDiscount.setText(String.format("-%,.0f VND", productDiscount));
                    tvVoucherDiscount.setText(String.format("-%,.0f VND", voucherDiscount));
                }
            }

            @Override
            public void onFailure(Call<APIResponse<List<ResFeeAndDiscountDTO>>> call, Throwable t) {
                Log.e("fetchFeeAndDiscount", "API call failed: " + t.getMessage());
            }
        });
    }



    private double calculateTotalSellingPrice(List<ResOrderDetailDTO> orderDetails) {
        double total = 0;
        for (ResOrderDetailDTO detail : orderDetails) {
            total += detail.getProduct().getSellingPrice() * detail.getQuantity();
        }
        return total;
    }

    private void initViews() {
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvDate = findViewById(R.id.tvDate);
        tvShippingName = findViewById(R.id.tvShippingName);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvShippingAddress = findViewById(R.id.tvShippingAddress);
        tvItemCount = findViewById(R.id.tvItemCount);
        tvItemPrice = findViewById(R.id.tvItemPrice);
        tvShippingCost = findViewById(R.id.tvShippingCost);
        tvProductDiscount = findViewById(R.id.tvProductDiscount);
        tvVoucherDiscount = findViewById(R.id.tvVoucherDiscount);
        tvPaymentMethod = findViewById(R.id.tvPaymentName);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        rvOrderItems = findViewById(R.id.rvOrderItems);
        layoutButtons = findViewById(R.id.layoutButtons);
        btnCancelOrder = findViewById(R.id.btnCancelOrder);
        btnNotReceived = findViewById(R.id.btnNotReceived);
        btnReceived = findViewById(R.id.btnReceived);
        btnRepurchase = findViewById(R.id.btnRepurchase);
        btnReview = findViewById(R.id.btnReview);
    }

    private void showButtonsByStatus(String status) {
        btnCancelOrder.setVisibility(View.GONE);
        btnNotReceived.setVisibility(View.GONE);
        btnReceived.setVisibility(View.GONE);
        btnRepurchase.setVisibility(View.GONE);
        btnReview.setVisibility(View.GONE);

        switch (status) {
            case "PENDING":
                btnCancelOrder.setVisibility(View.VISIBLE);
                break;
            case "WAITING":
                btnCancelOrder.setVisibility(View.VISIBLE);
                break;
            case "TRANSPORT":
                btnNotReceived.setVisibility(View.VISIBLE);
                btnReceived.setVisibility(View.VISIBLE);
                break;
            case "COMPLETED":
                btnRepurchase.setVisibility(View.VISIBLE);
                btnReview.setVisibility(View.VISIBLE);
                break;
            case "CANCELLED":
                btnRepurchase.setVisibility(View.VISIBLE);
                break;
        }
    }

    private String formatDate(String rawDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return outputFormat.format(inputFormat.parse(rawDate));
        } catch (Exception e) {
            return rawDate;
        }
    }
}
