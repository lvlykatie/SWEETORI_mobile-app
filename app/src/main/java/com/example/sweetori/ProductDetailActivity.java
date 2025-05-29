package com.example.sweetori;


import android.annotation.SuppressLint;
import android.os.Bundle;
import com.bumptech.glide.Glide;

import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sweetori.adapter.ProductAdapter;
import com.example.sweetori.content.ProductFetching;
import com.example.sweetori.dto.request.ReqAddToCartDTO;
import com.example.sweetori.dto.response.ResCartDTO;
import com.example.sweetori.APIResponse;
import com.example.sweetori.dto.response.ResCartDetailDTO;
import com.example.sweetori.dto.response.ResOrderDetailDTO;
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
import java.util.Collections;
import java.util.List;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    // UI Components
    private ImageView btnAccount, btnHome, btnCart, btnNoti, btnVoucher;
    private ImageView productImage;
    private ImageButton btnIncrease, btnDecrease, imgbtn_heart, imgbtn_buynow, imgbtn_cart;
    private TextView productName, productPrice, productDesc, productBrand, quantity, productRating;
    private androidx.cardview.widget.CardView cardAddReview;
    private EditText inputComment;
    private Button btnSubmitReview;
    private RatingBar inputRatingBar;
    private RecyclerView recyclerReview;
    private ReviewAdapter reviewAdapter;
    private RatingBar productRatingBar, evaluateRatingBar;

    // Data
    private List<ResCartDetailDTO> cartDetails;
    private int cartId = -1;
    private ResProductDTO.ProductData productDt;
    private List<ResReviewDTO> reviewList = new ArrayList<>();
    private ResUserDTO currentUser;
    private int[] quantityValue = {1};
    private RecyclerView recyclerSimilarProduct;
    private ProductAdapter similarAdapter;
    // danh sách category và brand
    private static final String[] categories = {
            "Foundation", "Blush", "Lipstick", "Mascara", "Powder",
            "Concealer", "Highlighter", "Contour", "EyePalette",
            "Eyebrows", "Eyeliner", "Eyelashes", "LipPencil",
            "FixingSpray", "Brush", "Sponge"
    };
    private static final String[] brands = {
            "Judydoll", "Colorkey", "GlamrrQ", "IPKN",
            "Maybelline", "Merzy", "Blackrouge", "Romand", "Lemonade"
    };
    private static final int SIMILAR_LIMIT = 10;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);
        EdgeToEdge.enable(this);

        // Initialize user and UI
        currentUser = SharedPref.getUser(this);
        initializeViews();

        // Get product data from intent
        handleIntentData();

        // Setup navigation buttons
        setupNavigation();

        // Setup quantity controls
        setupQuantityControls();

        // Setup action buttons (cart, buy now, wishlist)
        setupActionButtons();

        // Load reviews if product is available
        if (productDt != null) {
            loadReviews(productDt.getProductId());
        }
    }

    private void initializeViews() {
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
        cardAddReview = findViewById(R.id.cardAddReview);
        inputComment = findViewById(R.id.inputComment);
        inputRatingBar = findViewById(R.id.inputRatingBar);
        btnSubmitReview = findViewById(R.id.btnSubmitReview);
        evaluateRatingBar = findViewById(R.id.evaluateRatingBar);

        recyclerReview.setHasFixedSize(true);
        recyclerReview.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new ReviewAdapter(reviewList, this);
        recyclerReview.setAdapter(reviewAdapter);
    }

    private void handleIntentData() {
        // Check if coming from order with review
        boolean fromOrder = getIntent().getBooleanExtra("from_order", false);
        ResOrderDetailDTO orderDetail = (ResOrderDetailDTO) getIntent().getSerializableExtra("order_detail");

        if (fromOrder && orderDetail != null) {
            cardAddReview.setVisibility(View.VISIBLE);
            btnSubmitReview.setOnClickListener(v -> {
                submitReview(orderDetail, inputRatingBar.getRating(), inputComment.getText().toString());
            });
        } else {
            cardAddReview.setVisibility(View.GONE);
        }

        // Get product data
        productDt = (ResProductDTO.ProductData) getIntent().getSerializableExtra("product");
        if (productDt != null) {
            showProductDetails(productDt);
            return;
        }

        // If no direct product data, try to get by ID
        int productId = getIntent().getIntExtra("productId", -1);
        if (productId != -1) {
            List<ResProductDTO.ProductData> cachedList = ResProductDTO.ProductDataManager.getInstance().getProductList();
            if (cachedList != null) {
                for (ResProductDTO.ProductData product : cachedList) {
                    if (product.getProductId() == productId) {
                        productDt = product;
                        showProductDetails(product);
                        break;
                    }
                }
            }
        }

        if (productDt == null) {
            Log.e("ProductDetail", "No valid product data received");
            Toast.makeText(this, "Product information not available", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupNavigation() {
        btnAccount.setOnClickListener(v -> {
            startActivity(new Intent(ProductDetailActivity.this, AccountActivity.class));
        });

        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(ProductDetailActivity.this, HomepageActivity.class));
        });

        btnCart.setOnClickListener(v -> {
            startActivity(new Intent(ProductDetailActivity.this, CartActivity.class));
        });

        btnNoti.setOnClickListener(v -> {
            startActivity(new Intent(ProductDetailActivity.this, NotiActivity.class));
        });

        btnVoucher.setOnClickListener(v -> {
            startActivity(new Intent(ProductDetailActivity.this, VoucherActivity.class));
        });
    }

    private void setupQuantityControls() {
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
    }

    private void setupActionButtons() {
        Pair<String, Integer> accessTokenWithUserId = SharedPref.getAccessTokenWithUserId(this);

        imgbtn_cart.setOnClickListener(v -> {
            if (productDt == null) return;
            addToCart(productDt.getProductId(), quantityValue[0], accessTokenWithUserId.first, () -> {
                // Optional callback after adding to cart
            });
        });

        imgbtn_buynow.setOnClickListener(v -> {
            if (productDt == null) return;
            addToCart(productDt.getProductId(), quantityValue[0], accessTokenWithUserId.first, this::proceedToCheckout);
        });

        imgbtn_heart.setOnClickListener(v -> {
            if (productDt == null || productDt.getProductId() == -1) {
                Toast.makeText(this, "Error: product does not exist", Toast.LENGTH_SHORT).show();
                return;
            }

            WishlistFetching apiService = APIClient.getClientWithToken(accessTokenWithUserId.first).create(WishlistFetching.class);
            Call<Void> call = apiService.addToWishlist(productDt.getProductId());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ProductDetailActivity.this, "Added to wishlist", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProductDetailActivity.this, "Failed to add to wishlist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(ProductDetailActivity.this, "Connection error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        // similar product
        recyclerSimilarProduct = findViewById(R.id.recyclerSimilarProduct);
        recyclerSimilarProduct.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        recyclerSimilarProduct.setLayoutManager(lm);

        // sau khi có productDt
        if (productDt != null) {
            showProductDetails(productDt);
            loadSimilarProducts(productDt);
        }
    }

    // phần similar product
    private void loadSimilarProducts(ResProductDTO.ProductData current) {
        APIClient.getClientWithToken(
                        SharedPref.getAccessTokenWithUserId(this).first
                ).create(ProductFetching.class)
                .getAllProducts()
                .enqueue(new Callback<APIResponse<ResProductDTO>>() {
                    @Override
                    public void onResponse(Call<APIResponse<ResProductDTO>> call,
                                           Response<APIResponse<ResProductDTO>> res) {
                        if (!res.isSuccessful() || res.body() == null) return;
                        List<ResProductDTO.ProductData> all = res.body().getData().getData();
                        List<ScoredProduct> scored = new ArrayList<>();

                        String curName = current.getProductName().toLowerCase();
                        String curBrand = current.getBrand();

                        for (ResProductDTO.ProductData p : all) {
                            if (p.getProductId() == current.getProductId()) continue;
                            int score = 0;
                            String name = p.getProductName().toLowerCase();
                            // +2 nếu chứa category
                            for (String cat : categories) {
                                if (name.contains(cat.toLowerCase())) {
                                    score += 2;
                                    break;
                                }
                            }
                            // +1 nếu cùng brand
                            if (curBrand.equalsIgnoreCase(p.getBrand())) {
                                score += 1;
                            }
                            if (score > 0) {
                                scored.add(new ScoredProduct(p, score));
                            }
                        }
                        // sắp xếp
                        Collections.sort(scored, (a, b) -> {
                            if (b.score != a.score)
                                return Integer.compare(b.score, a.score);
                            // tie-breaker: rating
                            return Double.compare(
                                    b.product.getAvgRate(), a.product.getAvgRate()
                            );
                        });
                        // lấy top N
                        List<ResProductDTO.ProductData> top = new ArrayList<>();
                        for (int i = 0; i < Math.min(SIMILAR_LIMIT, scored.size()); i++) {
                            top.add(scored.get(i).product);
                        }
                        // bind vào adapter
                        similarAdapter = new ProductAdapter(top, ProductDetailActivity.this);
                        recyclerSimilarProduct.setAdapter(similarAdapter);
                    }

                    @Override
                    public void onFailure(Call<APIResponse<ResProductDTO>> call, Throwable t) {
                        // log hoặc hiển thị lỗi nếu cần
                    }
                });

    }


    private void proceedToCheckout() {
        if (productDt == null || cartId == -1) return;

        Pair<String, Integer> accessTokenWithUserId = SharedPref.getAccessTokenWithUserId(this);
        List<String> filters = Arrays.asList("cart:" + cartId, "product:" + productDt.getProductId());
        CartFetching apiService = APIClient.getClientWithToken(accessTokenWithUserId.first).create(CartFetching.class);

        apiService.getCartDetail(filters).enqueue(new Callback<APIResponse<PaginationWrapper<ResCartDetailDTO>>>() {
            @Override
            public void onResponse(Call<APIResponse<PaginationWrapper<ResCartDetailDTO>>> call,
                                   Response<APIResponse<PaginationWrapper<ResCartDetailDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ResCartDetailDTO> detailList = response.body().getData().getData();
                    if (detailList != null && !detailList.isEmpty()) {
                        Intent intent = new Intent(ProductDetailActivity.this, AddToBagActivity.class);
                        intent.putExtra("selectedItems", new Gson().toJson(detailList));
                        startActivity(intent);
                    } else {
                        Log.e("CartActivity", "No cart details found");
                        Toast.makeText(ProductDetailActivity.this, "No items in cart", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("CartActivity", "Failed to fetch cart details");
                    Toast.makeText(ProductDetailActivity.this, "Failed to proceed to checkout", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse<PaginationWrapper<ResCartDetailDTO>>> call, Throwable t) {
                Log.e("CartActivity", "Error fetching cart details: " + t.getMessage());
                Toast.makeText(ProductDetailActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private static class ScoredProduct {
        ResProductDTO.ProductData product;
        int score;
        ScoredProduct(ResProductDTO.ProductData p, int s) {
            product = p; score = s;
        }
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

        productRatingBar.setRating((int)product.getAvgRate());
        evaluateRatingBar.setRating((int)product.getAvgRate());
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
                    }

                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse<ResCartDTO>> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Connection error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitReview(ResOrderDetailDTO orderDetail, float rate, String content) {
        String accessToken = SharedPref.getAccessTokenWithUserId(this).first;
        if (accessToken == null) {
            Toast.makeText(this, "Please login again", Toast.LENGTH_SHORT).show();
            return;
        }

        ReqReviewDTO reviewDTO = new ReqReviewDTO();
        reviewDTO.setRate(rate);
        reviewDTO.setFeedback(content);

        FeedbackFetching apiService = APIClient.getClientWithToken(accessToken).create(FeedbackFetching.class);

        Call<APIResponse<ResReviewDTO>> call = apiService.addFeedback(
                orderDetail.getProduct().getProductId(), // productId as path parameter
                reviewDTO // request body
        );

        call.enqueue(new Callback<APIResponse<ResReviewDTO>>() {
            @Override
            public void onResponse(Call<APIResponse<ResReviewDTO>> call, Response<APIResponse<ResReviewDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ProductDetailActivity.this, "Review submitted successfully", Toast.LENGTH_SHORT).show();
                    cardAddReview.setVisibility(View.GONE);
                    loadReviews(orderDetail.getProduct().getProductId());
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Failed to submit review", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse<ResReviewDTO>> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadReviews(int productId) {
        FeedbackFetching apiService = APIClient.getClient().create(FeedbackFetching.class);
        String filter = "product:" + productId;

        apiService.getFeedback(filter).enqueue(new Callback<APIResponse<PaginationWrapper<ResReviewDTO>>>() {
            @Override
            public void onResponse(Call<APIResponse<PaginationWrapper<ResReviewDTO>>> call,
                                   Response<APIResponse<PaginationWrapper<ResReviewDTO>>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getData() != null) {
                        List<ResReviewDTO> reviews = response.body().getData().getData();
                        if (reviews != null) {
                            reviewAdapter.updateReviewList(reviews);
                            setRecyclerViewHeightBasedOnChildren(recyclerReview);
                        } else {
                            Log.e("API_ERROR", "Reviews list is null");
                            reviewAdapter.updateReviewList(new ArrayList<>()); // Show empty list
                        }
                    } else {
                        Log.e("API_ERROR", "Response body or data is null");
                        reviewAdapter.updateReviewList(new ArrayList<>()); // Show empty list
                    }
                } else {
                    Log.e("API_ERROR", "Unsuccessful response: " + response.code());
                    Toast.makeText(ProductDetailActivity.this, "Failed to load reviews", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse<PaginationWrapper<ResReviewDTO>>> call, Throwable t) {
                Log.e("API_ERROR", "Error loading reviews", t);
                Toast.makeText(ProductDetailActivity.this, "Error loading reviews", Toast.LENGTH_SHORT).show();
                reviewAdapter.updateReviewList(new ArrayList<>()); // Show empty list on failure
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