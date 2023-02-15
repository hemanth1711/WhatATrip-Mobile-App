package com.example.testwhatatrip;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;

import org.json.JSONObject;

import java.util.UUID;

public class Payment extends AppCompatActivity {
    private TextView bookingCode,PayToHome;
    private String OrderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        OrderId = UUID.randomUUID().toString().substring(0,28);
        bookingCode = findViewById(R.id.BookingCode);
        PayToHome = findViewById(R.id.pay_to_home);
        bookingCode.setText("Your booking code is"+OrderId);
        PayToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Payment.this,Home.class));
                finish();
            }
        });

    }

}