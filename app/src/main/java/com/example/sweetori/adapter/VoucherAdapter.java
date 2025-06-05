package com.example.sweetori.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweetori.R;
import com.example.sweetori.VoucherDetailActivity;
import com.example.sweetori.dto.response.ResUserVoucherDTO;
import com.example.sweetori.dto.response.ResVoucherDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder> {
    private List<ResUserVoucherDTO.UserVoucherData> userVoucherList;
    private Context context;

    public VoucherAdapter(Context context, List<ResUserVoucherDTO.UserVoucherData> userVoucherList) {
        this.context = context;
        this.userVoucherList = userVoucherList;
    }

    @NonNull
    @Override
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_voucher, parent, false);
        return new VoucherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        ResUserVoucherDTO.UserVoucherData userVoucher = userVoucherList.get(position);
        ResVoucherDTO.VoucherData voucher = userVoucher.getVoucher();

        if (voucher == null) return;

        holder.tvDiscount.setText("Sale " + voucher.getDiscountAmount().intValue() + " VND");
        holder.tvDetail.setText("Code: " + voucher.getCode());
        holder.tvDate.setText("Expire: " + formatDate(voucher.getValidTo()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VoucherDetailActivity.class);
            intent.putExtra("voucher", voucher);
            context.startActivity(intent);
        });

        holder.btnCopy.setOnClickListener(v -> {
            android.content.ClipboardManager clipboard =
                    (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip =
                    android.content.ClipData.newPlainText("Voucher Code", voucher.getCode());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Code copied: " + voucher.getCode(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return userVoucherList.size();
    }

    public static class VoucherViewHolder extends RecyclerView.ViewHolder {
        TextView tvDiscount, tvDetail, tvDate;
        Button btnCopy;

        public VoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDiscount = itemView.findViewById(R.id.tv_voucher_discount);
            tvDetail = itemView.findViewById(R.id.tv_voucher_details);
            tvDate = itemView.findViewById(R.id.tv_voucher_validity);
            btnCopy = itemView.findViewById(R.id.btn_copy);
        }
    }

    private String formatDate(String rawDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = inputFormat.parse(rawDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return rawDate;
        }
    }

    public void updateData(List<ResUserVoucherDTO.UserVoucherData> newList) {
        userVoucherList.clear();
        userVoucherList.addAll(newList);
        notifyDataSetChanged();
    }
}
