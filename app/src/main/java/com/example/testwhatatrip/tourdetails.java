package com.example.testwhatatrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.example.testwhatatrip.Favorites_data.firebaseFirestore;

public class tourdetails extends AppCompatActivity implements PaymentResultListener {

    //////////////////////////////////////////////////// Slider ///////////////////////////////////////
    TourDetailsImageAsapter slider;
    ViewPager viewPager;
    private List<slider_model> slider_modelList;
    private TabLayout viewpager_tab;
    private ImageView whishlist;
    public static boolean addwhish = false;
    boolean onclicked = false;
    public static String docId;
    public TextView ToursDescription,Tour_details_price;
    private ImageView BackTours;
    private DocumentSnapshot document;
    private TextView Tours_Buy_Now_Button;
    private Checkout checkout = new Checkout();
    private String Tour_Price;
    private String OrderId;
    private  String Product_image;

    //////////////////////////////////////////// ratings layout //////////////////////////////
    public  static LinearLayout ratings_layout;
    public  static LinearLayout Rating_progressbar_container;
    public  static LinearLayout ratings_number;
    public static int initial_ratings;
    public static boolean running_rating_query = false;
    public TextView totalRatings;
    public TextView Avg_Rating;
    public static  String UUri;
    //////////////////////// Reviews Data

    public ImageView Single_review_profile;
    public TextView Single_review_dis,Single_review_name,Single_review_chat;
    public EditText Reviews_input;
    public static reviews_recycler_adapter Recycler_review_adapter;
    public RecyclerView Reviews_RecyclerView;
    public EditText review_edit_text;
    private Button Review_Submit;
    public static DatabaseReference databaseReference;
    public String FullName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Checkout.preload(getApplicationContext());
        setContentView(R.layout.activity_tourdetails);
        TextView Tour_Title = findViewById(R.id.Tour_details_title);
        Tours_Buy_Now_Button = findViewById(R.id.tour_buy_now);
        ToursDescription = findViewById(R.id.TourDescription);
        Tour_details_price = findViewById(R.id.TourDetailsPrice);
        BackTours = findViewById(R.id.back_tours);
        String Tour_name = getIntent().getStringExtra("tour_name");
        Tour_Title.setText(Tour_name);
        docId = Tour_name;
        Single_review_profile = findViewById(R.id.review_profilepic);
        Single_review_chat = findViewById(R.id.chat_button);
        Single_review_dis = findViewById(R.id.review_user_review);
        Single_review_name = findViewById(R.id.review_user_name);
        // Seting chat visiblity to false
        Single_review_chat.setVisibility(View.INVISIBLE);
        //
        viewPager = findViewById(R.id.tourdetailspager);
        whishlist = findViewById(R.id.imagetours);
        viewpager_tab = (TabLayout) findViewById(R.id.tablayouttour);
        slider_modelList = new ArrayList<slider_model>();
        slider = new TourDetailsImageAsapter(slider_modelList);
        viewPager.setAdapter(slider);
        viewpager_tab.setupWithViewPager(viewPager,true);

        //////////// ratings layout
        initial_ratings = -1;
        totalRatings = findViewById(R.id.total_number_of_ratings);
        Review_Submit = findViewById(R.id.review_submit);
        review_edit_text =findViewById(R.id.edit_review);
        Avg_Rating = findViewById(R.id.avg_rating);
        TextView Total_min = findViewById(R.id.total_ratings);
        ratings_number = findViewById(R.id.linearLayout5);
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        DocumentReference docIdRef = rootRef.collection("TOURS").document(docId);

//        rootRef.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USERS_DATA")
//                .document("FAVORITES").update()

        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(tourdetails.this, docId, Toast.LENGTH_SHORT).show();
                     document = task.getResult();
                    if (document.exists()) {
                        //Toast.makeText(tourdetails.this,"Tour Exist",Toast.LENGTH_SHORT).show();
                        for(long x =1 ; x< (long)document.get("no_of_product_images")+1 ; x++)
                        {
                            slider_modelList.add(new slider_model(document.get("product_image_"+x).toString()));
                        }
                        slider.notifyDataSetChanged();
                        ToursDescription.setText(document.get("product_description").toString());
                        Tour_details_price.setText("Rs"+document.get("price").toString()+"/-");
                        Tour_Price = document.get("price").toString();
                        Product_image = document.get("product_image_1").toString();
                        ///////////////////// Ratings layout //////////


                        totalRatings.setText(document.get("total_ratings").toString());
                        Total_min.setText(document.get("total_ratings").toString()+" Ratings");
                        Avg_Rating.setText(document.get("average_rating").toString());

                        for (int x=0;x<5;x++)
                        {
                            TextView RatingFigure = (TextView) ratings_number.getChildAt(x);
                            RatingFigure.setText(String.valueOf((long) document.get((5-x)+"_star")));
                            ProgressBar progressBar = (ProgressBar) Rating_progressbar_container.getChildAt(x);
                            int maxProgress = Integer.parseInt(String.valueOf((long)document.get("total_ratings")));
                            progressBar.setMax(maxProgress);
                            progressBar.setProgress(Integer.parseInt(RatingFigure.getText().toString()));
                        }


                        if(Favorites_data.WishList.size()==0)
                        {
                            Favorites_data.WishList.clear();
                            Favorites_data.loadFavoriteData(tourdetails.this,false);
                        }
                        if(Ratings_data.MyRatedIds.size()==0)
                        {
                            Ratings_data.loadRatingList(tourdetails.this);
                        }
                        else
                        {
                            Ratings_data.loadRatingList(tourdetails.this);
                        }

                        if(Favorites_data.WishList.contains(docId))
                        {
                            addwhish = true;
                            whishlist.setImageResource(R.drawable.whish);
                        }
                        else
                        {
                            addwhish = false;
                            whishlist.setImageResource(R.drawable.whishgr);
                        }

                    } else {
                        Toast.makeText(tourdetails.this, docId, Toast.LENGTH_SHORT).show();
                        Toast.makeText(tourdetails.this,"Tour not Exists",Toast.LENGTH_SHORT).show();
                        docId = "Rome";
                        rootRef.collection("TOURS").document(docId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    document = task.getResult();
                                    for(long x =1 ; x< (long)document.get("no_of_product_images")+1 ; x++)
                                    {
                                        slider_modelList.add(new slider_model(document.get("product_image_"+x).toString()));
                                    }
                                    slider.notifyDataSetChanged();
                                    ToursDescription.setText(document.get("product_description").toString());
                                    Tour_details_price.setText("Rs"+document.get("price").toString()+"/-");
                                    Tour_Price = document.get("price").toString();

                                    if(Favorites_data.WishList.size()==0)
                                    {
                                        Favorites_data.WishList.clear();
                                        Favorites_data.loadFavoriteData(tourdetails.this,false);
                                    }

                                    if(Favorites_data.WishList.contains(docId))
                                    {
                                        addwhish = true;
                                        whishlist.setImageResource(R.drawable.whish);
                                    }
                                    else
                                    {
                                        addwhish = false;
                                        whishlist.setImageResource(R.drawable.whishgr);
                                    }
                                }

                                else {
                                    Toast.makeText(tourdetails.this,"Failed"+task.getException().toString(),Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }


                } else {
                    Toast.makeText(tourdetails.this,"Failed"+task.getException().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });


        /////////////////////// Reviews layout

        firebaseFirestore.collection("TOURS").document(tourdetails.docId).collection("REVIEWS").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull  Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        Single_review_name.setText(document.get("username").toString());
                        Single_review_dis.setText(task.getResult().get("review").toString());
                        Glide.with(tourdetails.this).load(task.getResult().get("profileurl").toString()).apply(new RequestOptions().placeholder(R.drawable.whatatriplogopic)).into(Single_review_profile);
                    }
                }
                else
                {
                    Toast.makeText(tourdetails.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        Reviews_input = findViewById(R.id.edit_review);
        Reviews_RecyclerView = findViewById(R.id.review_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(tourdetails.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        Reviews_RecyclerView.setLayoutManager(linearLayoutManager);
        reviews_data.Reviews_Model.clear();
        if(reviews_data.Reviews_Model.size()==0) {
            reviews_data.Reviews_Model.clear();
            reviews_data.loadReviewsData(tourdetails.this);
        }
        else
        {
            reviews_data.Reviews_Model.clear();
            reviews_data.loadReviewsData(tourdetails.this);
        }
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        Recycler_review_adapter = new reviews_recycler_adapter(reviews_data.Reviews_Model);
        Reviews_RecyclerView.setAdapter(Recycler_review_adapter);
        Recycler_review_adapter.notifyDataSetChanged();
        StorageReference storageReference = firebaseStorage.getReference("images/profile/" + FirebaseAuth.getInstance().getUid());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Toast.makeText(tourdetails.this, uri.toString(), Toast.LENGTH_SHORT).show();
                UUri = uri.toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FullName = snapshot.child("full_name").getValue(String.class);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(tourdetails.this, "Not retrived", Toast.LENGTH_SHORT).show();
            }
        });

        Review_Submit.setOnClickListener(new View.OnClickListener() {
            Map<String, Object> Review_discription = new HashMap<>();
            @Override

            public void onClick(View v) {
                Review_discription.put("review",review_edit_text.getText().toString());
                Review_discription.put("profileurl",UUri);
                Review_discription.put("username",FullName);
                docIdRef.collection("REVIEWS").document(FirebaseAuth.getInstance().getUid()).set(Review_discription).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull  Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            firebaseFirestore.collection("TOURS").document(tourdetails.docId).collection("REVIEWS").document(FirebaseAuth.getInstance().getUid())
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull  Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful())
                                    {
                                        Single_review_name.setText(task.getResult().get("username").toString());
                                        Single_review_dis.setText(task.getResult().get("review").toString());
                                        Glide.with(tourdetails.this).load(task.getResult().get("profileurl").toString()).apply(new RequestOptions().placeholder(R.drawable.whatatriplogopic)).into(Single_review_profile);
                                        review_edit_text.setText("");
                                    }
                                    else
                                    {
                                        Toast.makeText(tourdetails.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            Toast.makeText(tourdetails.this,"Review added successfully",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(tourdetails.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });



        /////////////////////// ratings layout
        ratings_layout = findViewById(R.id.rate_now_container);
        Rating_progressbar_container = findViewById(R.id.rating_progressbar_container);
        for(int x=0;x<ratings_layout.getChildCount();x++)
        {
            final int startPosition = x;
            ratings_layout.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(startPosition!=initial_ratings) {
                        if (!running_rating_query) {
                            setRatings(startPosition);
                            Map<String, Object> updateRating = new HashMap<>();
                            if (Ratings_data.MyRatedIds.contains(docId)) {
                                TextView startRating = (TextView) ratings_number.getChildAt(5 - initial_ratings - 1);
                                TextView finalRating = (TextView) ratings_number.getChildAt(5 - startPosition - 1);
                                updateRating.put(initial_ratings + 1 + "_star", Long.parseLong(startRating.getText().toString()) - 1);
                                updateRating.put(startPosition + 1 + "_star", Long.parseLong(finalRating.getText().toString()) + 1);
                                updateRating.put("average_rating", calculate_average_rating((long) startPosition - initial_ratings, true));
                            } else {
                                updateRating.put(startPosition + 1 + "_star", (long) document.get(startPosition + 1 + "_star") + 1);
                                updateRating.put("average_rating", calculate_average_rating((long) startPosition + 1, false));
                                updateRating.put("total_ratings", (long) document.get("total_ratings") + 1);
                            }
                            firebaseFirestore.collection("TOURS").document(docId)
                                    .update(updateRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Map<String, Object> rating = new HashMap<>();
                                        if (Ratings_data.MyRatedIds.contains(docId)) {
                                            rating.put("rating_" + Ratings_data.MyRatedIds.indexOf(docId), (long) startPosition + 1);
                                        } else {

                                            rating.put("list_size", (long) Ratings_data.MyRatedIds.size() + 1);
                                            rating.put("product_ID_" + Ratings_data.MyRatedIds.size(), docId);
                                            rating.put("rating_" + Ratings_data.MyRatedIds.size(), startPosition + 1);
                                        }

                                        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).
                                                collection("USER_DATA").document("MY_RATINGS").
                                                update(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    if (Ratings_data.MyRatedIds.contains(docId)) {
                                                        Ratings_data.MyRatingsNumber.set(Ratings_data.MyRatedIds.indexOf(docId), (long) startPosition + 1);
                                                        TextView startRating = (TextView) ratings_number.getChildAt(5 - initial_ratings - 1);
                                                        TextView finalRating = (TextView) ratings_number.getChildAt(5 - startPosition - 1);
                                                        startRating.setText(String.valueOf(Integer.parseInt(startRating.getText().toString()) - 1));
                                                        finalRating.setText(String.valueOf(Integer.parseInt(finalRating.getText().toString()) + 1));
                                                    } else {

                                                        Ratings_data.MyRatedIds.add(docId);
                                                        Ratings_data.MyRatingsNumber.add((long) startPosition + 1);

                                                        TextView Rating = (TextView) ratings_number.getChildAt(5 - startPosition - 1);
                                                        Rating.setText(String.valueOf(Integer.parseInt(Rating.getText().toString()) + 1));
                                                        totalRatings.setText(String.valueOf((long) document.get("total_ratings") + 1));
                                                        Total_min.setText(String.valueOf((long) document.get("total_ratings") + 1) + " Ratings");
                                                        Toast.makeText(tourdetails.this, "Thank's For the Rating", Toast.LENGTH_SHORT).show();
                                                    }
                                                    for (int x = 0; x < 5; x++) {
                                                        TextView RatingFigure = (TextView) ratings_number.getChildAt(x);
                                                        ProgressBar progressBar = (ProgressBar) Rating_progressbar_container.getChildAt(x);
                                                        int maxProgress = Integer.parseInt(totalRatings.getText().toString());
                                                        progressBar.setMax(maxProgress);
                                                        progressBar.setProgress(Integer.parseInt(RatingFigure.getText().toString()));
                                                    }
                                                    initial_ratings = startPosition;
                                                    Avg_Rating.setText(calculate_average_rating(0, true));
                                                } else {
                                                    setRatings(initial_ratings);
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(tourdetails.this, error, Toast.LENGTH_SHORT).show();
                                                }
                                                running_rating_query = false;
                                            }
                                        });
                                    } else {
                                        running_rating_query = false;
                                        setRatings(initial_ratings);
                                        String error = task.getException().getMessage();
                                        Toast.makeText(tourdetails.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }
        ////////////////////// ratings layout
//        slider_modelList.add(new test_model(R.drawable.grassland));
//        slider_modelList.add(new test_model(R.drawable.pixbay));
//        slider_modelList.add(new test_model(R.drawable.grassland));
//        slider_modelList.add(new test_model(R.drawable.pixbay));
//        slider_modelList.add(new test_model(R.drawable.bridge));
//        slider_modelList.add(new test_model(R.drawable.grassland));
//        slider_modelList.add(new test_model(R.drawable.pixbay));
//        slider_modelList.add(new test_model(R.drawable.grassland));
//        slider_modelList.add(new test_model(R.drawable.pixbay));
//        slider_modelList.add(new test_model(R.drawable.bridge));

        if (onclicked==false)
        {
        if (addwhish==false) {
            whishlist.setImageResource(R.drawable.whishgr);
        }
        else
            {
                whishlist.setImageResource(R.drawable.whish);
            }
        }
        Tours_Buy_Now_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderId = UUID.randomUUID().toString().substring(0,28);
                placeOrderDetails();
                startPayment();
            }
        });

        //////////////////////////////////////////////////////////////////  Whish List code ////////////////////////////////////////
        whishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Favorites_data.WishList.contains(docId))
                {
                    whishlist.setImageResource(R.drawable.whish);
                    addwhish = true;
                    onclicked = true;
                }
                if(addwhish)
                {
                    int index = Favorites_data.WishList.indexOf(docId);
                    Toast.makeText(tourdetails.this, docId+"is the index", Toast.LENGTH_SHORT).show();
                    Favorites_data.removeFromFavorite(index,tourdetails.this);
                    whishlist.setImageResource(R.drawable.whishgr);
                    addwhish = false;
                }
                else
                {
                    Map<String,Object> add_tour = new HashMap<>();
                    add_tour.put("product_ID_"+ String.valueOf(Favorites_data.WishList.size()),docId);
                    rootRef.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_FAVORITES")
                            .update(add_tour).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Map<String,Object> updateListSize = new HashMap<>();
                                updateListSize.put("list_size", (long) (Favorites_data.WishList.size()+1));
                                rootRef.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_FAVORITES")
                                        .update(updateListSize).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                                Favorites_data.wishListModelList.add(new FavoritesModel(document.get("product_image_1").toString(),document.get("product_title").toString(),
                                                        document.get("product_description").toString(),Integer.parseInt(document.get("price").toString())));
                                            addwhish = true;
                                            Favorites_data.WishList.add(docId);
                                            whishlist.setImageResource(R.drawable.whish);
                                            Log.d("wishlist", "onComplete: Added to whishlist successfully");
                                            Toast.makeText(tourdetails.this,"added to wish list",Toast.LENGTH_SHORT).show();
                                        }else
                                        {
                                            String error = task.getException().getMessage();
                                            Toast.makeText(tourdetails.this,error,Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else
                            {
                                String error = task.getException().getMessage();
                                Toast.makeText(tourdetails.this,error,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
        BackTours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(tourdetails.this,Home.class));
                finish();
            }
        });
    }

    //////////////////////////////////////////////// ratings layout //////////////////////////////////////

    public static void setRatings(int startPosition)
    {
            for (int x = 0; x < ratings_layout.getChildCount(); x++) {
                ImageView startbtn = (ImageView) ratings_layout.getChildAt(x);
                startbtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
                if (x <= startPosition) {
                    startbtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
                }
            }
    }

    private String calculate_average_rating(long currentUserRating,boolean update)
    {
        Double total_stars = Double.valueOf(0);
        for(int x=1;x<6;x++)
        {
            TextView ratno = (TextView) ratings_number.getChildAt(5-x);
           total_stars = total_stars + (Long.parseLong(ratno.getText().toString())*x);
        }
        total_stars = total_stars+currentUserRating;
        if(update) {
            return String.valueOf(total_stars / Long.parseLong(totalRatings.getText().toString())).substring(0,3);
        }
        else {
            return String.valueOf(total_stars / (Long.parseLong(totalRatings.getText().toString()) +1)).substring(0,3);
        }
    }

    //////////////////////////////////////////////// ratings layout //////////////////////////////////////





    /////////////////////////////////////////////////////////////////   Payment Code //////////////////////////////////////
    public void startPayment(){
//        checkout.setKeyID("rzp_test_s4k6CPAeRHxqTO");
        /**
         * Instantiate Checkout
         */
        /**
         * Set your logo here
         */
        /**
         * Reference to current activity
         */
        final Activity activity = this;
        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Online Payment");
            options.put("description", "Reference No. #123456");
            options.put("currency", "INR");
            options.put("amount",  Tour_Price+"00");//pass amount in currency subunits
            checkout.open(tourdetails.this, options);

        } catch(Exception e) {
            Toast.makeText(this, "Error in starting Razorpay Checkout" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }
//////////////////////////////////////////////////////// storing for specific users ////////////////////////////////////////
    @Override
    public void onPaymentSuccess(String s) {
        Map<String,Object>  UpdateStatus = new HashMap<>();
        UpdateStatus.put("PaymentStatus","paid");
        UpdateStatus.put("OrderStatus","Ordered");
        firebaseFirestore.collection("MyBookings").document(OrderId).collection("order_items")
                .document(docId).update(UpdateStatus)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Map<String,Object> User_orders = new HashMap<>();
                            User_orders.put("order_id",OrderId);
                            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_BOOKINGS")
                                    .document(OrderId).set(User_orders).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(tourdetails.this,"sucessfull",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(tourdetails.this,Payment.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(tourdetails.this,"Failed to update",Toast.LENGTH_SHORT);
                                    }
                                }
                            });
                        }else {
                            String error = task.getException().getMessage();
                            Toast.makeText(tourdetails.this,error,Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    ////////////////////////////////////////////////////////////  Order details ///////////////////////////////////////////
    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
    }

    private void placeOrderDetails(){
        String User = FirebaseAuth.getInstance().getUid();
                Map<String,Object> order_detail = new HashMap<>();
                order_detail.put("OrderId",OrderId);
                order_detail.put("productID",docId);
                order_detail.put("UserID",User);
                order_detail.put("product image",Product_image);
                order_detail.put("product title",docId);
                order_detail.put("productPrice",Tour_Price);
                order_detail.put("Date", FieldValue.serverTimestamp());
                order_detail.put("PaymentMode","paytm");
                order_detail.put("PaymentStatus","not paid");
                order_detail.put("OrderStatus","Ordered");
                firebaseFirestore.collection("MyBookings").document(OrderId).collection("order_items")
                        .document(docId)
                        .set(order_detail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(tourdetails.this,"Updated in db successfully",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
}