package com.example.testwhatatrip;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class stagged_recyclerview_adapter extends RecyclerView.Adapter<stagged_recyclerview_adapter.ViewHolder> {
    private List<latesttoursmodel> latesttoursmodels;

    public stagged_recyclerview_adapter(List<latesttoursmodel> latesttoursmodelList) {
        this.latesttoursmodels = latesttoursmodelList;
    }

    @NonNull
    @Override
    public stagged_recyclerview_adapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stagged_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  stagged_recyclerview_adapter.ViewHolder holder, int position) {
        String Image = latesttoursmodels.get(position).getTourImage();
        holder.text.setText(latesttoursmodels.get(position).getTourTitle());
        holder.prize.setText("RS."+latesttoursmodels.get(position).getTourPrize());
        holder.setImage(Image);
    }

    @Override
    public int getItemCount() {
        return latesttoursmodels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView image;
        TextView text;
        TextView prize;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageViewR);
            text = itemView.findViewById(R.id.round_image_title);
            prize = itemView.findViewById(R.id.round_prize);
        }
        private void setImage(String imagei)
        {
            Glide.with(itemView.getContext()).load(imagei).apply(new RequestOptions().placeholder(R.drawable.whatatriplogopic)).into(image);
        }
    }
}
