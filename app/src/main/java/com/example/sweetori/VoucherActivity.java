package com.example.sweetori;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweetori.adapter.VoucherAdapter;
import com.example.sweetori.content.VoucherFetching;
import com.example.sweetori.dto.response.ResUserVoucherDTO;
import com.example.sweetori.dto.response.ResUserDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoucherActivity extends AppCompatActivity {

    private ImageView btnAccount, btnHome, btnCart, btnNoti, btnVoucher;
    private RecyclerView rvVoucher;
    private VoucherAdapter adapter;
    private ResUserDTO currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voucher);

        currentUser = SharedPref.getUser(this);
        if (currentUser == null) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupNavigation();
        setupRecyclerView();

        Pair<String, Integer> tokenWithUserId = SharedPref.getAccessTokenWithUserId(this);
        if (tokenWithUserId == null) {
            Log.e("VoucherAPI", "Token hoặc UserId không hợp lệ");
            Toast.makeText(this, "Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchVouchers(tokenWithUserId.first, currentUser.getUserId());
    }

    private void initViews() {
        btnAccount = findViewById(R.id.btnAccount);
        btnHome = findViewById(R.id.btnHome);
        btnCart = findViewById(R.id.btnCart);
        btnNoti = findViewById(R.id.btnNoti);
        btnVoucher = findViewById(R.id.btnVoucher);
        rvVoucher = findViewById(R.id.rvVoucher);
    }

    private void setupNavigation() {
        btnAccount.setOnClickListener(v -> startActivity(new Intent(this, AccountActivity.class)));
        btnHome.setOnClickListener(v -> startActivity(new Intent(this, HomepageActivity.class)));
        btnCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        btnNoti.setOnClickListener(v -> startActivity(new Intent(this, NotiActivity.class)));
        // btnVoucher không set onClick vì đang ở màn hình Voucher
    }

    private void setupRecyclerView() {
        adapter = new VoucherAdapter(this, new ArrayList<>());
        rvVoucher.setLayoutManager(new LinearLayoutManager(this));
        rvVoucher.setAdapter(adapter);
    }

    private void fetchVouchers(String token, int userId) {
        String filter = "user:" + userId;

        VoucherFetching voucherService = APIClient.getClientWithToken(token)
                .create(VoucherFetching.class);

        voucherService.getVouchersByUser(filter).enqueue(new Callback<APIResponse<ResUserVoucherDTO>>() {
            @Override
            public void onResponse(Call<APIResponse<ResUserVoucherDTO>> call, Response<APIResponse<ResUserVoucherDTO>> response) {
                if (response.isSuccessful()) {
                    APIResponse<ResUserVoucherDTO> apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getData() != null) {
                        List<ResUserVoucherDTO.UserVoucherData> allVouchers = apiResponse.getData().getData();
                        if (allVouchers != null) {
                            List<ResUserVoucherDTO.UserVoucherData> unusedVouchers = new ArrayList<>();
                            for (ResUserVoucherDTO.UserVoucherData voucher : allVouchers) {
                                if (!voucher.isUsed()) {
                                    unusedVouchers.add(voucher);
                                }
                            }

                            adapter.updateData(unusedVouchers);

                            if (unusedVouchers.isEmpty()) {
                                Toast.makeText(VoucherActivity.this, "Không có voucher nào chưa dùng.", Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                    }
                    Toast.makeText(VoucherActivity.this, "Không có dữ liệu voucher.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VoucherActivity.this, "Không thể tải danh sách voucher.", Toast.LENGTH_SHORT).show();
                    Log.e("VoucherAPI", "Phản hồi không thành công: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<APIResponse<ResUserVoucherDTO>> call, Throwable t) {
                Toast.makeText(VoucherActivity.this, "Lỗi kết nối đến máy chủ.", Toast.LENGTH_SHORT).show();
                Log.e("VoucherAPI", "Lỗi: " + t.getMessage());
            }
        });
    }
}
