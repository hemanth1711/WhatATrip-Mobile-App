package com.example.testwhatatrip;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Ratings_data {

    public static List<String> MyRatedIds = new ArrayList<>();
    public static List<Long> MyRatingsNumber = new ArrayList<>();
    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public static void loadRatingList(final Context context) {
        if (!tourdetails.running_rating_query) {
            tourdetails.running_rating_query = true;
            MyRatedIds.clear();
            MyRatingsNumber.clear();
            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                    .document("MY_RATINGS").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                            MyRatedIds.add(task.getResult().get("product_ID_" + x).toString());
                            MyRatingsNumber.add((long) task.getResult().get("rating_" + x));


                            if (task.getResult().get("product_ID_" + x).toString().equals(tourdetails.docId)) {
                                tourdetails.initial_ratings = Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + x))) - 1;

                                if(tourdetails.ratings_layout != null) {
                                    tourdetails.setRatings(Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + x))) - 1);
                                }
                            }
                        }

                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }
                    tourdetails.running_rating_query = false;
                }

            });
        }
    }
}
