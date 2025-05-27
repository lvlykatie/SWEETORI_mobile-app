package com.example.sweetori;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.graphics.drawable.GradientDrawable;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sweetori.content.AuthFetching;
import com.example.sweetori.content.UserFetching;
import com.example.sweetori.dto.request.ReqUserDTO;
import com.example.sweetori.dto.response.ResUserDTO;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountActivity extends AppCompatActivity {

    ImageView btnAccount;
    ImageView btnHome;
    ImageView btnCart;
    ImageView btnNoti;
    ImageView btnVoucher, btnEdit;
    Button btnGeneral, btnPurchase, btnSupport;
    FrameLayout tabContent;
    LinearLayout btnLogOut;
    LinearLayout btnResetPass;
    LinearLayout btn_wishlist;
    TextView txtHello, txtName;
    EditText txtLastName, txtFirstName, txtEmail, txtPhone, txtAddress;

    private ResUserDTO currentUser;
    private String accessToken;
    private int userId;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);
        EdgeToEdge.enable(this);

        btnGeneral = findViewById(R.id.tabGeneral);
        btnPurchase = findViewById(R.id.tabPurchase);
        btnSupport = findViewById(R.id.tabSupport);
        tabContent = findViewById(R.id.tabContent);
        btnAccount = findViewById(R.id.btnAccount);
        btnHome = findViewById(R.id.btnHome);
        btnCart = findViewById(R.id.btnCart);
        btnNoti = findViewById(R.id.btnNoti);
        btnVoucher = findViewById(R.id.btnVoucher);
        txtHello = findViewById(R.id.txtHello);

        // Gán dữ liệu từ Intent
        currentUser = SharedPref.getUser(this);
        accessToken = SharedPref.getAccessTokenWithUserId(this).first;
        userId = SharedPref.getAccessTokenWithUserId(this).second;

        if (currentUser != null && currentUser.getFirstName() != null) {
            txtHello.setText("Hello, " + currentUser.getFirstName());
        } else {
            txtHello.setText("Guest");
        }

        btnHome.setOnClickListener(v -> {
            Intent home = new Intent(AccountActivity.this, HomepageActivity.class);
            startActivity(home);
        });

        btnCart.setOnClickListener(v -> {
            Intent cart = new Intent(AccountActivity.this, CartActivity.class);
            startActivity(cart);
        });

        btnNoti.setOnClickListener(v -> {
            Intent noti = new Intent(AccountActivity.this, NotiActivity.class);
            startActivity(noti);
        });

        btnVoucher.setOnClickListener(v -> {
            Intent voucher = new Intent(AccountActivity.this, VoucherActivity.class);
            startActivity(voucher);
        });

        // Mặc định hiển thị tab General
        showTab(R.layout.tab_general);
        highlightTab(btnGeneral);
        setupGeneralTab();

        btnGeneral.setOnClickListener(v -> {
            highlightTab(btnGeneral);
            showTab(R.layout.tab_general);
            setupGeneralTab();
        });

        btnPurchase.setOnClickListener(v -> {
            highlightTab(btnPurchase);
            showTab(R.layout.tab_purchase);

            View purchaseView = tabContent.getChildAt(0);
            LinearLayout llPending = purchaseView.findViewById(R.id.llPending);
            LinearLayout llWaiting = purchaseView.findViewById(R.id.llWaiting);
            LinearLayout llTransport = purchaseView.findViewById(R.id.llTransport);
            LinearLayout llCompleted = purchaseView.findViewById(R.id.llCompleted);
            LinearLayout llCancelled = purchaseView.findViewById(R.id.llCancelled);

            llPending.setOnClickListener(v1 -> openOrderTracking("PENDING", currentUser));
            llWaiting.setOnClickListener(v1 -> openOrderTracking("WAITING", currentUser));
            llTransport.setOnClickListener(v1 -> openOrderTracking("TRANSPORT", currentUser));
            llCompleted.setOnClickListener(v1 -> openOrderTracking("COMPLETED", currentUser));
            llCancelled.setOnClickListener(v1 -> openOrderTracking("CANCELLED", currentUser));
        });

        btnSupport.setOnClickListener(v -> {
            highlightTab(btnSupport);
            showTab(R.layout.tab_support);
        });

        btn_wishlist = findViewById(R.id.btn_wishlist);
        btn_wishlist.setOnClickListener(v -> {
            Intent wishlist = new Intent(AccountActivity.this, WishListActivity.class);
            startActivity(wishlist);
        });

        btnResetPass = findViewById(R.id.btn_reset_password);
        btnResetPass.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, CreatePassActivity.class);
            startActivity(intent);
        });
    }

    private void openOrderTracking(String status, ResUserDTO currentUser) {
        Intent intent = new Intent(AccountActivity.this, OrderTrackingActivity.class);
        intent.putExtra("orderStatus", status);
        intent.putExtra("user", currentUser);
        intent.putExtra("accessToken", accessToken);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    private void showTab(int layoutResId) {
        View view = getLayoutInflater().inflate(layoutResId, null);
        tabContent.removeAllViews();
        tabContent.addView(view);
    }

    private void setupGeneralTab() {
        View tabView = tabContent.getChildAt(0);

        txtFirstName = tabView.findViewById(R.id.txtFirstName);
        txtLastName = tabView.findViewById(R.id.txtLastName);
        txtEmail = tabView.findViewById(R.id.txtEmail);
        txtPhone = tabView.findViewById(R.id.txtPhone);
        txtAddress = tabView.findViewById(R.id.txtAddress);
        btnEdit = tabView.findViewById(R.id.btnEdit);
        btnLogOut = tabView.findViewById(R.id.btnLogOut);
        txtName = tabView.findViewById(R.id.txtName);

        txtName.setText(currentUser.getFirstName());
        txtFirstName.setText(currentUser.getFirstName());
        txtLastName.setText(currentUser.getLastName());
        txtEmail.setText(currentUser.getEmail());
        txtPhone.setText(currentUser.getPhoneNumber());
        txtAddress.setText(currentUser.getBuyingAddress());

        txtFirstName.setEnabled(false);
        txtLastName.setEnabled(false);
        txtEmail.setEnabled(false);
        txtPhone.setEnabled(false);
        txtAddress.setEnabled(false);

        btnEdit.setOnClickListener(v -> {
            txtFirstName.setEnabled(true);
            txtLastName.setEnabled(true);
            txtEmail.setEnabled(true);
            txtPhone.setEnabled(true);
            txtAddress.setEnabled(true);
            txtFirstName.requestFocus();
        });

        View.OnFocusChangeListener onDoneListener = (v, hasFocus) -> {
            if (!hasFocus) {
                updateUserInfo();
                txtFirstName.setEnabled(false);
                txtLastName.setEnabled(false);
                txtEmail.setEnabled(false);
                txtPhone.setEnabled(false);
                txtAddress.setEnabled(false);
            }
        };

        txtFirstName.setOnFocusChangeListener(onDoneListener);
        txtLastName.setOnFocusChangeListener(onDoneListener);
        txtEmail.setOnFocusChangeListener(onDoneListener);
        txtPhone.setOnFocusChangeListener(onDoneListener);
        txtAddress.setOnFocusChangeListener(onDoneListener);

        btnLogOut.setOnClickListener(v -> {
            AuthFetching authFetching = APIClient.getClientWithToken(accessToken).create(AuthFetching.class);

            authFetching.logout().enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast.makeText(AccountActivity.this, "Sign out successfully!", Toast.LENGTH_SHORT).show();
                    SharedPref.clearTokens(AccountActivity.this);
                    SharedPref.clearUser(AccountActivity.this);
                    Intent loginIntent = new Intent(AccountActivity.this, SignInActivity.class);
                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(AccountActivity.this, "Lỗi khi đăng xuất: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        btn_wishlist.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, WishListActivity.class);
            startActivity(intent);
        });
    }

    private void updateUserInfo() {
        UserFetching apiService = APIClient.getClientWithToken(accessToken).create(UserFetching.class);
        ReqUserDTO request = new ReqUserDTO(
                txtFirstName.getText().toString(),
                txtLastName.getText().toString(),
                txtPhone.getText().toString(),
                txtEmail.getText().toString(),
                txtAddress.getText().toString()
        );

        apiService.updateUser(request).enqueue(new Callback<APIResponse<ResUserDTO>>() {
            @Override
            public void onResponse(Call<APIResponse<ResUserDTO>> call, Response<APIResponse<ResUserDTO>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AccountActivity.this, "Update successful!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AccountActivity.this, "Update failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse<ResUserDTO>> call, Throwable t) {
                Toast.makeText(AccountActivity.this, "Connection error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private GradientDrawable getRoundedBackground(int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(60);
        return drawable;
    }

    private void highlightTab(Button activeTab) {
        // Tạo danh sách các tab để dễ dàng xử lý
        List<Button> tabs = Arrays.asList(btnGeneral, btnPurchase, btnSupport);

        // Reset tất cả tab về trong suốt và thay đổi màu chữ về mặc định
        for (Button tab : tabs) {
            tab.setBackground(null);
            tab.setTextColor(ContextCompat.getColor(this, R.color.black));
        }

        // Đặt màu nền cho tab đang được chọn
        activeTab.setBackground(getRoundedBackground(
                ContextCompat.getColor(this, R.color.color02)
        ));
        activeTab.setTextColor(ContextCompat.getColor(this, R.color.white));
    }
}

