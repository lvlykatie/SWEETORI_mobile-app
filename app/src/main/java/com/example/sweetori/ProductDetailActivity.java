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
import com.example.sweetori.dto.response.ResProductDTO;
import com.example.sweetori.dto.request.ReqReviewDTO;
import com.example.sweetori.APIClient;
import com.example.sweetori.dto.request.ReqCartDetailDTO;
import com.example.sweetori.content.CartFetching;
import com.example.sweetori.SharedPref;
import com.example.sweetori.dto.request.ReqCartDTO;
import com.example.sweetori.content.FeedbackFetching;
import com.example.sweetori.adapter.ReviewAdapter;
import com.example.sweetori.dto.response.ResReviewDTO;
import com.example.sweetori.content.WishlistFetching;
import com.example.sweetori.dto.request.ReqWishlistDTO;
import com.example.sweetori.APIResponse;
import com.example.sweetori.dto.response.PaginationWrapper;

import java.util.ArrayList;
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
    private ImageButton imgbtnCart, btnIncrease, btnDecrease, imgbtn_heart;
    private TextView productName, productPrice, productDesc, productBrand, quantity, productRating;
    private RecyclerView recyclerReview;
    private ReviewAdapter reviewAdapter;
    private RatingBar productRatingBar;

    private List<ResReviewDTO> reviewList = new ArrayList<>();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);
        EdgeToEdge.enable(this);

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
        imgbtnCart = findViewById(R.id.imgbtn_cart);
        productBrand = findViewById(R.id.brand);
        quantity = findViewById(R.id.quantity);
        btnIncrease = findViewById(R.id.imgbtn_increase);
        btnDecrease = findViewById(R.id.imgbtn_decrease);
        imgbtn_heart = findViewById(R.id.imgbtn_heart);
        productRatingBar = findViewById(R.id.productRatingBar);
        productRating = findViewById(R.id.productRating);
        recyclerReview = findViewById(R.id.recyclerReview);
        recyclerReview.setHasFixedSize(true);
        recyclerReview.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new ReviewAdapter(reviewList, this);
        recyclerReview.setAdapter(reviewAdapter);
        Pair<String, Integer> accessTokenWithUserId = SharedPref.getAccessTokenWithUserId(ProductDetailActivity.this);


        //Intent
        btnAccount.setOnClickListener(v ->

        {
            Intent account = new Intent(ProductDetailActivity.this, AccountActivity.class);
            startActivity(account);
        });
        btnHome.setOnClickListener(v ->

        {
            Intent home = new Intent(ProductDetailActivity.this, MainActivity.class);
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


        int productId = getIntent().getIntExtra("productId", -1);

        if (productId != -1) {
            // Tìm sản phẩm trong danh sách đã cache
            List<ResProductDTO.ProductData> cachedList = ResProductDTO.ProductDataManager.getInstance().getProductList();
            if (cachedList != null) {
                for (ResProductDTO.ProductData product : cachedList) {
                    if (product.getProductId() == productId) {
                        showProductDetails(product);
                        break;
                    }
                }
            }
        } else {
            Log.e("ProductDetail", "Invalid productId received");
        }

        FeedbackFetching apiService = APIClient.getClient().create(FeedbackFetching.class);
        String filter = "product:" + productId;
        apiService.getFeedback(filter).enqueue(new Callback<APIResponse<PaginationWrapper<ResReviewDTO>>>() {
            @Override
            public void onResponse(Call<APIResponse<PaginationWrapper<ResReviewDTO>>> call, Response<APIResponse<PaginationWrapper<ResReviewDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    APIResponse<PaginationWrapper<ResReviewDTO>> apiResponse = response.body();
                    PaginationWrapper<ResReviewDTO> pagination = apiResponse.getData();
                    if (pagination != null) {
                        List<ResReviewDTO> reviewListResponse = pagination.getData();
                        if (reviewListResponse != null && !reviewListResponse.isEmpty()) {
                            reviewAdapter.updateReviewList(reviewListResponse);
                            // Gọi hàm set chiều cao cho RecyclerView ở đây
                            setRecyclerViewHeightBasedOnChildren(recyclerReview);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse<PaginationWrapper<ResReviewDTO>>> call, Throwable t) {
                Log.e("API_ERROR", "Error loading reviews", t);
                Toast.makeText(ProductDetailActivity.this, "Error loading reviews: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


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
        imgbtnCart.setOnClickListener(v -> {
            if (productId == -1) {
                Toast.makeText(this, "Error: product does not exist", Toast.LENGTH_SHORT).show();
                return;
            }


            CartFetching api = APIClient.getClientWithToken(accessTokenWithUserId.first).create(CartFetching.class);
            ReqCartDetailDTO request_product = new ReqCartDetailDTO(productId, quantityValue[0]);

            Call<Void> call = api.addCartDetail(request_product);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ProductDetailActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProductDetailActivity.this, "False: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(ProductDetailActivity.this, "Connection error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        imgbtn_heart.setOnClickListener(v -> {
            if (productId == -1) {
                Toast.makeText(this, "Error: product does not exist", Toast.LENGTH_SHORT).show();
                return;
            }

            WishlistFetching api_Service = APIClient.getClientWithToken(accessTokenWithUserId.first).create(WishlistFetching.class);

            Call<Void> call = api_Service.addToWishlist(productId);
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