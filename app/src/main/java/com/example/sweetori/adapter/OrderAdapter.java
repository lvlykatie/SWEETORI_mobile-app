package com.example.sweetori.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sweetori.ProductDetailActivity;
import com.example.sweetori.R;
import com.example.sweetori.dto.response.ResOrderDTO;
import com.example.sweetori.dto.response.ResOrderDetailDTO;
import com.example.sweetori.dto.response.ResProductDTO;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{
    private List<ResOrderDTO> orderList;
    private Context context;

    public OrderAdapter(List<ResOrderDTO> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        return new OrderAdapter.OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        if (position >= 0 && position < orderList.size()) {
            final ResOrderDTO data = orderList.get(position);  // final ở đây

            holder.tvTotal.setText(String.format("%,.0f VND", data.getTotal()));

            // Set up RecyclerView for order items
            OrderItemAdapter orderItemAdapter = new OrderItemAdapter(data.getListOfOrderdetails(), context);
            holder.orderItemRecyclerView.setAdapter(orderItemAdapter);

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("orderId", data.getOrderId());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount () {
        return orderList != null ? orderList.size() : 0;
    }
    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvTotal;
        RecyclerView orderItemRecyclerView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            orderItemRecyclerView = itemView.findViewById(R.id.orderItemRecyclerView);
            orderItemRecyclerView.setNestedScrollingEnabled(false);
        }
    }
    public void updateOrderList(List<ResOrderDTO> newList) {
        if (newList != null) {
            orderList.clear();
            orderList.addAll(newList);
            notifyDataSetChanged();
            Log.d("OrderAdapter", "Updated list with " + newList.size() + " items");
        } else {
            Log.e("OrderAdapter", "New list is null");
        }
    }
}