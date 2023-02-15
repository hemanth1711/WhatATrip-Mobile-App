package com.example.testwhatatrip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MyBookings extends AppCompatActivity {
    private RecyclerView bookings_recycler_view;
    private ImageView back_to_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);
        back_to_home = findViewById(R.id.back_bookings);
        bookings_recycler_view = findViewById(R.id.MyBookingsRecyclerView);
        MyBookings_data.myorder_item_models.clear();
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyBookings.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bookings_recycler_view.setLayoutManager(layoutManager);
        MyBokkingsAdapter my_order_adapter= new MyBokkingsAdapter(MyBookings_data.myorder_item_models);
        bookings_recycler_view.setAdapter(my_order_adapter);
        if(MyBookings_data.myorder_item_models.size() == 0){
            MyBookings_data.loadOrders(MyBookings.this,my_order_adapter);
        }
        my_order_adapter.notifyDataSetChanged();

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyBookings.this,Home.class));
                finish();
            }
        });
    }
}