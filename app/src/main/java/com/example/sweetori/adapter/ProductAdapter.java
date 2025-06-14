package com.example.sweetori.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sweetori.CartActivity;
import com.example.sweetori.ProductActivity;
import com.example.sweetori.R;
import com.example.sweetori.content.CartFetching;
import com.example.sweetori.dto.response.ResProductDTO;
import com.example.sweetori.ProductDetailActivity;
import com.example.sweetori.APIClient;
import com.example.sweetori.SharedPref;
import com.example.sweetori.content.WishlistFetching;
import com.example.sweetori.dto.request.ReqWishlistDTO;
import com.example.sweetori.APIResponse;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<ResProductDTO.ProductData> productList;
    private ResProductDTO.ProductData clickedProduct;
    private Context context;

    // Constructor
    public ProductAdapter(List<ResProductDTO.ProductData> productList, Context context) {
        this.productList = productList != null ? productList : new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        if (position >= 0 && position < productList.size()) {
            final ResProductDTO.ProductData[] product = {productList.get(position)};  // final ở đây

            // Set text for product name
            holder.productName.setText(product[0].getProductName() != null ? product[0].getProductName() : "No name");
            if (product[0].getDiscount() != null
                    && product[0].getDiscount().getDiscountPercentage() > 0) {

                double percentage = product[0].getDiscount().getDiscountPercentage() * 100;
                holder.productDiscount.setVisibility(View.VISIBLE);
                holder.productDiscount.setText(String.format("%.0f%%", percentage));
            } else {
                holder.productDiscount.setVisibility(View.GONE);
            }


            if (product[0].getProductName().length() > 30) {
                holder.productName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            } else {
                holder.productName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            }

            holder.productPrice.setText(String.format("%,.0f VND", product[0].getSellingPrice()));
            holder.productRate.setText(String.format("%.1f", product[0].getAvgRate()));

            if (product[0].getImage() != null && !product[0].getImage().isEmpty()) {
                Glide.with(context)
                        .load(product[0].getImage())
                        .placeholder(R.drawable.moimahong)
                        .error(R.drawable.moimahong)
                        .into(holder.productImage);
            } else {
                holder.productImage.setImageResource(R.drawable.moimahong);
            }

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                clickedProduct = productList.get(holder.getAdapterPosition());
                intent.putExtra("product", clickedProduct);
                context.startActivity(intent);
            });

            holder.imgHeart.setOnClickListener(v -> {
                int productId = product[0].getProductId();
                Pair<String, Integer> accessTokenWithUserId = SharedPref.getAccessTokenWithUserId(v.getContext());

                WishlistFetching apiService = APIClient.getClientWithToken(accessTokenWithUserId.first).create(WishlistFetching.class);
                Call<Void> call = apiService.addToWishlist(productId);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Added to wishlist", Toast.LENGTH_SHORT).show();
                            holder.imgHeart.setImageResource(R.drawable.heart);
                        } else {
                            Toast.makeText(context, "Failse: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Connection error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
    }


    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    // Method to update the product list
    public void updateProductList(List<ResProductDTO.ProductData> newList) {
        if (newList != null) {
            productList.clear();
            productList.addAll(newList);
            notifyDataSetChanged();
            Log.d("ProductAdapter", "Updated list with " + newList.size() + " items");
        } else {
            Log.e("ProductAdapter", "New list is null");
        }
    }

    // ViewHolder class
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, productRate, productDiscount;
        ImageView imgHeart;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productRate = itemView.findViewById(R.id.productRate);
            productDiscount = itemView.findViewById(R.id.productDiscount);
            imgHeart = itemView.findViewById(R.id.imgHeart);

        }
    }
}