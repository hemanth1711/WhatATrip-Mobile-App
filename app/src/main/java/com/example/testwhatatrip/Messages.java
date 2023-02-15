package com.example.testwhatatrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class Messages extends AppCompatActivity {
    ImageView Message_Profile;
    TextView Message_UName;
    ImageView Message_Send_Btn;
    EditText Send_Message;
    String Messages;
    FirebaseDatabase database;
    String SenderRoom,ReceiverRoom;
    String ReceiverUid;
    String SenderUid;
    RecyclerView chat_recycler;
    ArrayList<MessagesModel> MessagesArrayList;
    MessagesAdapter messagesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        Message_Profile = findViewById(R.id.Message_P);
        Message_UName = findViewById(R.id.Message_T);
        Message_Send_Btn = findViewById(R.id.Send_Message_btn);
        Send_Message = findViewById(R.id.send_message);
        chat_recycler = findViewById(R.id.ChatRecyclerView);
        MessagesArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        chat_recycler.setLayoutManager(linearLayoutManager);
        messagesAdapter = new MessagesAdapter(Messages.this,MessagesArrayList);
        chat_recycler.setAdapter(messagesAdapter);
        database = FirebaseDatabase.getInstance();
        String UName = getIntent().getStringExtra("UserName");
        String  MProfile= getIntent().getStringExtra("Profile");
        ReceiverUid = getIntent().getStringExtra("review_uid");
        SenderUid = FirebaseAuth.getInstance().getUid();
        Message_UName.setText(UName);
        Toast.makeText(this, ReceiverUid, Toast.LENGTH_SHORT).show();
        Glide.with(Messages.this).load(MProfile).apply(new RequestOptions().placeholder(R.drawable.whatatriplogopic)).into(Message_Profile);
        SenderRoom = SenderUid+ReceiverUid;
        ReceiverRoom = ReceiverUid+SenderUid;
        DatabaseReference chatReference = database.getReference().child("chats").child(SenderRoom).child("messages");
        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                MessagesArrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    MessagesModel messages = dataSnapshot.getValue(MessagesModel.class);
                    MessagesArrayList.add(messages);
                }
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });



        Message_Send_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Messages = Send_Message.getText().toString();
                if(Messages.isEmpty())
                {
                    Toast.makeText(Messages.this, "Enter A message to Send", Toast.LENGTH_SHORT).show();
                    return;
                }
                Send_Message.setText("");
                Date date = new Date();
                MessagesModel messages = new MessagesModel(Messages, SenderUid, date.getTime());
                database = FirebaseDatabase.getInstance();
                database.getReference().child("chats")
                        .child(SenderRoom)
                        .child("messages")
                        .push()
                        .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        database.getReference().child("chats")
                                .child(ReceiverRoom)
                                .child("messages")
                                .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull  Task<Void> task) {

                            }
                        });
                    }
                });
            }
        });

    }
}