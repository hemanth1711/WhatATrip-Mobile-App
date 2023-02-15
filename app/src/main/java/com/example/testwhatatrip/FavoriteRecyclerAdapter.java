package com.example.testwhatatrip;

import android.app.Activity;
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

public class FavoriteRecyclerAdapter extends RecyclerView.Adapter<FavoriteRecyclerAdapter.ViewHolder> {
List<FavoritesModel> favoritesModelList;

    public FavoriteRecyclerAdapter(List<FavoritesModel> favoritesModelList) {
        this.favoritesModelList = favoritesModelList;
    }

    @Override
    public FavoriteRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  FavoriteRecyclerAdapter.ViewHolder holder, int position) {
        String FavoriteImage = favoritesModelList.get(position).getImage();
        int FavoriteImagePrice = favoritesModelList.get(position).getPrice();
        String FavoriteImageTitle = favoritesModelList.get(position).getTitle();
        String FavoriteImageDescription = favoritesModelList.get(position).getShortDescription();
        holder.setFavoriteImage(FavoriteImage);
        holder.setFavoritePrice(FavoriteImagePrice);
        holder.setFavoriteTitle(FavoriteImageTitle);
        holder.setFavoriteDescription(FavoriteImageDescription);
        holder.setTourname(FavoriteImageTitle);

        holder.FavoriteRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = Favorites_data.WishList.indexOf(favoritesModelList.get(position).getTitle());
                Favorites_data.removeFromFavorite(index, v.getContext());
                holder.itemView.getContext().startActivity(new Intent(v.getContext(),Favorites.class));
                ((Activity)holder.itemView.getContext()).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoritesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView FavoriteImage,FavoriteRemove;
        public TextView FavoriteTitle,FavoriteDescription,FavoritePrice;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            FavoriteImage = itemView.findViewById(R.id.booking_image);
            FavoriteTitle = itemView.findViewById(R.id.booking_title);
            FavoriteDescription = itemView.findViewById(R.id.booking_date);
            FavoritePrice = itemView.findViewById(R.id.favorite_price);
            FavoriteRemove = itemView.findViewById(R.id.favorite_delete);
        }
        private void setTourname(String Title)
        {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), tourdetails.class);
                    intent.putExtra("tour_name",Title);
                    itemView.getContext().startActivity(intent);

                }
            });
        }
        public void setFavoriteImage(String Res)
        {
            Glide.with(itemView.getContext()).load(Res).apply(new RequestOptions().placeholder(R.drawable.whatatriplogopic)).into(FavoriteImage);
        }

        public void setFavoritePrice(int Price)
        {
            FavoritePrice.setText("Rs."+Price+"/-");
        }
        public void setFavoriteTitle(String Title)
        {
            FavoriteTitle.setText(Title);
        }
        public void setFavoriteDescription(String Description){
            String Short_des = Description.substring(0,120);
            FavoriteDescription.setText(Short_des+"...");
        }
    }
}
