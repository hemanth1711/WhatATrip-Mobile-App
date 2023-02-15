package com.example.testwhatatrip;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class grid_product_layout_adapter extends BaseAdapter {
    private List<latesttoursmodel> gridList;

    public grid_product_layout_adapter(List<latesttoursmodel> gridList) {
        this.gridList = gridList;
    }

    @Override
    public int getCount() {
        if (gridList.size()<=4) {
            return gridList.size();
        }
        else {
            return 4;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item,null);
            RoundedImageView ProductImage = view.findViewById(R.id.imageViewg);
            TextView ProductTitle = view.findViewById(R.id.round_image_titleg);
            TextView ProductPrice = view.findViewById(R.id.round_prizeg);
            int ProductPric = gridList.get(position).getTourPrize();
            String ProductTitl = gridList.get(position).getTourTitle();
            ProductTitle.setText(gridList.get(position).getTourTitle());
            ProductPrice.setText("Rs. " + ProductPric);
            Glide.with(parent.getContext()).load(gridList.get(position).getTourImage()).apply(new RequestOptions().placeholder(R.drawable.whatatriplogopic)).into(ProductImage);

            ProductImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(view.getContext(), tourdetails.class);
                    intent.putExtra("tour_name",ProductTitl);
                    view.getContext().startActivity(intent);
                }
            });

        }else {
            view = convertView;
        }
        return view;
    }
}
