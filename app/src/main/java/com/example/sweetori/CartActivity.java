package com.example.sweetori;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweetori.APIClient;
import com.example.sweetori.adapter.CartDetailAdapter;
import com.example.sweetori.content.CartFetching;
import com.example.sweetori.dto.response.ResCartDTO;
import com.example.sweetori.dto.response.ResCartDTO.CartDetail;
import com.example.sweetori.SharedPref;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    ImageView btnAccount, btnHome, btnCart, btnNoti, btnVoucher;
    RecyclerView recyclerViewCart;
    CartDetailAdapter cartDetailAdapter;
    TextView txtTotalQuantity, txtTotalPrice;

    List<CartDetail> cartDetails;
    Button imgbtn_buynow_cart;
    CheckBox checkboxall;
    private boolean isSelectAllChangingProgrammatically = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.cart);

        // Khởi tạo các thành phần giao diện
        btnAccount = findViewById(R.id.btnAccount);
        btnHome = findViewById(R.id.btnHome);
        btnCart = findViewById(R.id.btnCart);
        btnNoti = findViewById(R.id.btnNoti);
        btnVoucher = findViewById(R.id.btnVoucher);
        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        txtTotalQuantity = findViewById(R.id.txtTotalQuantity);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        imgbtn_buynow_cart = findViewById(R.id.imgbtn_buynow_cart);
        checkboxall = findViewById(R.id.checkboxall);



        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));

        // Bắt sự kiện điều hướng
        btnAccount.setOnClickListener(v -> startActivity(new Intent(this, AccountActivity.class)));
        btnHome.setOnClickListener(v -> startActivity(new Intent(this, HomepageActivity.class)));
        btnNoti.setOnClickListener(v -> startActivity(new Intent(this, NotiActivity.class)));
        btnVoucher.setOnClickListener(v -> startActivity(new Intent(this, VoucherActivity.class)));

        imgbtn_buynow_cart.setOnClickListener(v -> {
            List<ResCartDTO.CartDetail> selectedItems = cartDetailAdapter.getSelectedItems();
            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "Please select at least one item", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, AddToBagActivity.class);
                intent.putExtra("selectedItems", new Gson().toJson(selectedItems));
                startActivity(intent);
            }
        });

        checkboxall.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (cartDetailAdapter != null) {
                cartDetailAdapter.setAllSelected(isChecked);
                updateTotals();
            }
        });


        // Gọi API lấy giỏ hàng
        String accessToken = SharedPref.getAccessToken(this);
        int userId = SharedPref.getUserId(this);
        String filter = "user:" + userId;

        CartFetching apiService = APIClient.getClientWithToken(accessToken).create(CartFetching.class);
        apiService.getCart(filter).enqueue(new Callback<ResCartDTO>() {
            @Override
            public void onResponse(Call<ResCartDTO> call, Response<ResCartDTO> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<ResCartDTO.Cart> carts = response.body().getData().getData();
                    if (carts != null && !carts.isEmpty()) {
                        cartDetails = carts.get(0).getListOfCartdetails();

                        for (CartDetail detail : cartDetails) {
                            detail.setSelected(false);
                        }
                        cartDetailAdapter = new CartDetailAdapter(cartDetails, new CartDetailAdapter.OnItemClickListener() {
                            @Override
                            public void onDeleteClick(CartDetail item, int position) {
                                deleteCartItemFromServer(item.getCartDetailsId(), position);
                            }

                            @Override
                            public void onQuantityChanged() {
                                updateTotals();
                            }
                        },
                        new CartDetailAdapter.SelectAllCheckboxListener() {
                            @Override
                            public void onSelectAllChecked(boolean isAllSelected) {
                                isSelectAllChangingProgrammatically = true;
                                checkboxall.setChecked(isAllSelected);
                                isSelectAllChangingProgrammatically = false;
                            }
                        });

                        recyclerViewCart.setAdapter(cartDetailAdapter);
                        updateTotals();

                    }
                } else {
                    Log.e("CartActivity", "No data");
                }
            }

            @Override
            public void onFailure(Call<ResCartDTO> call, Throwable t) {
                Log.e("CartActivity", "failse: " + t.getMessage());
            }
        });
    }

    private void deleteCartItemFromServer(int cartDetailId, int position) {
        String accessToken = SharedPref.getAccessToken(this);
        CartFetching apiService = APIClient.getClientWithToken(accessToken).create(CartFetching.class);

        apiService.deleteCartItem(cartDetailId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    cartDetailAdapter.removeItem(position);
                    updateTotals();
                    Log.d("CartActivity", "Đã xoá item khỏi server");
                } else {
                    Log.e("CartActivity", "Xoá thất bại");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("CartActivity", "Lỗi xoá item: " + t.getMessage());
            }
        });
    }

    // Hàm tính tổng số lượng và tổng tiền
    private void updateTotals() {
        int totalQuantity = 0;
        double totalPrice = 0;

        if (cartDetails != null) {
            for (CartDetail detail : cartDetails) {
                if (detail.isSelected()) {
                    totalQuantity += detail.getQuantity();
                    totalPrice += detail.getQuantity() * detail.getProduct().getSellingPrice();
                }
            }
        }

        txtTotalQuantity.setText("Total quantity: " + totalQuantity);
        txtTotalPrice.setText(String.format("Total: %, .0f VND", totalPrice));
    }

    private void updateSelectAllCheckbox() {
        if (cartDetails == null || cartDetails.isEmpty()) {
            checkboxall.setChecked(false);
            return;
        }

        boolean allSelected = true;
        for (CartDetail detail : cartDetails) {
            if (!detail.isSelected()) {
                allSelected = false;
                break;
            }
        }

        isSelectAllChangingProgrammatically = true;
        checkboxall.setChecked(allSelected);
        isSelectAllChangingProgrammatically = false;
    }
}
