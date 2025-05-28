package com.example.sweetori;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweetori.adapter.VoucherGroupAdapter;
import com.example.sweetori.content.VoucherFetching;
import com.example.sweetori.dto.response.ResVoucherDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoucherActivity extends AppCompatActivity {
    ImageView btnAccount, btnHome, btnCart, btnNoti, btnVoucher;
    TextView voucher_discount;

    RecyclerView recyclerViewGroup;
    int voucherId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.voucher);

        // Ánh xạ view
        btnAccount = findViewById(R.id.btnAccount);
        btnHome = findViewById(R.id.btnHome);
        btnCart = findViewById(R.id.btnCart);
        btnNoti = findViewById(R.id.btnNoti);
        btnVoucher = findViewById(R.id.btnVoucher);
        recyclerViewGroup = findViewById(R.id.recyclerViewGroup);

        // Xử lý chuyển màn hình
        btnAccount.setOnClickListener(v -> startActivity(new Intent(this, AccountActivity.class)));
        btnHome.setOnClickListener(v -> startActivity(new Intent(this, HomepageActivity.class)));
        btnCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        btnNoti.setOnClickListener(v -> startActivity(new Intent(this, NotiActivity.class)));

        // Gọi API
        fetchVoucherByUserId();
    }

    private void fetchVoucherByUserId() {
        Pair<String, Integer> accessTokenWithUserId = SharedPref.getAccessTokenWithUserId(VoucherActivity.this);
        if (accessTokenWithUserId == null) {
            Log.e("VoucherAPI", "Token hoặc UserId không hợp lệ");
            return;
        }

        VoucherFetching voucherService = APIClient.getClientWithToken(accessTokenWithUserId.first).create(VoucherFetching.class);
        String filter = "users:" + accessTokenWithUserId.second;

        voucherService.getVoucherByUser(filter).enqueue(new Callback<APIResponse<ResVoucherDTO>>() {
            @Override
            public void onResponse(Call<APIResponse<ResVoucherDTO>> call, Response<APIResponse<ResVoucherDTO>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<ResVoucherDTO.VoucherData> voucherList = response.body().getData().getData();

                    if (voucherList!= null && !voucherList.isEmpty()) {
                        List<List<ResVoucherDTO.VoucherData>> grouped = groupVouchers(voucherList);
                        VoucherGroupAdapter groupAdapter = new VoucherGroupAdapter(VoucherActivity.this, grouped);

                        recyclerViewGroup.setLayoutManager(new LinearLayoutManager(VoucherActivity.this));
                        recyclerViewGroup.setAdapter(groupAdapter);
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


    private List<List<ResVoucherDTO.VoucherData>> groupVouchers(List<ResVoucherDTO.VoucherData> originalList) {
        List<List<ResVoucherDTO.VoucherData>> groups = new ArrayList<>();
        int size = originalList.size();
        for (int i = 0; i < size; i += 4) {
            int end = Math.min(size, i + 4);
            groups.add(new ArrayList<>(originalList.subList(i, end)));
        }
        return groups;
    }
}
