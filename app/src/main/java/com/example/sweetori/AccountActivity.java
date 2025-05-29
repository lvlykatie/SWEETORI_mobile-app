package com.example.sweetori;

import android.content.Intent;
import android.os.Bundle;
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
import android.view.inputmethod.InputMethodManager;
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
    Button btnGeneral, btnPurchase, btnSupport, btnApply;
    FrameLayout tabContent;
    LinearLayout btnLogOut;
    LinearLayout btnResetPass;
    LinearLayout btn_wishlist;
    TextView txtHello, txtName;
    EditText txtLastName, txtFirstName, txtEmail, txtPhone, txtAddress;

    private ResUserDTO currentUser;
    private String accessToken;
    private int userId;

    // ✅ Thêm listener để ẩn bàn phím khi mất focus
    private final View.OnFocusChangeListener onDoneListener = (view, hasFocus) -> {
        if (!hasFocus) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    };

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

        currentUser = SharedPref.getUser(this);
        accessToken = SharedPref.getAccessTokenWithUserId(this).first;
        userId = SharedPref.getAccessTokenWithUserId(this).second;

        if (currentUser != null && currentUser.getFirstName() != null) {
            txtHello.setText("Hello, " + currentUser.getFirstName());
        } else {
            txtHello.setText("Guest");
        }

        btnHome.setOnClickListener(v -> startActivity(new Intent(this, HomepageActivity.class)));
        btnCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        btnNoti.setOnClickListener(v -> startActivity(new Intent(this, NotiActivity.class)));
        btnVoucher.setOnClickListener(v -> startActivity(new Intent(this, VoucherActivity.class)));

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
            purchaseView.findViewById(R.id.llPending).setOnClickListener(v1 -> openOrderTracking("PENDING"));
            purchaseView.findViewById(R.id.llWaiting).setOnClickListener(v1 -> openOrderTracking("WAITING"));
            purchaseView.findViewById(R.id.llTransport).setOnClickListener(v1 -> openOrderTracking("TRANSPORT"));
            purchaseView.findViewById(R.id.llCompleted).setOnClickListener(v1 -> openOrderTracking("COMPLETED"));
            purchaseView.findViewById(R.id.llCancelled).setOnClickListener(v1 -> openOrderTracking("CANCELLED"));
        });

        btnSupport.setOnClickListener(v -> {
            highlightTab(btnSupport);
            showTab(R.layout.tab_support);
        });

        btn_wishlist = findViewById(R.id.btn_wishlist);
        btn_wishlist.setOnClickListener(v -> startActivity(new Intent(this, WishListActivity.class)));

        btnResetPass = findViewById(R.id.btn_reset_password);
        btnResetPass.setOnClickListener(v -> {
            AuthFetching authFetching = APIClient.getClientWithToken(accessToken).create(AuthFetching.class);
            authFetching.logout().enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast.makeText(AccountActivity.this, "Enter email to reset password", Toast.LENGTH_SHORT).show();
                    SharedPref.clearTokens(AccountActivity.this);
                    SharedPref.clearUser(AccountActivity.this);
                    Intent loginIntent = new Intent(AccountActivity.this, ForgetEmailActivity.class);
                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(AccountActivity.this, "Log out failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    });
    }

    private void openOrderTracking(String status) {
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
        btnApply = tabView.findViewById(R.id.btnApply);

        if (currentUser != null) {
            txtName.setText(currentUser.getFirstName());
            txtFirstName.setText(currentUser.getFirstName());
            txtLastName.setText(currentUser.getLastName());
            txtEmail.setText(currentUser.getEmail());
            txtPhone.setText(currentUser.getPhoneNumber());
            txtAddress.setText(currentUser.getBuyingAddress());
        }

        txtFirstName.setEnabled(false);
        txtLastName.setEnabled(false);
        txtEmail.setEnabled(false);
        txtPhone.setEnabled(false);
        txtAddress.setEnabled(false);
        btnApply.setVisibility(View.GONE);

        btnEdit.setOnClickListener(v -> {
            txtFirstName.setEnabled(true);
            txtLastName.setEnabled(true);
            txtEmail.setEnabled(true);
            txtPhone.setEnabled(true);
            txtAddress.setEnabled(true);
            txtFirstName.requestFocus();
            btnApply.setVisibility(View.VISIBLE);
        });

        btnApply.setOnClickListener(v -> {
            updateUserInfo();
            txtFirstName.setEnabled(false);
            txtLastName.setEnabled(false);
            txtEmail.setEnabled(false);
            txtPhone.setEnabled(false);
            txtAddress.setEnabled(false);
            btnApply.setVisibility(View.GONE);
        });

        // Gán listener để ẩn bàn phím khi mất focus
        List<EditText> editFields = Arrays.asList(txtFirstName, txtLastName, txtEmail, txtPhone, txtAddress);
        for (EditText field : editFields) {
            field.setOnFocusChangeListener(onDoneListener);
        }

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
                    Toast.makeText(AccountActivity.this, "Log out failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
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
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    ResUserDTO updatedUser = response.body().getData();
                    Toast.makeText(AccountActivity.this, "Update successful!", Toast.LENGTH_SHORT).show();
                    currentUser = updatedUser;
                    txtName.setText(updatedUser.getFirstName());
                    txtHello.setText("Hello, " + updatedUser.getFirstName());
                    
                    SharedPref.saveUser(AccountActivity.this, updatedUser);
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
        List<Button> tabs = Arrays.asList(btnGeneral, btnPurchase, btnSupport);
        for (Button tab : tabs) {
            tab.setBackground(null);
            tab.setTextColor(ContextCompat.getColor(this, R.color.black));
        }
        activeTab.setBackground(getRoundedBackground(ContextCompat.getColor(this, R.color.color02)));
        activeTab.setTextColor(ContextCompat.getColor(this, R.color.white));
    }
}
