package com.example.testwhatatrip;

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

public class categories_recycler_adapter extends RecyclerView.Adapter<categories_recycler_adapter.ViewHolder> {
    @NonNull
    List<categories_model> categories_models;

    public categories_recycler_adapter(@NonNull List<categories_model> categories_models) {
        this.categories_models = categories_models;
    }

    @Override
    public categories_recycler_adapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  categories_recycler_adapter.ViewHolder holder, int position) {
        String CIcon = categories_models.get(position).getCategories_icon();
        holder.categories_text.setText(categories_models.get(position).getCategories_title());
        holder.setCategorieIcon(CIcon);
    }

    @Override
    public int getItemCount() {
        return categories_models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView categories_image;
        private TextView categories_text;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            categories_image = itemView.findViewById(R.id.categories_item);
            categories_text = itemView.findViewById(R.id.categories_title);
        }
        private void setCategorieIcon(String IconUri){
            Glide.with(itemView.getContext()).load(IconUri).apply(new RequestOptions().placeholder(R.drawable.whatatriplogopic)).into(categories_image);
        }
    }
}
