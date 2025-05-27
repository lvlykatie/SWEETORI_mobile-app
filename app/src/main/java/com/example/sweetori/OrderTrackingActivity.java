package com.example.sweetori;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweetori.adapter.OrderAdapter;
import com.example.sweetori.content.OrderFetching;
import com.example.sweetori.dto.response.ResOrderDTO;
import com.example.sweetori.dto.response.ResUserDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderTrackingActivity extends AppCompatActivity {
    private ImageView btnHome, btnCart, btnNoti, btnVoucher;
    private ImageView tabPending, tabWaiting, tabTransport, tabCompleted, tabCancelled;
    private RecyclerView orderRecyclerView;
    private OrderAdapter orderAdapter;
    private TextView txtHello;
    private ResUserDTO currentUser;
    private String currentOrderStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_order);
        EdgeToEdge.enable(this);

        initViews();
        setupNavigation();
        setupOrderTabs();

        // Nhận trạng thái đơn hàng ban đầu từ Intent
        currentOrderStatus = getIntent().getStringExtra("orderStatus");
        currentUser = SharedPref.getUser(this);

        if (currentUser != null && currentUser.getFirstName() != null) {
            txtHello.setText("Hello, " + currentUser.getFirstName());
        } else {
            txtHello.setText("Guest");
        }
        setupInitialTab(); // Highlight tab đúng trạng thái

        // Lấy userId và token
        Pair<String, Integer> accessTokenWithUserId = SharedPref.getAccessTokenWithUserId(this);
        int userId = accessTokenWithUserId.second;

        // Setup RecyclerView
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderAdapter = new OrderAdapter(new ArrayList<>(), this);
        orderRecyclerView.setAdapter(orderAdapter);

        // Gọi API lấy đơn hàng
        fetchOrders(userId, accessTokenWithUserId.first);
    }

    private void initViews() {
        btnHome = findViewById(R.id.btnHome);
        btnCart = findViewById(R.id.btnCart);
        btnNoti = findViewById(R.id.btnNoti);
        btnVoucher = findViewById(R.id.btnVoucher);
        tabPending = findViewById(R.id.tabPending);
        tabWaiting = findViewById(R.id.tabWaiting);
        tabTransport = findViewById(R.id.tabTransport);
        tabCompleted = findViewById(R.id.tabCompleted);
        tabCancelled = findViewById(R.id.tabCancelled);
        orderRecyclerView = findViewById(R.id.orderRecyclerView);
        txtHello = findViewById(R.id.txtHello);
    }

    private void setupNavigation() {
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomepageActivity.class);
            startActivity(intent);
        });
        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });
        btnNoti.setOnClickListener(v -> {
            Intent intent = new Intent(this, NotiActivity.class);
            startActivity(intent);
        });
        btnVoucher.setOnClickListener(v -> {
            Intent intent = new Intent(this, VoucherActivity.class);
            startActivity(intent);
        });
    }

    private void setupOrderTabs() {
        tabPending.setOnClickListener(v -> handleTabClick("PENDING", tabPending));
        tabWaiting.setOnClickListener(v -> handleTabClick("WAITING", tabWaiting));
        tabTransport.setOnClickListener(v -> handleTabClick("TRANSPORT", tabTransport));
        tabCompleted.setOnClickListener(v -> handleTabClick("COMPLETED", tabCompleted));
        tabCancelled.setOnClickListener(v -> handleTabClick("CANCELLED", tabCancelled));
    }

    private void setupInitialTab() {
        if (currentOrderStatus != null) {
            switch (currentOrderStatus) {
                case "PENDING": highlightTab(tabPending); break;
                case "WAITING": highlightTab(tabWaiting); break;
                case "TRANSPORT": highlightTab(tabTransport); break;
                case "COMPLETED": highlightTab(tabCompleted); break;
                case "CANCELLED": highlightTab(tabCancelled); break;
            }
        }
    }

    private void handleTabClick(String status, ImageView clickedTab) {
        currentOrderStatus = status;
        highlightTab(clickedTab);
        Pair<String, Integer> accessTokenWithUserId = SharedPref.getAccessTokenWithUserId(this);
        fetchOrders(accessTokenWithUserId.second, accessTokenWithUserId.first);
    }

    private void fetchOrders(int userId, String accessToken) {
        OrderFetching apiService = APIClient.getClientWithToken(accessToken).create(OrderFetching.class);
        apiService.getOrder(userId).enqueue(new Callback<APIResponse<List<ResOrderDTO>>>() {
            @Override
            public void onResponse(Call<APIResponse<List<ResOrderDTO>>> call, Response<APIResponse<List<ResOrderDTO>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<ResOrderDTO> filteredOrders = filterOrders(response.body().getData());
                    orderAdapter.updateOrderList(filteredOrders);

                    if (filteredOrders.isEmpty()) {
                        Toast.makeText(OrderTrackingActivity.this, "Không có đơn hàng nào.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OrderTrackingActivity.this, "Không thể tải đơn hàng.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse<List<ResOrderDTO>>> call, Throwable t) {
                Toast.makeText(OrderTrackingActivity.this, "Lỗi kết nối mạng.", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private List<ResOrderDTO> filterOrders(List<ResOrderDTO> allOrders) {
        List<ResOrderDTO> filteredOrders = new ArrayList<>();
        if (currentOrderStatus == null || currentOrderStatus.isEmpty()) {
            return allOrders;
        }

        for (ResOrderDTO order : allOrders) {
            if (currentOrderStatus.equalsIgnoreCase(order.getStatus())) {
                filteredOrders.add(order);
            }
        }
        return filteredOrders;
    }

    private void highlightTab(ImageView activeTab) {
        ImageView[] tabs = {tabPending, tabWaiting, tabTransport, tabCompleted, tabCancelled};
        for (ImageView tab : tabs) {
            tab.setBackground(null);
            tab.setColorFilter(ContextCompat.getColor(this, R.color.black));
        }

        activeTab.setBackground(getRoundedBackground(ContextCompat.getColor(this, R.color.color02)));
        activeTab.setColorFilter(ContextCompat.getColor(this, R.color.white));
    }

    private GradientDrawable getRoundedBackground(int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(60);
        return drawable;
    }
}
