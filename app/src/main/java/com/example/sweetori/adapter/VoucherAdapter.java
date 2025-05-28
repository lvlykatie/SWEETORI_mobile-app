package com.example.sweetori.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweetori.R;
import com.example.sweetori.dto.response.ResVoucherDTO;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder> {
    private List<ResVoucherDTO.VoucherData> voucherList;
    private Context context;

    public VoucherAdapter(Context context, List<ResVoucherDTO.VoucherData> voucherList) {
        this.context = context;
        this.voucherList = voucherList;
    }

    @NonNull
    @Override
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_voucher, parent, false);
        return new VoucherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        ResVoucherDTO.VoucherData voucher = voucherList.get(position);
        holder.tvDiscount.setText("Giảm " + voucher.getDiscountAmount().intValue() + "đ");
        holder.tvDetail.setText("Mã: " + voucher.getCode());
        holder.tvDate.setText("HSD: " + voucher.getValidTo());
    }

    @Override
    public int getItemCount() {
        return voucherList.size();
    }

    public static class VoucherViewHolder extends RecyclerView.ViewHolder {
        TextView tvDiscount, tvDetail, tvDate;

        public VoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDiscount = itemView.findViewById(R.id.tv_voucher_discount);
            tvDetail = itemView.findViewById(R.id.tv_voucher_details);
            tvDate = itemView.findViewById(R.id.tv_voucher_validity);
        }
    }
}

