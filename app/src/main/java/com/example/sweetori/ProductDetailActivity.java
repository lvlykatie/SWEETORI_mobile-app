package com.example.sweetori;

import android.annotation.SuppressLint;
import android.os.Bundle;
import com.bumptech.glide.Glide;


import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sweetori.dto.request.ReqAddToCartDTO;
import com.example.sweetori.dto.response.ResCartDTO;
import com.example.sweetori.APIResponse;
import com.example.sweetori.dto.response.ResCartDetailDTO;
import com.example.sweetori.dto.response.ResProductDTO;
import com.example.sweetori.dto.request.ReqReviewDTO;
import com.example.sweetori.APIClient;
import com.example.sweetori.dto.request.ReqCartDetailDTO;
import com.example.sweetori.content.CartFetching;
import com.example.sweetori.SharedPref;
import com.example.sweetori.content.FeedbackFetching;
import com.example.sweetori.adapter.ReviewAdapter;
import com.example.sweetori.dto.response.ResReviewDTO;
import com.example.sweetori.content.WishlistFetching;
import com.example.sweetori.dto.request.ReqWishlistDTO;
import com.example.sweetori.APIResponse;
import com.example.sweetori.dto.response.PaginationWrapper;
import com.example.sweetori.dto.response.ResUserDTO;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView btnAccount;
    ImageView btnHome;
    ImageView btnCart;
    ImageView btnNoti;
    ImageView btnVoucher;
    private ImageView productImage;
    private ImageButton imgbtnCart, btnIncrease, btnDecrease, imgbtn_heart, imgbtn_buynow, imgbtn_cart;
    private TextView productName, productPrice, productDesc, productBrand, quantity, productRating;
    private RecyclerView recyclerReview;
    private ReviewAdapter reviewAdapter;
    private RatingBar productRatingBar;
    private List<ResCartDetailDTO> cartDetails;
    private int cartId = -1;
    ResProductDTO.ProductData productDt;
    private List<ResReviewDTO> reviewList = new ArrayList<>();
    private ResUserDTO currentUser;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);
        EdgeToEdge.enable(this);
        currentUser = SharedPref.getUser(this);

        //Component
        btnAccount = findViewById(R.id.btnAccount);
        btnHome = findViewById(R.id.btnHome);
        btnCart = findViewById(R.id.btnCart);
        btnNoti = findViewById(R.id.btnNoti);
        btnVoucher = findViewById(R.id.btnVoucher1);
        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productDesc = findViewById(R.id.productDesc);
        productBrand = findViewById(R.id.brand);
        quantity = findViewById(R.id.quantity);
        btnIncrease = findViewById(R.id.imgbtn_increase);
        btnDecrease = findViewById(R.id.imgbtn_decrease);
        imgbtn_heart = findViewById(R.id.imgbtn_heart);
        imgbtn_buynow = findViewById(R.id.imgbtn_buynow);
        imgbtn_cart = findViewById(R.id.imgbtn_cart);
        productRatingBar = findViewById(R.id.productRatingBar);
        productRating = findViewById(R.id.productRating);
        recyclerReview = findViewById(R.id.recyclerReview);
        recyclerReview.setHasFixedSize(true);
        recyclerReview.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new ReviewAdapter(reviewList, this);
        recyclerReview.setAdapter(reviewAdapter);
        Pair<String, Integer> accessTokenWithUserId = SharedPref.getAccessTokenWithUserId(ProductDetailActivity.this);
        cartDetails = new ArrayList<>();

        //Intent
        btnAccount.setOnClickListener(v ->

        {
            Intent account = new Intent(ProductDetailActivity.this, AccountActivity.class);
            startActivity(account);
        });
        btnHome.setOnClickListener(v ->
        {
            Intent home = new Intent(ProductDetailActivity.this, HomepageActivity.class);
            startActivity(home);
        });

        btnCart.setOnClickListener(v ->
        {
            Intent cart = new Intent(ProductDetailActivity.this, CartActivity.class);
            startActivity(cart);
        });

        btnNoti.setOnClickListener(v ->
        {
            Intent noti = new Intent(ProductDetailActivity.this, NotiActivity.class);
            startActivity(noti);
        });
        btnVoucher.setOnClickListener(v ->

        {
            Intent voucher = new Intent(ProductDetailActivity.this, VoucherActivity.class);
            startActivity(voucher);
        });

        //Lấy thông tin product từ đây
        productDt = (ResProductDTO.ProductData) getIntent().getSerializableExtra("product");
        if (productDt != null) {
            showProductDetails(productDt);
        } else {
            Log.e("ProductDetail", "No product data received");
        }

        int[] quantityValue = {1};

        quantity.setText(String.valueOf(quantityValue[0]));

        btnIncrease.setOnClickListener(v -> {
            quantityValue[0]++;
            quantity.setText(String.valueOf(quantityValue[0]));
        });

        btnDecrease.setOnClickListener(v -> {
            if (quantityValue[0] > 1) {
                quantityValue[0]--;
                quantity.setText(String.valueOf(quantityValue[0]));
            }
        });

        imgbtn_cart.setOnClickListener(v -> {
            addToCart(productDt.getProductId(), quantityValue[0], accessTokenWithUserId.first, () -> {});
        });

        imgbtn_buynow.setOnClickListener(v -> {
            addToCart(productDt.getProductId(), quantityValue[0], accessTokenWithUserId.first, () -> {
                List<String> filters = Arrays.asList("cart:" + cartId, "product:" + productDt.getProductId());
                CartFetching api_Service = APIClient.getClientWithToken(accessTokenWithUserId.first).create(CartFetching.class);

                api_Service.getCartDetail(filters).enqueue(new Callback<APIResponse<PaginationWrapper<ResCartDetailDTO>>>() {
                    @Override
                    public void onResponse(Call<APIResponse<PaginationWrapper<ResCartDetailDTO>>> call, Response<APIResponse<PaginationWrapper<ResCartDetailDTO>>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<ResCartDetailDTO> detailList = response.body().getData().getData();
                            if (detailList != null && !detailList.isEmpty()) {
                                cartDetails.clear();
                                cartDetails.addAll(detailList); // 👈 Add cả danh sách
                                Log.d("DEBUG_CART", "Cart details size: " + cartDetails.size());
                                Log.d("DEBUG_CART", "First item: " + new Gson().toJson(detailList.get(0)));

                                Intent intent = new Intent(ProductDetailActivity.this, AddToBagActivity.class);
                                intent.putExtra("selectedItems", new Gson().toJson(cartDetails));
                                startActivity(intent);
                            } else {
                                Log.e("CartActivity", "No cart details found");
                            }
                        } else {
                            Log.e("CartActivity", "Failed to fetch cart details");
                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponse<PaginationWrapper<ResCartDetailDTO>>> call, Throwable t) {
                        Log.e("CartActivity", "Error fetching cart details: " + t.getMessage());
                    }
                });

            });
        });


        imgbtn_heart.setOnClickListener(v -> {
            if (productDt.getProductId() == -1) {
                Toast.makeText(this, "Error: product does not exist", Toast.LENGTH_SHORT).show();
                return;
            }

            WishlistFetching api_Service = APIClient.getClientWithToken(accessTokenWithUserId.first).create(WishlistFetching.class);

            Call<Void> call = api_Service.addToWishlist(productDt.getProductId());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ProductDetailActivity.this, "Added to wishlist", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProductDetailActivity.this, "Failed: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(ProductDetailActivity.this, "Connection error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void showProductDetails(ResProductDTO.ProductData product) {
        productName.setText(product.getProductName());
        productPrice.setText(String.format("%,.0f VND", product.getSellingPrice()));
        productBrand.setText(String.format("Brand: %s", product.getBrand()));
        productDesc.setText(product.getDescriptionDetails() + "\n" + product.getDescription());

        if (product.getImage() != null && !product.getImage().isEmpty()) {
            Glide.with(this)
                    .load(product.getImage())
                    .placeholder(R.drawable.moimahong)
                    .into(productImage);
        } else {
            productImage.setImageResource(R.drawable.moimahong);
        }
        productRatingBar.setRating((float) product.getAvgRate());
        productRating.setText(String.format("%.1f", product.getAvgRate()));

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
                    Toast.makeText(ProductDetailActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();

                    ResCartDTO cart = response.body().getData();
                    if (cart != null) {
                        cartId = cart.getCartId();
                        Log.d("ProductDetail", "cartDetailId: " + cartId);
                    }

                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse<ResCartDTO>> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Connection error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




    public static void setRecyclerViewHeightBasedOnChildren(RecyclerView recyclerView) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null) return;

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), View.MeasureSpec.UNSPECIFIED);

        for (int i = 0; i < adapter.getItemCount(); i++) {
            RecyclerView.ViewHolder holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i));
            adapter.bindViewHolder(holder, i);
            holder.itemView.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += holder.itemView.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.height = totalHeight;
        recyclerView.setLayoutParams(params);
        recyclerView.requestLayout();
    }

}