package com.example.sweetori.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.sweetori.R;
import com.example.sweetori.dto.response.ResProductDTO;

import java.util.List;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.ViewHolder> {

    private List<ResProductDTO.ProductData> notifyProduct;
    private OnItemClickListener listener;


    public NotifyAdapter(List<ResProductDTO.ProductData> notifyProduct) {
        this.notifyProduct = notifyProduct;
    }
    public interface OnItemClickListener {
        void onItemClick(ResProductDTO.ProductData product);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notify, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResProductDTO.ProductData product = notifyProduct.get(position);
        double discount = product.getDiscount().getDiscountPercentage()*100;
        int discountInt = (int) Math.round(discount);

        holder.productName.setText(product.getProductName());
        holder.notifyProduct.setText("This item has discount: " + discountInt + "%");
        Glide.with(holder.itemView.getContext())
                .load(product.getImage())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                .into(holder.productImage);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(product);
            }
        });

    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public int getItemCount() {
        return notifyProduct.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, notifyProduct, time;
        ImageView productImage;

        public ViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            notifyProduct = itemView.findViewById(R.id.notify_product);
            productImage = itemView.findViewById(R.id.notifyImage);
            time = itemView.findViewById(R.id.time);
        }
    }
}

