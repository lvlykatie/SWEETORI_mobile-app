package com.example.sweetori.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sweetori.R;
import com.example.sweetori.dto.response.ResCartDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartDetailAdapter extends RecyclerView.Adapter<CartDetailAdapter.ViewHolder> {

    private List<ResCartDTO.CartDetail> cartDetails;
    private OnItemClickListener onItemClickListener;
    private SelectAllCheckboxListener selectAllCheckboxListener;


    // Interface để callback khi xoá hoặc thay đổi số lượng
    public interface OnItemClickListener {
        void onDeleteClick(ResCartDTO.CartDetail item, int position);
        void onQuantityChanged();
    }

    public interface SelectAllCheckboxListener {
        void onSelectAllChecked(boolean isAllSelected);
    }

    public CartDetailAdapter(List<ResCartDTO.CartDetail> cartDetails, OnItemClickListener listener, SelectAllCheckboxListener selectAllCheckboxListener) {
        this.cartDetails = cartDetails;
        this.onItemClickListener = listener;
        this.selectAllCheckboxListener = selectAllCheckboxListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResCartDTO.CartDetail item = cartDetails.get(position);

        holder.productName.setText(item.getProduct().getProductName());
        holder.productPrice.setText(String.format(Locale.getDefault(), "%, .0f VND", item.getProduct().getSellingPrice()));
        holder.quantityText.setText(String.valueOf(item.getQuantity()));

        int radius = 20;

        Glide.with(holder.itemView.getContext())
                .load(item.getProduct().getImage())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(radius)))
                .into(holder.productImage);


        // Xoá sản phẩm
        holder.icDelete.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onDeleteClick(item, holder.getAdapterPosition());
            }
        });


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

        holder.checkboxSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setSelected(isChecked);
            if (onItemClickListener != null) {
                onItemClickListener.onQuantityChanged();
            }

            // Đồng bộ lại trạng thái checkboxSelectAll nếu cần
            boolean allSelected = true;
            for (ResCartDTO.CartDetail detail : cartDetails) {
                if (!detail.isSelected()) {
                    allSelected = false;
                    break;
                }
            }
            if (selectAllCheckboxListener != null) {
                selectAllCheckboxListener.onSelectAllChecked(allSelected);
            }
        });


    }

    public void removeItem(int position) {
        cartDetails.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, cartDetails.size());
    }

    public void setAllSelected(boolean isSelected) {
        for (ResCartDTO.CartDetail item : cartDetails) {
            item.setSelected(isSelected);
        }
        notifyDataSetChanged();
        if (onItemClickListener != null) {
            onItemClickListener.onQuantityChanged();
        }
    }


    public List<ResCartDTO.CartDetail> getSelectedItems() {
        List<ResCartDTO.CartDetail> selected = new ArrayList<>();
        for (ResCartDTO.CartDetail item : cartDetails) {
            if (item.isSelected()) {
                selected.add(item);
            }
        }
        return selected;
    }



    @Override
    public int getItemCount() {
        return cartDetails.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage, icDelete;
        TextView productName, productDesc, productPrice, quantityText;
        Button minusBtn, plusBtn;
        CheckBox checkboxSelect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImag);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            icDelete = itemView.findViewById(R.id.ic_delete);
            minusBtn = itemView.findViewById(R.id.minusBtn);
            plusBtn = itemView.findViewById(R.id.plusBtn);
            quantityText = itemView.findViewById(R.id.quantityText);
            checkboxSelect = itemView.findViewById(R.id.itemCheckBox);
        }
    }
}
