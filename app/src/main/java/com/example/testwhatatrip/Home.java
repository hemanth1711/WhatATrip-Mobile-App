package com.example.testwhatatrip;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Home extends AppCompatActivity {
//    Button Logout,verify;
//    TextView email;
// Initialize Variable
    DrawerLayout drawerLayout;
    FirebaseAuth FAuth;
    RoundedImageView imageRound;

//    int images[] = {R.drawable.bridge,R.drawable.grassland,R.drawable.marlon,R.drawable.pixbay};

    ////////////////////////////////////////////// Banner Slider Code /////////////////////////////////////////////
    Slider slider;
    ViewPager viewPager;
    private List<slider_model> slider_modelList;
    private TabLayout viewpager_tab;

    /////////////////////////////////////////////  LatestTours  ////////////////////////////////////////////
    private TextView latestToursTitle;
    private RecyclerView latestToursRecyclerView;

    /////////////////////////////////////////////// Grid layout  ////////////////////////////////////////////////////

    private  TextView GridTextView;
    private Button GridViewAll;
    private GridView Grid_view;
    private RecyclerView Grid_Recycler_View;

    ///////////////////////////////////// categories layout   /////////////////////////////////////////////////////

    private RecyclerView categories_recyclerView;

    private FirebaseFirestore firebaseFirestore;

    ///////////////////////////////////// Tool Bar ////////////////////////////////////////////////
    private TextView Favorite_icon_number;
    private ImageView Favorites_page;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
           getWindow().setStatusBarColor(Color.TRANSPARENT) ;

        setContentView(R.layout.activity_home);
        firebaseFirestore = FirebaseFirestore.getInstance();
//        Toast.makeText(this, "The size of whish list is"+whishlistSize, Toast.LENGTH_SHORT).show();
        Favorite_icon_number = findViewById(R.id.favorites_number);
        Favorites_page = findViewById(R.id.favorites);


///////////////////////////////////////////////// Favorite IconNumbers ////////////////////////////////////////////
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                .document("MY_FAVORITES").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                           @RequiresApi(api = Build.VERSION_CODES.M)
                                           @Override
                                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                               if (task.isSuccessful()) {
                                                    Favorite_icon_number.setText (String.valueOf( (long)task.getResult().get("list_size")));
//                                                   Toast.makeText(Home.this, String.valueOf( (long)task.getResult().get("list_size") ), Toast.LENGTH_SHORT).show();

                                               }
                                           }
                                       });
        Favorites_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,Favorites.class));
            }
        });


        //////////////////////////// Fetch user data //////////

//        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull  Task<DocumentSnapshot> task) {
//                if (task.isSuccessful())
//                {
//                    User_Name = task.getResult().get("user_name").toString();
//                }
//            }
//        });

/////////////////////////////////////////////// Search Icon Code ///////////////////////////////////////////////////////

        ImageView SearchImage = findViewById(R.id.Searchicon);
        SearchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,Search_Activity.class));
            }
        });


//////////////////////////////////////////////// Search icon code Ended ////////////////////////////////////////////
/////////////////////////////////////////////// Banner Slider Code /////////////////////////////////////////////

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewpager_tab = (TabLayout) findViewById(R.id.my_tablayout);
        slider_modelList = new ArrayList<slider_model>();
        slider = new Slider(slider_modelList);
        viewPager.setAdapter(slider);
        viewpager_tab.setupWithViewPager(viewPager,true);

//        slider_modelList.add(new slider_model(R.drawable.grassland,"#012302"));
//        slider_modelList.add(new slider_model(R.drawable.pixbay,"#012302"));
//        slider_modelList.add(new slider_model(R.drawable.grassland,"#012302"));
//        slider_modelList.add(new slider_model(R.drawable.pixbay,"#012302"));
//        slider_modelList.add(new slider_model(R.drawable.bridge,"#012302"));
//        slider_modelList.add(new slider_model(R.drawable.grassland,"#012302"));
//        slider_modelList.add(new slider_model(R.drawable.pixbay,"#012302"));
//        slider_modelList.add(new slider_model(R.drawable.grassland,"#012302"));
//        slider_modelList.add(new slider_model(R.drawable.pixbay,"#012302"));
//        slider_modelList.add(new slider_model(R.drawable.bridge,"#012302"));

        firebaseFirestore.collection("BANNERS").orderBy("index").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        slider_modelList.add(new slider_model(queryDocumentSnapshot.get("icon").toString(),queryDocumentSnapshot.get("place").toString()));
                    }
                    slider.notifyDataSetChanged();
                }
            }
        });
        java.util.Timer timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new The_slide_timer(),2000,3000);

/////////////////////////////////////////////// Banner Slider Code Ended /////////////////////////////////////////////

/////////////////////////////////////////////////// Latest Tours code Recycler view ////////////////////////////////////////////

        latestToursTitle = findViewById(R.id.latest_tours_title);
        latestToursRecyclerView = findViewById(R.id.latest_tours_recyclerview);

        List<latesttoursmodel> latesttoursmodels = new ArrayList<>();
        latesttoursadapter LatestToursAdapter = new latesttoursadapter(latesttoursmodels);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Home.this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        latestToursRecyclerView.setLayoutManager(linearLayoutManager);
        latestToursRecyclerView.setAdapter(LatestToursAdapter);
        LatestToursAdapter.notifyDataSetChanged();

        firebaseFirestore.collection("TOP_TOURS").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull  Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshots : task.getResult())
                            {
                                latesttoursmodels.add(new latesttoursmodel(documentSnapshots.get("icon").toString(),documentSnapshots.get("title").toString(),Integer.parseInt(documentSnapshots.get("price").toString())));
                            }
                            LatestToursAdapter.notifyDataSetChanged();
                        }
                        else {
                            String error = task.getException().getMessage();
                            Toast.makeText(Home.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

//        latesttoursmodels.add(new latesttoursmodel(R.drawable.pixbay,"WaterLand","Rs.500/-"));
//        latesttoursmodels.add(new latesttoursmodel(R.drawable.bridge,"BridgePool","Rs.600/-"));
//        latesttoursmodels.add(new latesttoursmodel(R.drawable.grassland,"GrassLand","Rs.850/-"));
//        latesttoursmodels.add(new latesttoursmodel(R.drawable.pixbay,"WaterLand","Rs.500/-"));
//        latesttoursmodels.add(new latesttoursmodel(R.drawable.whatatriplogo,"RainBowLand","Rs.750/-"));
//        latesttoursmodels.add(new latesttoursmodel(R.drawable.bridge,"BridgePool","Rs.600/-"));
//        latesttoursmodels.add(new latesttoursmodel(R.drawable.grassland,"GrassLand","Rs.850/-"));
//        latesttoursmodels.add(new latesttoursmodel(R.drawable.pixbay,"WaterLand","Rs.500/-"));
//        latesttoursmodels.add(new latesttoursmodel(R.drawable.whatatriplogo,"RainBowLand","Rs.750/-"));
//        latesttoursmodels.add(new latesttoursmodel(R.drawable.bridge,"BridgePool","Rs.600/-"));
//        latesttoursmodels.add(new latesttoursmodel(R.drawable.grassland,"GrassLand","Rs.850/-"));
//        latesttoursmodels.add(new latesttoursmodel(R.drawable.forest,"Forest","Rs.900/-"));

///////////////////////////////////////////Latest Tours code Recycler view code Ended ///////////////////////////////////

///////////////////////////////////////////////////// Grid view //////////////////////////////////////////////

        List<latesttoursmodel> GridList = new ArrayList<>();
        GridTextView = findViewById(R.id.title_grid);
        Grid_view = findViewById(R.id.Grid_View);
        grid_product_layout_adapter Grid_Adapter = new grid_product_layout_adapter(GridList);
        Grid_view.setAdapter(Grid_Adapter);
        Grid_Recycler_View = findViewById(R.id.grid_recycle);
        Grid_Recycler_View.setHasFixedSize(true);
        Grid_Adapter.notifyDataSetChanged();
        firebaseFirestore.collection("TRENDING_TOURS").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull  Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshots : task.getResult())
                            {
                                GridList.add(new latesttoursmodel(documentSnapshots.get("icon").toString(),documentSnapshots.get("title").toString(),Integer.parseInt(documentSnapshots.get("price").toString())));
                            }
                            Grid_Adapter.notifyDataSetChanged();
                        }
                        else {
                            String error = task.getException().getMessage();
                            Toast.makeText(Home.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

//        List<stagged_model_recycler> stagged_model_recyclers = new ArrayList<>();
//        stagged_model_recyclers.add(new stagged_model_recycler(R.drawable.forest));
//        stagged_model_recyclers.add(new stagged_model_recycler(R.drawable.bridge));
//        stagged_model_recyclers.add(new stagged_model_recycler(R.drawable.pixbay));
//        stagged_model_recyclers.add(new stagged_model_recycler(R.drawable.grassland));

        Grid_Recycler_View.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        List<latesttoursmodel> staggedModel = new ArrayList<>();
        stagged_recyclerview_adapter staggedRecyclerviewAdapter = new stagged_recyclerview_adapter(staggedModel);
        Grid_Recycler_View.setAdapter(staggedRecyclerviewAdapter);
        staggedRecyclerviewAdapter.notifyDataSetChanged();
        firebaseFirestore.collection("TOP_TOURS").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull  Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshots : task.getResult())
                            {
                                staggedModel.add(new latesttoursmodel(documentSnapshots.get("icon").toString(),documentSnapshots.get("title").toString(),Integer.parseInt(documentSnapshots.get("price").toString())));
                            }
                            staggedRecyclerviewAdapter.notifyDataSetChanged();
                        }
                        else {
                            String error = task.getException().getMessage();
                            Toast.makeText(Home.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

////////////////////////////////////////////   Grid View Coded Ended /////////////////////////////////////////////

////////////////////////////////////////////   categories    //////////////////////////////////////////////////////
        categories_recyclerView = findViewById(R.id.Recycler_view_categories);
        List <categories_model> categories_models = new ArrayList<>();
        //        categories_models.add(new categories_model(R.drawable.forest,"Forests"));
//        categories_models.add(new categories_model(R.drawable.bonfire,"camp Fire"));
//        categories_models.add(new categories_model(R.drawable.mountain,"Mountains"));
//        categories_models.add(new categories_model(R.drawable.beach,"Beach"));
//
//        categories_models.add(new categories_model(R.drawable.forest,"Forests"));
//        categories_models.add(new categories_model(R.drawable.bonfire,"camp Fire"));
//        categories_models.add(new categories_model(R.drawable.mountain,"Mountains"));
//        categories_models.add(new categories_model(R.drawable.beach,"Beach"));
        categories_recycler_adapter categoriesRecyclerAdapter = new categories_recycler_adapter(categories_models);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(Home.this);
        linearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        categories_recyclerView.setLayoutManager(linearLayoutManager1);
        categories_recyclerView.setAdapter(categoriesRecyclerAdapter);
        categoriesRecyclerAdapter.notifyDataSetChanged();

        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull  Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshots : task.getResult())
                            {
                                categories_models.add(new categories_model(documentSnapshots.get("icon").toString(),documentSnapshots.get("categoriesname").toString()));
                            }
                            categoriesRecyclerAdapter.notifyDataSetChanged();
                        }
                        else {
                            String error = task.getException().getMessage();
                            Toast.makeText(Home.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

///////////////////////////////////////////     CATEGORIES CODE ENDED       /////////////////////////////////////


///////////////////////////////////////////  Firebase Code for Home page /////////////////////////////////////////
        FAuth = FirebaseAuth.getInstance();
        // Assign the drawer variable
        drawerLayout = findViewById(R.id.drawer_layout);
//        Logout = findViewById(R.id.logout);
//        verify = findViewById(R.id.verify);
//        email = findViewById(R.id.Emailver);
//        Logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(Home.this,Signin.class));
//                finish();
//            }
//        });
//        if (!FAuth.getCurrentUser().isEmailVerified()){
//            verify.setVisibility(View.VISIBLE);
//            email.setVisibility(View.VISIBLE);
//        }
//        verify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Toast.makeText(Home.this,"Verification Email Sent",Toast.LENGTH_SHORT).show();
//                        verify.setVisibility(View.GONE);
//                        email.setVisibility(View.GONE);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull  Exception e) {
//                        Toast.makeText(Home.this,e.getMessage(),Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
    }
    public void ClickMenu(View view){
        // Open The drawer
        OpenDrawer(drawerLayout);
    }

    private static void OpenDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public void ClickLogo(View view){
        closeDrawer(drawerLayout);
    }

    private void closeDrawer(DrawerLayout drawerLayout) {
        // Close drawer if it is open
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public void ClickHome(View view){
        recreate();
    }
    public void ClickDashBoard(View view){
        startActivity(new Intent(Home.this,Favorites.class));
    }
    public void ClickProfile(View view){
        startActivity(new Intent(Home.this,UserProfile.class));
    }
    public void ClickLogout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(Home.this,Signin.class));
        finish();
    }
    public void Clickbookings(View view)
    {
        startActivity(new Intent(Home.this,MyBookings.class));
    }

    public class The_slide_timer extends TimerTask {
        @Override
        public void run() {

            Home.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem()< slider_modelList.size()-1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                    }
                    else
                        viewPager.setCurrentItem(0);
                }
            });
        }
    }
}