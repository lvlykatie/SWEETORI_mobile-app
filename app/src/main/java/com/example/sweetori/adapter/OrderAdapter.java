package com.example.sweetori.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweetori.ProductDetailActivity;
import com.example.sweetori.R;
import com.example.sweetori.dto.response.ResOrderDTO;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<ResOrderDTO> orderList;
    private Context context;

    public OrderAdapter(List<ResOrderDTO> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
        sortOrdersByDateDescending();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        ResOrderDTO data = orderList.get(position);
        holder.tvTotalPrice.setText(String.format("%,.0f VND", data.getTotal()));

        // Set LayoutManager trước khi gán adapter cho nested RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.orderItemRecyclerView.setLayoutManager(layoutManager);
        holder.orderItemRecyclerView.setAdapter(new OrderItemAdapter(data.getListOfOrderdetails(), context));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("orderId", data.getOrderId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    public void updateOrderList(List<ResOrderDTO> newList) {
        if (newList != null) {
            orderList.clear();
            orderList.addAll(newList);
            sortOrdersByDateDescending();
            notifyDataSetChanged();
            Log.d("OrderAdapter", "Updated list with " + newList.size() + " items");
        } else {
            Log.e("OrderAdapter", "New list is null");
        }
    }
    private void sortOrdersByDateDescending() {
        if (orderList != null && !orderList.isEmpty()) {
            Collections.sort(orderList, new Comparator<ResOrderDTO>() {
                @Override
                public int compare(ResOrderDTO o1, ResOrderDTO o2) {
                    // Giả sử ResOrderDTO có trường orderDate kiểu String (yyyy-MM-dd)
                    return o2.getDate().compareTo(o1.getDate());

                    // Nếu orderDate là kiểu Date object:
                    // return o2.getOrderDate().compareTo(o1.getOrderDate());
                }
            });
        }
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvTotalPrice;
        RecyclerView orderItemRecyclerView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            orderItemRecyclerView = itemView.findViewById(R.id.orderItemRecyclerView);
            // Ngăn việc RecyclerView con làm scroll không mượt trong RecyclerView cha
            orderItemRecyclerView.setNestedScrollingEnabled(false);
        }
    }
}
