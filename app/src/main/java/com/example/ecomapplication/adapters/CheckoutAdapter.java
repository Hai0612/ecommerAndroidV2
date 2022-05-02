package com.example.ecomapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ecomapplication.R;
import com.example.ecomapplication.models.Product;

import java.util.List;

public class CheckoutAdapter  extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {

    private Context context;
    private List<Product> products;

    public CheckoutAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_items , parent, false)) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Glide.with(context).load(popularProductAdapterList.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(products.get(position).getName());
//        holder.price.setText((Integer) products.get(position).getPrice());

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.all_img);
            name = itemView.findViewById(R.id.payment_provider);
            price = itemView.findViewById(R.id.all_price);

        }
    }
}
