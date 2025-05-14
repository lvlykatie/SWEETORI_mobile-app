package com.example.sweetori.adapter;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sweetori.R;
import com.example.sweetori.dto.response.ResProductDTO;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<ResProductDTO.ProductData> productList;
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
            ResProductDTO.ProductData product = productList.get(position);

            // Set text for product name
            holder.productName.setText(product.getTitle() != null ? product.getTitle() : "No name");

            if (product.getTitle().length() > 30) { // Ngưỡng số ký tự
                holder.productName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10); // Giảm size
            } else {
                holder.productName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12); // Size mặc định
            }
            // Format price with currency
            holder.productPrice.setText(String.format("%,.0f VND", product.getSellingPrice()));

            // Set rating
            holder.productRate.setText(String.format("%.1f", product.getAvgRate()));

            // Load image using Glide
            if (product.getImage() != null && !product.getImage().isEmpty()) {
                Glide.with(context)
                        .load(product.getImage())
                        .placeholder(R.drawable.moimahong)
                        .error(R.drawable.moimahong)
                        .into(holder.productImage);
            } else {
                holder.productImage.setImageResource(R.drawable.moimahong);
            }
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
        TextView productName, productPrice, productRate;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productRate = itemView.findViewById(R.id.productRate);
        }
    }
}