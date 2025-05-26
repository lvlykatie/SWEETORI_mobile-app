package com.example.sweetori.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.sweetori.ProductDetailActivity;
import com.example.sweetori.R;
import com.example.sweetori.dto.response.ResOrderDetailDTO;
import com.example.sweetori.dto.response.ResProductDTO;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {
    private List<ResOrderDetailDTO> listOfOrderdetails;
    private Context context;

    public OrderItemAdapter(List<ResOrderDetailDTO> listOfOrderdetails, Context context) {
        this.listOfOrderdetails = listOfOrderdetails;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_order, parent, false);
        return new OrderItemAdapter.OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        if (position >= 0 && position < listOfOrderdetails.size()) {
            final ResOrderDetailDTO orderItem = listOfOrderdetails.get(position);
            final ResProductDTO.ProductData product = (ResProductDTO.ProductData) orderItem.getProduct();// final ở đây

            // Set text for product name
            holder.tvProductName.setText(product.getProductName() != null ? product.getProductName() : "No name");

            if (product.getProductName().length() > 30) {
                holder.tvProductName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            } else {
                holder.tvProductName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            }

            holder.tvProductPrice.setText(String.format("%,.0f VND", product.getSellingPrice()));
            holder.tvQuantity.setText(String.format("%.1f", orderItem.getQuantity()));

            if (product.getImage() != null && !product.getImage().isEmpty()) {
                Glide.with(context)
                        .load(product.getImage())
                        .placeholder(R.drawable.moimahong)
                        .error(R.drawable.moimahong)
                        .into(holder.ivProductImage);
            } else {
                holder.ivProductImage.setImageResource(R.drawable.moimahong);
            }

        }
    }
    @Override
    public int getItemCount () {
        return listOfOrderdetails != null ? listOfOrderdetails.size() : 0;
    }
    public void updateOrderItemList(List<ResOrderDetailDTO> newList) {
        if (newList != null) {
            listOfOrderdetails.clear();
            listOfOrderdetails.addAll(newList);
            notifyDataSetChanged();
            Log.d("OrderAdapter", "Updated list with " + newList.size() + " items");
        } else {
            Log.e("OrderAdapter", "New list is null");
        }
    }
    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        public TextView tvProductName;
        public TextView tvProductPrice;
        public TextView tvQuantity;
        public ImageView ivProductImage;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
        }
    }
}