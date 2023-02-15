package com.example.testwhatatrip;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.testwhatatrip.Favorites_data.firebaseFirestore;


public class MyBookings_data {
    public static List<MyBookingsModel> myorder_item_models = new ArrayList<>();
    public static void loadOrders(final Context context, final MyBokkingsAdapter myOrderAdapter){
        myorder_item_models.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_BOOKINGS")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                        firebaseFirestore.collection("MyBookings").document(documentSnapshot.getString("order_id")).
                                collection("order_items").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(DocumentSnapshot Order_items : task.getResult().getDocuments()){
                                        MyBookingsModel myorder_item_model = new MyBookingsModel(Order_items.getString("product image"),
                                                Order_items.getString("product title"),
                                                Order_items.getDate("Date"),
                                                Integer.parseInt(Order_items.getString("productPrice"))
                                                );
                                        myorder_item_models.add(myorder_item_model);
                                    }
                                    myOrderAdapter.notifyDataSetChanged();
                                }
                                else{
                                    String error = task.getException().getMessage();
                                    Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                else{
                    String error = task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
