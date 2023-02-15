package com.example.testwhatatrip;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class reviews_recycler_adapter  extends RecyclerView.Adapter<reviews_recycler_adapter.ViewHolder> {
    List<reviews_model> Reviews_model;

    public reviews_recycler_adapter(List<reviews_model> reviews_model) {
        Reviews_model = reviews_model;
    }

    @NonNull
    @Override
    public reviews_recycler_adapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  reviews_recycler_adapter.ViewHolder holder, int position) {
        String profile = Reviews_model.get(position).getReview_image();
        String user_name = Reviews_model.get(position).getName();
        String reviews = Reviews_model.get(position).getReview_description();

        holder.setDetails(profile,user_name,reviews);

        holder.Chat_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Messages.class);
                intent.putExtra("UserName",Reviews_model.get(position).name);
                intent.putExtra("Profile",Reviews_model.get(position).review_image);
                intent.putExtra("review_uid",Reviews_model.get(position).review_user_uid);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Reviews_model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profile;
        public TextView User_Name,reviews,Chat_Button;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.review_profilepic);
            User_Name = itemView.findViewById(R.id.review_user_name);
            reviews = itemView.findViewById(R.id.review_user_review);
            Chat_Button = itemView.findViewById(R.id.chat_button);
        }
        private void setDetails(String Profile,String user_Name,String Reviews)
        {
            Glide.with(itemView.getContext()).load(Profile).apply(new RequestOptions().placeholder(R.drawable.whatatriplogopic)).into(profile);
            User_Name.setText(user_Name);
            reviews.setText(Reviews);
        }
    }

}
