package com.example.sweetori.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.sweetori.ProductDetailActivity;
import com.example.sweetori.R;
import com.example.sweetori.dto.response.ResWishListDTO;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {

    private List<ResWishListDTO> wishList;
    private OnItemClickListener onItemClickListener;
    private OnDeleteClickListener onDeleteClickListener;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(ResWishListDTO item, int position);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(ResWishListDTO item, int position);
    }

    public WishListAdapter(List<ResWishListDTO> wishList,
                           OnItemClickListener onItemClickListener,
                           OnDeleteClickListener onDeleteClickListener,
                           Context context) {
        this.wishList = wishList;
        this.onItemClickListener = onItemClickListener;
        this.onDeleteClickListener = onDeleteClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public WishListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wishlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListAdapter.ViewHolder holder, int position) {
        ResWishListDTO item = wishList.get(position);

        holder.productName.setText(item.getProduct().getProductName());
        holder.productPrice.setText(String.format(Locale.getDefault(), "%,.0f VND", item.getProduct().getSellingPrice()));

        Glide.with(holder.itemView.getContext())
                .load(item.getProduct().getImage())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                .into(holder.productImage);

        holder.itemView.setOnClickListener(v -> {
            onItemClickListener.onItemClick(item, position);
        });

        holder.ic_delete.setVisibility(isEditMode ? View.VISIBLE : View.GONE);

        // Xử lý nút xóa
        holder.ic_delete.setOnClickListener(v -> {
            onDeleteClickListener.onDeleteClick(item, position);
        });
    }

    @Override
    public int getItemCount() {
        return wishList.size();
    }

    private boolean isEditMode = false;

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        wishList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, wishList.size());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage, ic_delete;
        TextView productName, productPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            ic_delete = itemView.findViewById(R.id.ic_delete); // Nút xóa
        }
    }
}
