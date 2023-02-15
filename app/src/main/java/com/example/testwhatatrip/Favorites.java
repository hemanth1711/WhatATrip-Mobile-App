package com.example.testwhatatrip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Favorites extends AppCompatActivity {
    private  RecyclerView FavoriteRecyclerView;
    public static FavoriteRecyclerAdapter favoriteRecyclerAdapter;
    private ImageView back_favorites;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        back_favorites = findViewById(R.id.back_favorites);
        FavoriteRecyclerView = findViewById(R.id.FavoriteRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Favorites.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        FavoriteRecyclerView.setLayoutManager(linearLayoutManager);
//        List<FavoritesModel> favoritesModelList = new ArrayList<>();
        Favorites_data.wishListModelList.clear();
//        favoritesModelList.add(new FavoritesModel( R.drawable.grassland,"Rome","Helobjdbgkjdbjsdgbjdsbfvjdbvjdsbkjvbsdjvbjkdsbvkjdbvkjdbvjbdxkjvbkjdb",40000));
//        favoritesModelList.add(new FavoritesModel( R.drawable.grassland,"Rome","Helobjdbgkjdbjsdgbjdsbfvjdbvjdsbkjvbsdjvbjkdsbvkjdbvkjdbvjbdxkjvbkjdb",40000));
        if(Favorites_data.wishListModelList.size()==0) {
            Favorites_data.WishList.clear();
            Favorites_data.loadFavoriteData(Favorites.this,true);
        }
        favoriteRecyclerAdapter = new FavoriteRecyclerAdapter(Favorites_data.wishListModelList);
        FavoriteRecyclerView.setAdapter(favoriteRecyclerAdapter);
        favoriteRecyclerAdapter.notifyDataSetChanged();
        back_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Favorites.this,Home.class));
                finish();
            }
        });
    }

}