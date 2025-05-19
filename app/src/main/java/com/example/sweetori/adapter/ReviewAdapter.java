package com.example.sweetori.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sweetori.R;
import com.example.sweetori.dto.response.ResReviewDTO;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private final List<ResReviewDTO> reviewList;
    private final Context context;

    public ReviewAdapter(List<ResReviewDTO> reviewList, Context context) {
        // Tránh null pointer: nếu null thì khởi tạo list rỗng
        this.reviewList = reviewList != null ? reviewList : new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Dùng parent.getContext() để chắc chắn context phù hợp với parent
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ResReviewDTO review = reviewList.get(position);

        // Gộp tên người dùng, nếu không có thì để Unknown
        String fullName = "Unknown";
        if (review.getUser() != null) {
            String firstName = review.getUser().getFirstName() != null ? review.getUser().getFirstName() : "";
            String lastName = review.getUser().getLastName() != null ? review.getUser().getLastName() : "";
            fullName = (firstName + " " + lastName).trim();
            if (fullName.isEmpty()) fullName = "Unknown";
        }

        holder.txtName.setText(fullName);
        holder.txtDate.setText(review.getUpdatedAt() != null ? review.getUpdatedAt() : "N/A");
        holder.txtComment.setText(review.getFeedback() != null ? review.getFeedback() : "Không có bình luận.");
        holder.ratingBar.setRating((float) review.getRate());

        // Load avatar: ưu tiên ảnh sản phẩm, nếu không có thì dùng icon mặc định
        String imageUrl = null;
        if (review.getProduct() != null && review.getProduct().getImage() != null && !review.getProduct().getImage().isEmpty()) {
            imageUrl = review.getProduct().getImage();
        }

        if (imageUrl != null) {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.uit)
                    .error(R.drawable.account_icon)
                    .into(holder.imgAvatar);
        } else {
            holder.imgAvatar.setImageResource(R.drawable.account_icon);
        }

        Log.d("ReviewAdapter", String.format("User: %s - Rating: %.1f", fullName, review.getRate()));


    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    // Cập nhật danh sách review mới, đảm bảo tránh lỗi null
    public void updateReviewList(List<ResReviewDTO> newList) {
        if (newList == null) return;
        reviewList.clear();
        reviewList.addAll(newList);
        notifyDataSetChanged();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView txtName, txtDate, txtComment;
        RatingBar ratingBar;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            txtName = itemView.findViewById(R.id.tvName);
            txtDate = itemView.findViewById(R.id.tvDate);
            txtComment = itemView.findViewById(R.id.tvComment);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
