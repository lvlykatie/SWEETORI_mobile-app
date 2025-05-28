package com.example.sweetori;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweetori.adapter.CartDetailAdapter;
import com.example.sweetori.content.CartFetching;
import com.example.sweetori.APIResponse;
import com.example.sweetori.dto.request.ReqCartDetailDTO;
import com.example.sweetori.dto.response.ResCartDTO;
import com.example.sweetori.dto.response.ResCartDetailDTO;
import com.example.sweetori.dto.response.ResLoginDTO;
import com.example.sweetori.dto.response.ResUserDTO;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    ImageView btnAccount, btnHome, btnCart, btnNoti, btnVoucher;
    RecyclerView recyclerViewCart;
    CartDetailAdapter cartDetailAdapter;
    TextView txtTotalQuantity, txtTotalPrice, txtHello;
    Button imgbtn_buynow_cart;
    CheckBox checkboxall;

    private boolean isSelectAllChangingProgrammatically = false;
    private List<ResCartDetailDTO> cartDetails;
    private int cartId;
    private Map<Integer, Boolean> selectedMap = new HashMap<>();
    private ResUserDTO currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.cart);

        // Init UI components
        btnAccount = findViewById(R.id.btnAccount);
        btnHome = findViewById(R.id.btnHome);
        btnCart = findViewById(R.id.btnCart);
        btnNoti = findViewById(R.id.btnNoti);
        btnVoucher = findViewById(R.id.btnVoucher);
        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        txtTotalQuantity = findViewById(R.id.txtTotalQuantity);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        txtHello = findViewById(R.id.txtHello);
        imgbtn_buynow_cart = findViewById(R.id.imgbtn_buynow_cart);
        checkboxall = findViewById(R.id.checkboxall);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));

        currentUser = SharedPref.getUser(this);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userJson = prefs.getString("user", null);

        if (userJson != null) {
            Gson gson = new Gson();
            ResLoginDTO.UserLogin user = gson.fromJson(userJson, ResLoginDTO.UserLogin.class);
            if (user != null) {
                String userName = user.getFirstName();
                txtHello.setText("Hello, "+ userName);
            } else {
                txtHello.setText("Guest");
            }
        }

        // Navigation listeners
        btnAccount.setOnClickListener(v -> {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
        });
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomepageActivity.class);
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


        // Buy now button
        imgbtn_buynow_cart.setOnClickListener(v -> {
            if (cartDetailAdapter == null) return;
            List<ResCartDetailDTO> selectedItems = getSelectedItems();
            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "Please select at least one item", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(CartActivity.this, AddToBagActivity.class);
                intent.putExtra("selectedItems", new Gson().toJson(selectedItems));
                startActivity(intent);
            }
        });

        // Check all checkbox
        checkboxall.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isSelectAllChangingProgrammatically && cartDetailAdapter != null) {
                setAllSelected(isChecked);
                cartDetailAdapter.notifyDataSetChanged();
                updateTotals();
            }
        });
        // Fetch cart data
        fetchCartData();
    }

    private void fetchCartData() {
        Pair<String, Integer> accessTokenWithUserId = SharedPref.getAccessTokenWithUserId(CartActivity.this);
        int userId = accessTokenWithUserId.second;

        CartFetching apiService = APIClient.getClientWithToken(accessTokenWithUserId.first).create(CartFetching.class);
        apiService.getCart(userId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<APIResponse<ResCartDTO>> call, Response<APIResponse<ResCartDTO>> response) {
                if (response.isSuccessful() && response.body() != null &&
                        response.body().getData() != null &&
                        response.body().getData().getListOfCartdetails() != null) {
                    cartId = response.body().getData().getCartId();
                    Log.d("CartActivity", "Data fetched successfully");
                    cartDetails = response.body().getData().getListOfCartdetails();
                    Log.d("CartActivity", cartDetails.toString());
                    selectedMap.clear();
                    for (ResCartDetailDTO detail : cartDetails) {
                        selectedMap.put(detail.getCartDetailsId(), false);
                    }

                    cartDetailAdapter = new CartDetailAdapter(
                            cartDetails,
                            new CartDetailAdapter.OnItemClickListener() {
                                @Override
                                public void onDeleteClick(ResCartDetailDTO item, int position) {
                                    deleteCartItemFromServer(item.getCartDetailsId(), position);
                                }

                                @Override
                                public void onQuantityChanged(ResCartDetailDTO item) {
                                    updateTotals();
                                    updateSelectAllCheckbox();
                                    updateCartDetailQuantity(
                                            item.getCartDetailsId(),
                                            item.getQuantity(),
                                            cartId,
                                            item.getProduct().getProductId()
                                    );
                                }

                                @Override
                                public void onItemSelectedChanged(int cartDetailId, boolean isSelected) {
                                    selectedMap.put(cartDetailId, isSelected);
                                    updateTotals();
                                    updateSelectAllCheckbox();
                                }

                                public void onItemClick(ResCartDetailDTO item, int position) {
                                    Intent intent = new Intent(CartActivity.this, ProductDetailActivity.class);
                                    intent.putExtra("productId", item.getProduct().getProductId());
                                    startActivity(intent);
                                }
                            },
                            isAllSelected -> {
                                isSelectAllChangingProgrammatically = true;
                                checkboxall.setChecked(isAllSelected);
                                isSelectAllChangingProgrammatically = false;
                            },
                            selectedMap
                    );

                    recyclerViewCart.setAdapter(cartDetailAdapter);
                    updateTotals();

                } else {
                    Log.e("CartActivity", "Failed to get data or data is null");
                }
            }

            @Override
            public void onFailure(Call<APIResponse<ResCartDTO>> call, Throwable t) {
                Log.e("CartActivity", "API call failed: " + t.getMessage());
            }
        });
    }

    private List<ResCartDetailDTO> getSelectedItems() {
        List<ResCartDetailDTO> selectedItems = new java.util.ArrayList<>();
        if (cartDetails != null) {
            for (ResCartDetailDTO detail : cartDetails) {
                Boolean isSelected = selectedMap.get(detail.getCartDetailsId());
                if (isSelected != null && isSelected) {
                    selectedItems.add(detail);
                }
            }
        }
        return selectedItems;
    }

    private void setAllSelected(boolean isSelected) {
        if (cartDetails != null) {
            for (ResCartDetailDTO detail : cartDetails) {
                selectedMap.put(detail.getCartDetailsId(), isSelected);
            }
        }
    }

    private void deleteCartItemFromServer(int cartDetailId, int position) {
        Pair<String, Integer> accessTokenWithUserId = SharedPref.getAccessTokenWithUserId(CartActivity.this);
        CartFetching apiService = APIClient.getClientWithToken(accessTokenWithUserId.first).create(CartFetching.class);

        apiService.deleteCartItem(cartDetailId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    cartDetailAdapter.removeItem(position);
                    selectedMap.remove(cartDetailId);
                    updateTotals();
                    updateSelectAllCheckbox();
                    Log.d("CartActivity", "Item deleted successfully");
                } else {
                    Log.e("CartActivity", "Failed to delete item");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("CartActivity", "Error deleting item: " + t.getMessage());
            }
        });
    }

    private void updateTotals() {
        int totalQuantity = 0;
        double totalPrice = 0;

        if (cartDetails != null) {
            for (ResCartDetailDTO detail : cartDetails) {
                Boolean isSelected = selectedMap.get(detail.getCartDetailsId());
                if (isSelected != null && isSelected) {
                    totalQuantity += 1;
                    totalPrice += detail.getQuantity() * detail.getProduct().getSellingPrice();
                }
            }
        }

        txtTotalQuantity.setText("Total quantity: " + totalQuantity);
        txtTotalPrice.setText(String.format("Total: %,.0f VND", totalPrice).replaceAll(",", "."));
    }

    private void updateSelectAllCheckbox() {
        if (cartDetails == null || cartDetails.isEmpty()) {
            isSelectAllChangingProgrammatically = true;
            checkboxall.setChecked(false);
            isSelectAllChangingProgrammatically = false;
            return;
        }

        boolean allSelected = true;
        for (ResCartDetailDTO detail : cartDetails) {
            Boolean isSelected = selectedMap.get(detail.getCartDetailsId());
            if (isSelected == null || !isSelected) {
                allSelected = false;
                break;
            }
        }

        isSelectAllChangingProgrammatically = true;
        checkboxall.setChecked(allSelected);
        isSelectAllChangingProgrammatically = false;
    }

    private void updateCartDetailQuantity(int cartDetailsId, int quantity, int cartId, int productId) {
        Pair<String, Integer> accessTokenWithUserId = SharedPref.getAccessTokenWithUserId(this);
        CartFetching apiService = APIClient.getClientWithToken(accessTokenWithUserId.first).create(CartFetching.class);

        ReqCartDetailDTO.Cart reqCart = new ReqCartDetailDTO.Cart();
        reqCart.setCartId(cartId);

        ReqCartDetailDTO.Product reqProduct = new ReqCartDetailDTO.Product();
        reqProduct.setProductId(productId);

        ReqCartDetailDTO request = new ReqCartDetailDTO(
                cartDetailsId,
                quantity,
                reqCart,
                reqProduct
        );

        apiService.updateCartDetailQuantity(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("CartActivity", "Quantity updated successfully");
                } else {
                    Log.e("CartActivity", "Failed to update quantity");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("CartActivity", "API error updating quantity: " + t.getMessage());
            }
        });
    }

}
