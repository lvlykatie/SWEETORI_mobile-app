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
import com.example.sweetori.dto.response.ResCartDetailDTO;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CartDetailAdapter extends RecyclerView.Adapter<CartDetailAdapter.ViewHolder> {

    private List<ResCartDetailDTO> cartDetails;
    private OnItemClickListener onItemClickListener;
    private SelectAllCheckboxListener selectAllCheckboxListener;

    // Map trạng thái chọn: key = cartDetailsId, value = isSelected
    private Map<Integer, Boolean> selectedMap;

    public interface OnItemClickListener {
        void onDeleteClick(ResCartDetailDTO item, int position);
        void onQuantityChanged();
        void onItemSelectedChanged(int cartDetailId, boolean isSelected);
    }

    public interface SelectAllCheckboxListener {
        void onSelectAllChecked(boolean isAllSelected);
    }

    public CartDetailAdapter(List<ResCartDetailDTO> cartDetails,
                             OnItemClickListener listener,
                             SelectAllCheckboxListener selectAllCheckboxListener,
                             Map<Integer, Boolean> selectedMap) {
        this.cartDetails = cartDetails;
        this.onItemClickListener = listener;
        this.selectAllCheckboxListener = selectAllCheckboxListener;
        this.selectedMap = selectedMap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_1, parent, false);
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
                .into(holder.productImage);

        holder.icDelete.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onDeleteClick(item, holder.getAdapterPosition());
            }
        });

        holder.plusBtn.setOnClickListener(v -> {
            int quantity = item.getQuantity() + 1;
            item.setQuantity(quantity);
            holder.quantityText.setText(String.valueOf(quantity));
            if (onItemClickListener != null) {
                onItemClickListener.onQuantityChanged();
            }
        });

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

        // Tránh listener cũ gây lỗi khi tái sử dụng view
        holder.checkboxSelect.setOnCheckedChangeListener(null);

        // Set trạng thái checkbox dựa trên selectedMap
        Boolean isSelected = selectedMap.get(item.getCartDetailsId());
        holder.checkboxSelect.setChecked(isSelected != null && isSelected);

        // Bắt sự kiện check change, gọi callback cập nhật map bên activity
        holder.checkboxSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemSelectedChanged(item.getCartDetailsId(), isChecked);
            }

            // Kiểm tra tất cả item đã chọn chưa để cập nhật checkbox "select all"
            boolean allSelected = true;
            for (ResCartDetailDTO detail : cartDetails) {
                Boolean selected = selectedMap.get(detail.getCartDetailsId());
                if (selected == null || !selected) {
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
        for (ResCartDetailDTO item : cartDetails) {
            selectedMap.put(item.getCartDetailsId(), isSelected);
        }
        notifyDataSetChanged();
        if (onItemClickListener != null) {
            onItemClickListener.onQuantityChanged();
        }
    }

    @Override
    public int getItemCount() {
        return cartDetails.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage, icDelete;
        TextView productName, productPrice, quantityText;
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
