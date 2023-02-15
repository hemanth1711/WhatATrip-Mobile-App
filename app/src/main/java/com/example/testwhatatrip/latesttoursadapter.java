package com.example.testwhatatrip;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class latesttoursadapter extends RecyclerView.Adapter<latesttoursadapter.ViewHolder> {
    private List<latesttoursmodel> latesttoursmodels;

    public latesttoursadapter(List<latesttoursmodel> latesttoursmodels) {
        this.latesttoursmodels = latesttoursmodels;
    }

    @NonNull
    @Override
    public latesttoursadapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.latest_tours_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull latesttoursadapter.ViewHolder holder, int position) {
        String resource = latesttoursmodels.get(position).getTourImage();
        String Title = latesttoursmodels.get(position).getTourTitle();
        int Prize = latesttoursmodels.get(position).getTourPrize();
        holder.setTourPrize(Prize);
        holder.setTourImage(resource);
        holder.setTourTitle(Title);
        holder.setTourname(Title);
    }

    @Override
    public int getItemCount() {
        return latesttoursmodels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView TourImage;
        TextView TourTitle,TourPrize;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            TourImage = itemView.findViewById(R.id.booking_image);
            TourTitle = itemView.findViewById(R.id.tourtitle);
            TourPrize = itemView.findViewById(R.id.LatestToursPrize);
        }
        private void setTourImage(String Resource)
        {
            Glide.with(itemView.getContext()).load(Resource).apply(new RequestOptions().placeholder(R.drawable.whatatriplogopic)).into(TourImage);
        }
        private void setTourname(String Title)
        {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), tourdetails.class);
                    intent.putExtra("tour_name",Title);
                    itemView.getContext().startActivity(intent);

                }
            });
        }
        private void  setTourTitle(String Title){
            TourTitle.setText(Title);
        }
        private void  setTourPrize(int prize){
            TourPrize.setText("Rs."+prize);
        }
    }
}
