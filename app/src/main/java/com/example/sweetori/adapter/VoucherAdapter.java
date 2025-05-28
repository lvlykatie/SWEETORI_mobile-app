package com.example.sweetori.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweetori.R;
import com.example.sweetori.SharedPref;
import com.example.sweetori.VoucherDetailActivity;
import com.example.sweetori.dto.response.ResUserDTO;
import com.example.sweetori.dto.response.ResUserVoucherDTO;
import com.example.sweetori.dto.response.ResVoucherDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
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

        boolean hasVoucher = false;
        boolean isUsed = false;

        List<ResUserDTO> users = voucher.getUsers();
        List<ResUserVoucherDTO> userVouchers = voucher.getUserVouchers();
        Pair<String, Integer> accessTokenWithUserId = SharedPref.getAccessTokenWithUserId(context);
        int userId = accessTokenWithUserId.second;
        if (users != null && userVouchers != null) {
            for (int i = 0; i < users.size(); i++) {
                ResUserDTO user = users.get(i);
                if (user.getUserId() == userId) {
                    hasVoucher = true;
                    ResUserVoucherDTO uv = userVouchers.get(i);
                    isUsed = uv.isUsed();
                    break;
                }
            }
        }

        if (!hasVoucher || isUsed) {
            // Ẩn item nếu không có hoặc đã dùng
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            return;
        }

        // Hiển thị voucher hợp lệ
        holder.itemView.setVisibility(View.VISIBLE);
        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        holder.tvDiscount.setText("Sale " + voucher.getDiscountAmount().intValue() + "VND");
        holder.tvDetail.setText("Code: " + voucher.getCode());
        String rawDate = voucher.getValidTo(); // e.g., "2025-06-15T00:00:00"
        String formattedDate = formatDate(rawDate);
        holder.tvDate.setText("Expire: " + formattedDate);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VoucherDetailActivity.class);
            intent.putExtra("voucher", voucher);
            context.startActivity(intent);
        });

        holder.btnCopy.setOnClickListener(v -> {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                    context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Voucher Code", voucher.getCode());
            clipboard.setPrimaryClip(clip);

            android.widget.Toast.makeText(context, "Code copied " + voucher.getCode(), Toast.LENGTH_SHORT).show();
        });
    }


    @Override
    public int getItemCount() {
        return voucherList.size();
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
            // Nếu có định dạng dạng đầy đủ ISO: "2025-06-15T00:00:00"
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

            // Nếu chỉ có ngày: "2025-06-15", thì dùng dòng sau thay thế:
            // SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = inputFormat.parse(rawDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return rawDate; // Trả lại ngày gốc nếu có lỗi
        }
    }

}

