package com.example.sweetori;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweetori.adapter.OrderItemAdapter;
import com.example.sweetori.content.CartFetching;
import com.example.sweetori.content.FeeAndDiscountFetching;
import com.example.sweetori.content.OrderFetching;
import com.example.sweetori.APIResponse;
import com.example.sweetori.content.ProductFetching;
import com.example.sweetori.dto.request.ReqAddToCartDTO;
import com.example.sweetori.dto.response.PaginationWrapper;
import com.example.sweetori.dto.response.ResCartDTO;
import com.example.sweetori.dto.response.ResCartDetailDTO;
import com.example.sweetori.dto.response.ResFeeAndDiscountDTO;
import com.example.sweetori.dto.response.ResOrderDTO;
import com.example.sweetori.dto.response.ResOrderDetailDTO;
import com.example.sweetori.dto.response.ResProductDTO;
import com.example.sweetori.dto.response.ResUserDTO;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.recyclerview.widget.LinearLayoutManager;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView tvOrderStatus, tvDate, tvShippingName, tvPhoneNumber, tvShippingAddress,
            tvItemCount, tvItemPrice, tvShippingCost, tvProductDiscount,
            tvVoucherDiscount, tvPaymentMethod, tvTotalPrice;
    private LinearLayout layoutButtons;
    private Button btnCancelOrder, btnReceived, btnNotReceived, btnRepurchase, btnReview;
    private ResOrderDTO order;
    private ResUserDTO currentUser;
    private RecyclerView rvOrderItems;
    private OrderItemAdapter orderItemAdapter;
    private List<ResCartDetailDTO> cartDetails;
    private int cartId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.order_detail);

        currentUser = SharedPref.getUser(this);
        cartDetails = new ArrayList<>();
        initViews();
        loadOrderData();
        setupOrderItems();
        setupButtonsByStatus();
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
        btnReceived = findViewById(R.id.btnReceived);
        btnNotReceived = findViewById(R.id.btnNotReceived);
        btnRepurchase = findViewById(R.id.btnRepurchase);
        btnReview = findViewById(R.id.btnReview);

        // Ẩn các nút không sử dụng
        btnNotReceived.setVisibility(View.GONE);
        btnRepurchase.setVisibility(View.GONE);
        btnReview.setVisibility(View.GONE);
    }

    private void loadOrderData() {
        order = (ResOrderDTO) getIntent().getSerializableExtra("order");
        if (order == null) {
            Toast.makeText(this, "Không thể tải thông tin đơn hàng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvOrderStatus.setText(order.getStatus());
        tvDate.setText(formatDate(order.getDate()));
        tvShippingName.setText(order.getDelivery().getName());
        tvPhoneNumber.setText(order.getUser().getPhoneNumber());
        tvShippingAddress.setText(order.getUser().getShippingAddress());

        tvItemCount.setText("Item (" + order.getListOfOrderdetails().size() + ")");
        tvItemPrice.setText(String.format("%,.0f VND", calculateTotalSellingPrice(order.getListOfOrderdetails())));
        tvShippingCost.setText(String.format("%,.0f VND", order.getDelivery().getShippingCost()));
        tvPaymentMethod.setText(order.getPayment().getName());
        tvTotalPrice.setText(String.format("%,.0f VND", order.getTotal()));

        fetchFeeAndDiscount(order.getOrderId(), this);
    }

    private void setupOrderItems() {
        orderItemAdapter = new OrderItemAdapter(order.getListOfOrderdetails(), this);
        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        rvOrderItems.setAdapter(orderItemAdapter);
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
            public void onResponse(Call<APIResponse<List<ResFeeAndDiscountDTO>>> call,
                                   Response<APIResponse<List<ResFeeAndDiscountDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    calculateAndDisplayDiscounts(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<APIResponse<List<ResFeeAndDiscountDTO>>> call, Throwable t) {
                Log.e("fetchFeeAndDiscount", "API call failed: " + t.getMessage());
            }
        });
    }

    private void calculateAndDisplayDiscounts(List<ResFeeAndDiscountDTO> fees) {
        double productDiscount = 0;
        double voucherDiscount = 0;

        for (ResFeeAndDiscountDTO fee : fees) {
            String desc = fee.getDescription() != null ? fee.getDescription().toLowerCase() : "";
            if (desc.contains("voucher")) {
                voucherDiscount += fee.getAmount();
            } else if (desc.contains("sản phẩm")) {
                productDiscount += fee.getAmount();
            }
        }

        tvProductDiscount.setText(String.format("%,.0f VND", productDiscount));
        tvVoucherDiscount.setText(String.format("%,.0f VND", voucherDiscount));
    }

    private double calculateTotalSellingPrice(List<ResOrderDetailDTO> orderDetails) {
        double total = 0;
        for (ResOrderDetailDTO detail : orderDetails) {
            total += detail.getProduct().getSellingPrice() * detail.getQuantity();
        }
        return total;
    }

    private void setupButtonsByStatus() {
        String status = order.getStatus();
        btnCancelOrder.setVisibility(View.GONE);
        btnReceived.setVisibility(View.GONE);
        btnRepurchase.setVisibility(View.GONE);
        btnNotReceived.setVisibility(View.GONE);
        btnReview.setVisibility(View.GONE);

        switch (status) {
            case "PENDING":
                btnCancelOrder.setVisibility(View.VISIBLE);
                btnCancelOrder.setOnClickListener(v -> updateOrderStatus("CANCELLED"));
                break;
            case "WAITING":
                btnCancelOrder.setVisibility(View.VISIBLE);
                btnCancelOrder.setOnClickListener(v -> updateOrderStatus("CANCELLED"));
                break;
            case "TRANSPORT":
                btnReceived.setVisibility(View.VISIBLE);
                btnReceived.setOnClickListener(v -> updateOrderStatus("COMPLETED"));
                break;
            case "COMPLETED":
                btnRepurchase.setVisibility(View.VISIBLE);
                btnReview.setVisibility(View.VISIBLE);

                btnRepurchase.setOnClickListener(v -> repurchaseAllItems());

                btnReview.setOnClickListener(v -> {
                    if (!order.getListOfOrderdetails().isEmpty()) {
                        ResOrderDetailDTO orderDetail = order.getListOfOrderdetails().get(0);
                        Intent intent = new Intent(OrderDetailActivity.this, ProductDetailActivity.class);
                        intent.putExtra("product", orderDetail.getProduct());
                        intent.putExtra("order_detail", orderDetail);
                        intent.putExtra("from_order", true);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this,
                                "No products found in this order",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case "CANCELLED":
                btnRepurchase.setVisibility(View.VISIBLE);
                btnRepurchase.setOnClickListener(v -> repurchaseAllItems());
                break;
        }
    }

    private void repurchaseAllItems() {
        Pair<String, Integer> accessTokenWithUserId = SharedPref.getAccessTokenWithUserId(this);
        String accessToken = accessTokenWithUserId.first;

        for (ResOrderDetailDTO orderDetail : order.getListOfOrderdetails()) {
            addToCart(orderDetail.getProduct().getProductId(),
                    orderDetail.getQuantity(),
                    accessToken,
                    () -> {
                        if (order.getListOfOrderdetails().indexOf(orderDetail) == order.getListOfOrderdetails().size() - 1) {
                            navigateToAddToBag(accessToken);
                        }
                    });
        }
    }

    private void navigateToAddToBag(String accessToken) {
        List<String> filters = Arrays.asList("cart:" + cartId);
        CartFetching apiService = APIClient.getClientWithToken(accessToken).create(CartFetching.class);

        apiService.getCartDetail(filters).enqueue(new Callback<APIResponse<PaginationWrapper<ResCartDetailDTO>>>() {
            @Override
            public void onResponse(Call<APIResponse<PaginationWrapper<ResCartDetailDTO>>> call,
                                   Response<APIResponse<PaginationWrapper<ResCartDetailDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ResCartDetailDTO> detailList = response.body().getData().getData();
                    if (detailList != null && !detailList.isEmpty()) {
                        cartDetails.clear();
                        cartDetails.addAll(detailList);
                        Log.d("DEBUG_CART", "Cart details size: " + cartDetails.size());

                        Intent intent = new Intent(OrderDetailActivity.this, AddToBagActivity.class);
                        intent.putExtra("selectedItems", new Gson().toJson(cartDetails));
                        startActivity(intent);
                    } else {
                        Log.e("CartActivity", "No cart details found");
                        Toast.makeText(OrderDetailActivity.this, "No items in cart", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("CartActivity", "Failed to fetch cart details");
                    Toast.makeText(OrderDetailActivity.this, "Failed to load cart", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse<PaginationWrapper<ResCartDetailDTO>>> call, Throwable t) {
                Log.e("CartActivity", "Error fetching cart details: " + t.getMessage());
                Toast.makeText(OrderDetailActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateOrderStatus(String newStatus) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("orderId", order.getOrderId());
        requestBody.put("status", newStatus);

        String accessToken = SharedPref.getAccessTokenWithUserId(this).first;
        if (accessToken == null) {
            Toast.makeText(this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            return;
        }

        OrderFetching orderService = APIClient.getClientWithToken(accessToken).create(OrderFetching.class);
        Call<APIResponse<ResOrderDTO>> call = orderService.updateOrderStatus(requestBody);

        call.enqueue(new Callback<APIResponse<ResOrderDTO>>() {
            @Override
            public void onResponse(Call<APIResponse<ResOrderDTO>> call,
                                   Response<APIResponse<ResOrderDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    order.setStatus(newStatus);
                    tvOrderStatus.setText(newStatus);
                    setupButtonsByStatus();
                    showStatusUpdateMessage(newStatus);
                } else {
                    Toast.makeText(OrderDetailActivity.this,
                            "Thao tác thất bại",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse<ResOrderDTO>> call, Throwable t) {
                Toast.makeText(OrderDetailActivity.this,
                        "Lỗi kết nối: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showStatusUpdateMessage(String status) {
        String message = status.equals("CANCELLED")
                ? "Đã hủy đơn hàng thành công"
                : "Đã xác nhận nhận hàng thành công";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void addToCart(int productId, int quantity, String accessToken, Runnable onSuccess) {
        if (productId == -1) {
            Toast.makeText(this, "Error: product does not exist", Toast.LENGTH_SHORT).show();
            return;
        }

        CartFetching api = APIClient.getClientWithToken(accessToken).create(CartFetching.class);
        ReqAddToCartDTO requestProduct = new ReqAddToCartDTO(productId, quantity);

        api.addCartDetail(requestProduct).enqueue(new Callback<APIResponse<ResCartDTO>>() {
            @Override
            public void onResponse(Call<APIResponse<ResCartDTO>> call, Response<APIResponse<ResCartDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(OrderDetailActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();

                    ResCartDTO cart = response.body().getData();
                    if (cart != null) {
                        cartId = cart.getCartId();
                        Log.d("OrderDetail", "cartDetailId: " + cartId);
                    }

                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                } else {
                    Toast.makeText(OrderDetailActivity.this, "Failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse<ResCartDTO>> call, Throwable t) {
                Toast.makeText(OrderDetailActivity.this, "Connection error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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