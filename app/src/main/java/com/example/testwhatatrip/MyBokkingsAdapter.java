package com.example.testwhatatrip;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.List;

public class MyBokkingsAdapter extends RecyclerView.Adapter<MyBokkingsAdapter.ViewHolder> {
    private List<MyBookingsModel> myBookingsModel;

    public MyBokkingsAdapter(List<MyBookingsModel> myBookingsModel) {
        this.myBookingsModel = myBookingsModel;
    }

    @NonNull
    @Override
    public MyBokkingsAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_bookings_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  MyBokkingsAdapter.ViewHolder holder, int position) {
        String Resouce = myBookingsModel.get(position).getImage();
        int price = myBookingsModel.get(position).getPrice();
        String Title = myBookingsModel.get(position).getTitle();
        Date date = myBookingsModel.get(position).getDate();
        holder.set_bokking_image(Resouce);
        holder.set_bokking_price(price);
        holder.set_bokking_title(Title);
        holder.set_booking_date(date);
    }

    @Override
    public int getItemCount() {
        return myBookingsModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView BookingsImage;
        TextView Title,Price,date1;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            BookingsImage = itemView.findViewById(R.id.booking_image);
            Title = itemView.findViewById(R.id.booking_title);
            Price = itemView.findViewById(R.id.booking_price);
            date1 = itemView.findViewById(R.id.booking_date);
        }
        public void set_bokking_image(String Resource)
        {
            Glide.with(itemView.getContext()).load(Resource).into(BookingsImage);
        }
        public void set_bokking_price(int price)
        {
            Price.setText("Rs."+price+"/-");
        }
        public void set_bokking_title(String title)
        {
            Title.setText(title);
        }
        public void set_booking_date(Date date)
        {
            date1.setText("Booked the package on: "+date);
        }
    }
}
