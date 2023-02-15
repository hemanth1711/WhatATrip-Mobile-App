package com.example.testwhatatrip;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class reviews_data {
    public static List<reviews_model> Reviews_Model = new ArrayList<>();
    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static String ReUid = firebaseAuth.getUid();
    private static FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
    static String FullName,UUri;
    public static void loadReviewsData(final Context context)
    {
        Reviews_Model.clear();
        firebaseFirestore.collection("TOURS").document(tourdetails.docId).collection("REVIEWS").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull  Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshots : task.getResult())
                            {

                                if (documentSnapshots.getId(). equals(ReUid))
                                {
                                    Toast.makeText(context, "this is user id", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    StorageReference storageReference = firebaseStorage.getReference("images/profile/" + documentSnapshots.getId());
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            // TODO Converting Uri to String And downloading the image from fire store to database
                                            UUri = uri.toString();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle any errors
                                        }
                                    });
                                    Reviews_Model.add(new reviews_model(documentSnapshots.get("profileurl").toString(),documentSnapshots.get("username").toString(), documentSnapshots.get("review").toString(), documentSnapshots.getId()));
                                }

                            }
                            tourdetails.Recycler_review_adapter.notifyDataSetChanged();

                        }
                        else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
