package com.example.testwhatatrip;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.testwhatatrip.FavoriteRecyclerAdapter;
import com.example.testwhatatrip.Favorites;
import com.example.testwhatatrip.FavoritesModel;
import com.example.testwhatatrip.tourdetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Favorites_data {
    public static List<String> WishList = new ArrayList<>();
    public  static  List<FavoritesModel> wishListModelList = new ArrayList<>();

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public static void loadFavoriteData(final Context context, final boolean loadproductdata)
    {
        WishList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                .document("MY_FAVORITES").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            for(long x = 0 ; x < (long)task.getResult().get("list_size"); x++){
                                WishList.add(task.getResult().get("product_ID_"+x).toString());
                                if(loadproductdata) {
                                    firebaseFirestore.collection("TOURS").document(task.getResult().get("product_ID_" + x).toString())
                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {

                                                wishListModelList.add(new FavoritesModel(task.getResult().get("product_image_1").toString(),task.getResult().get("product_title").toString(),
                                                        task.getResult().get("product_description").toString(),Integer.parseInt(task.getResult().get("price").toString())));
                                                Favorites.favoriteRecyclerAdapter.notifyDataSetChanged();

                                            } else {
                                                String error = task.getException().getMessage();
                                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        }else{
                            String error = task.getException().getMessage();
                            Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public static void removeFromFavorite(final int index, final Context context){
        final String removedProductID = WishList.remove(index);
//        if(WishList.size()!=0) {
//            WishList.remove(index);
//        }
        Map<String,Object> updateWhishList = new HashMap<>();
        for(int x=0;x< WishList.size(); x++){
            updateWhishList.put("product_ID_"+x,WishList.get(x));
        }
        updateWhishList.put("list_size",(long)WishList.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                .document("MY_FAVORITES").set(updateWhishList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    tourdetails.addwhish=false;
                    Toast.makeText(context,"Removed successfully",Toast.LENGTH_SHORT).show();
//                    Favorites.favoriteRecyclerAdapter.notifyDataSetChanged();
                }else {
                    WishList.add(index,removedProductID);
                    String error = task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    }



