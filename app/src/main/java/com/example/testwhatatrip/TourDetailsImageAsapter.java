package com.example.testwhatatrip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class TourDetailsImageAsapter extends PagerAdapter {
    Context context;
    private List<slider_model> sliderModel;

    public TourDetailsImageAsapter(List<slider_model> sliderModel) {
        this.sliderModel = sliderModel;
    }

    @Override
    public int getCount() {
        return sliderModel.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull  View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(@NonNull  ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.tourdetailimage,container,false);
        ImageView imageView = view.findViewById(R.id.tour_detail_image);
        Glide.with(container.getContext()).load(sliderModel.get(position).getBanner()).apply(new RequestOptions().placeholder(R.drawable.whatatriplogopic)).into(imageView);
//        imageView.setImageResource(sliderModel.get(position).getBanner());
        container.addView(view,0);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(container.getContext(),"you clicked image: "+ (position+1),Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    @Override
    public void destroyItem(@NonNull  ViewGroup container, int position, @NonNull  Object object) {
        container.removeView( (View) object);
    }
}
