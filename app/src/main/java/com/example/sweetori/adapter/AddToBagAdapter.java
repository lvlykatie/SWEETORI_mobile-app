package com.example.sweetori.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.sweetori.R;
import com.example.sweetori.dto.response.ResCartDTO;
import com.example.sweetori.dto.response.ResCartDetailDTO;
import com.example.sweetori.dto.response.ResDiscountDTO;

import java.util.List;
import java.util.Locale;

public class AddToBagAdapter extends RecyclerView.Adapter<AddToBagAdapter.ViewHolder> {

    private List<ResCartDetailDTO> cartDetails;
    private OnItemClickListener onItemClickListener;
    private List<ResDiscountDTO> discounts;

    public interface OnItemClickListener {
        void onQuantityChanged();
    }

    public AddToBagAdapter(List<ResCartDetailDTO> cartDetails, OnItemClickListener listener) {
        this.cartDetails = cartDetails;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item mới bạn gửi (ví dụ tên file: item_product_2.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResCartDetailDTO item = cartDetails.get(position);

        holder.productName.setText(item.getProduct().getProductName());
        holder.productPrice.setText(String.format(Locale.getDefault(), "%,.0f VND", item.getProduct().getSellingPrice()));
        holder.quantityText.setText(String.valueOf(item.getQuantity()));

        int radius = 20;

        Glide.with(holder.itemView.getContext())
                .load(item.getProduct().getImage())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(radius)))
                .into(holder.productImag);

        // Tăng số lượng
        holder.plusBtn.setOnClickListener(v -> {
            int quantity = item.getQuantity() + 1;
            item.setQuantity(quantity);
            holder.quantityText.setText(String.valueOf(quantity));
            if (onItemClickListener != null) {
                onItemClickListener.onQuantityChanged();
            }
        });

        // Giảm số lượng (tối thiểu là 1)
        holder.minusBtn.setOnClickListener(v -> {
            int quantity = item.getQuantity();
            if (quantity > 1) {
                quantity--;
                item.setQuantity(quantity);
                holder.quantityText.setText(String.valueOf(quantity));
                if (onItemClickListener != null) {
                    onItemClickListener.onQuantityChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartDetails.size();
    }
    public List<ResCartDetailDTO> getCartDetails() {
        return cartDetails;
    }

    public void setDiscounts(List<ResDiscountDTO> discounts) {
        this.discounts = discounts;
    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImag;
        TextView productName, productPrice, quantityText;
        Button minusBtn, plusBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImag = itemView.findViewById(R.id.productImag);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            quantityText = itemView.findViewById(R.id.quantityText);
            minusBtn = itemView.findViewById(R.id.minusBtn);
            plusBtn = itemView.findViewById(R.id.plusBtn);
        }
    }
}
