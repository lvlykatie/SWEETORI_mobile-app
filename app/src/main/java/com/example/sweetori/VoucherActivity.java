package com.example.sweetori;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweetori.adapter.VoucherAdapter;
import com.example.sweetori.content.VoucherFetching;
import com.example.sweetori.dto.response.ResVoucherDTO;
import com.example.sweetori.dto.response.ResUserDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoucherActivity extends AppCompatActivity {
    ImageView btnAccount, btnHome, btnCart, btnNoti, btnVoucher;
    private ResUserDTO currentUser;
    RecyclerView rvVoucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voucher);

        currentUser = SharedPref.getUser(this);

        btnAccount = findViewById(R.id.btnAccount);
        btnHome = findViewById(R.id.btnHome);
        btnCart = findViewById(R.id.btnCart);
        btnNoti = findViewById(R.id.btnNoti);
        btnVoucher = findViewById(R.id.btnVoucher);
        rvVoucher = findViewById(R.id.rvVoucher);

        btnAccount.setOnClickListener(v -> startActivity(new Intent(this, AccountActivity.class)));
        btnHome.setOnClickListener(v -> startActivity(new Intent(this, HomepageActivity.class)));
        btnCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        btnNoti.setOnClickListener(v -> startActivity(new Intent(this, NotiActivity.class)));

        fetchVoucherByUserId();
    }

    private void fetchVoucherByUserId() {
        Pair<String, Integer> accessTokenWithUserId = SharedPref.getAccessTokenWithUserId(this);
        if (accessTokenWithUserId == null) {
            Log.e("VoucherAPI", "Token hoặc UserId không hợp lệ");
            return;
        }

        VoucherFetching voucherService = APIClient.getClientWithToken(accessTokenWithUserId.first).create(VoucherFetching.class);

        voucherService.getAllVouchers().enqueue(new Callback<APIResponse<ResVoucherDTO>>() {
            @Override
            public void onResponse(Call<APIResponse<ResVoucherDTO>> call, Response<APIResponse<ResVoucherDTO>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<ResVoucherDTO.VoucherData> voucherList = response.body().getData().getData();

                    if (voucherList != null && !voucherList.isEmpty()) {
                        // Lọc voucher chỉ dành cho user hiện tại và chưa dùng (có thể làm trực tiếp trong adapter, nhưng lọc ở đây cũng được)
                        VoucherAdapter adapter = new VoucherAdapter(VoucherActivity.this, voucherList);
                        rvVoucher.setLayoutManager(new LinearLayoutManager(VoucherActivity.this));
                        rvVoucher.setAdapter(adapter);
                    } else {
                        Toast.makeText(VoucherActivity.this, "Không có voucher nào.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VoucherActivity.this, "Không thể tải danh sách voucher.", Toast.LENGTH_SHORT).show();
                    Log.e("VoucherAPI", "Phản hồi lỗi hoặc không có dữ liệu.");
                }
            }

            @Override
            public void onFailure(Call<APIResponse<ResVoucherDTO>> call, Throwable t) {
                Toast.makeText(VoucherActivity.this, "Lỗi kết nối đến máy chủ.", Toast.LENGTH_SHORT).show();
                Log.e("VoucherAPI", "Lỗi: " + t.getMessage());
            }
        });
    }
}
