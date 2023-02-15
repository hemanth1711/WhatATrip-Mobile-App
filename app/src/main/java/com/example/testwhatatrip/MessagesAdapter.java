package com.example.testwhatatrip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<MessagesModel> MessagesArrayList;

    public MessagesAdapter(Context context, ArrayList<MessagesModel> messagesArrayList) {
        this.context = context;
        MessagesArrayList = messagesArrayList;
    }

    int ITEM_SEND = 1;
    int ITEM_RECEIVE = 2;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        if(viewType == ITEM_SEND)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_chat_layout,parent,false);
            return new SenderViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciver_chat_layout,parent,false);
            return new ReciverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
        MessagesModel messagesModel = MessagesArrayList.get(position);

        if(holder.getClass()==SenderViewHolder.class)
        {
            SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
            senderViewHolder.textView.setText(messagesModel.getMessage());
        }
        else {
            ReciverViewHolder reciverViewHolder= (ReciverViewHolder) holder;
            reciverViewHolder.textView.setText(messagesModel.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return MessagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessagesModel messages = MessagesArrayList.get(position);
        if(FirebaseAuth.getInstance().getUid().equals(messages.getUid()))
        {
            return ITEM_SEND;
        }
        else {
            return ITEM_RECEIVE;
        }
    }

    class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public SenderViewHolder(@NonNull  View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.sender_chat_message);
        }
    }

    class ReciverViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public ReciverViewHolder(@NonNull  View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.reciver_chat_message);
        }
    }
}
