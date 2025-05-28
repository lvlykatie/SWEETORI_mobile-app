package com.example.sweetori.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweetori.R;
import com.example.sweetori.dto.response.ResVoucherDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
public class VoucherGroupAdapter extends RecyclerView.Adapter<VoucherGroupAdapter.GroupViewHolder> {
    private List<List<ResVoucherDTO.VoucherData>> groupList;
    private Context context;

    public VoucherGroupAdapter(Context context, List<List<ResVoucherDTO.VoucherData>> groupList) {
        this.context = context;
        this.groupList = groupList;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_voucher_group, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        List<ResVoucherDTO.VoucherData> group = groupList.get(position);
        VoucherAdapter adapter = new VoucherAdapter(context, group);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.rv_voucher_group);
        }
    }
}

