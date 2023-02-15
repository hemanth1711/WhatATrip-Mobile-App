package com.example.testwhatatrip;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class Slider extends PagerAdapter {
    Context context;
    private List<slider_model> sliderModel;

    public Slider(List<slider_model> sliderModel) {
        this.sliderModel = sliderModel;
    }

    @Override
    public int getCount() {
        return sliderModel.size();
    }

    @Override
    public boolean isViewFromObject( View view,Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem( ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item,container,false);
        ImageView imageView = (ImageView) view.findViewById(R.id.View_image);
        TextView textView = view.findViewById(R.id.TextSlide);
        textView.setVisibility(View.VISIBLE);
        textView.setText(sliderModel.get(position).getTitle());
        Glide.with(container.getContext()).load(sliderModel.get(position).getBanner()).apply(new RequestOptions().placeholder(R.drawable.whatatriplogopic)).into(imageView);
        container.addView(view,0);

        // When Click on image
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(container.getContext(),"you clicked image: "+ (position+1),Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container,int position ,Object object) {
        container.removeView((View) object);
    }
}
