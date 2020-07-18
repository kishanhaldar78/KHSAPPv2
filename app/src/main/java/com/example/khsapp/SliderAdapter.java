package com.example.khsapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private List<SliderModel> sliderModel;
    private ViewPager2 viewPager2;

    public SliderAdapter(List<SliderModel> sliderModel, ViewPager2 viewPager2) {
        this.sliderModel = sliderModel;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate
                        (R.layout.slider_layout,parent,false
                        )
        );


    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImageView(sliderModel.get(position));
        holder.bannerContainer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(sliderModel.get(position).getBackgroundColor())));
        if(position==sliderModel.size()-2){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return sliderModel.size();
    }



    class SliderViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private ConstraintLayout bannerContainer;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.banner_slide);
            this.bannerContainer=itemView.findViewById(R.id.banner_container);
        }

        void setImageView(SliderModel sliderItem){
            //if you want to display image from the internet
            //you can put code here using glide or picasso

            imageView.setImageResource(sliderItem.getBanner());
        }
    }

    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            sliderModel.addAll(sliderModel);
            notifyDataSetChanged();
        }
    };

}