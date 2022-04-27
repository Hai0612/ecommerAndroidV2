package com.example.ecomapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ecomapplication.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private List<SlideItemHome> slideItemHomes ;
    private ViewPager2 viewPager2;

    public SliderAdapter(List<SlideItemHome> slideItemHomes, ViewPager2 viewPager2) {
        this.slideItemHomes = slideItemHomes;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slide_item_home,parent,false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(slideItemHomes.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class SliderViewHolder extends RecyclerView.ViewHolder{
        private RoundedImageView imageView;
        public SliderViewHolder(@NonNull View itemView){
            super(itemView);

            imageView = itemView.findViewById(R.id.imageSlide_home);

        }
        void setImage(SlideItemHome sliderItem){
            imageView.setImageResource(sliderItem.getImage());
        }
    }
}
