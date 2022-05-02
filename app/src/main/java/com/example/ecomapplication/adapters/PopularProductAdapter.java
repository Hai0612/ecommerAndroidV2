package com.example.ecomapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecomapplication.R;
import com.example.ecomapplication.activities.DetailActivity;
import com.example.ecomapplication.models.Product;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PopularProductAdapter extends RecyclerView.Adapter<PopularProductAdapter.ViewHolder> {

    private Context context;
    private FirebaseStorage storage;

    private List<Product> products;

    public PopularProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
        this.storage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_items , parent, false)) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.productId = products.get(position).getProductId();
        holder.name.setText(products.get(position).getName());
        holder.price.setText(products.get(position).getPrice());

        StorageReference storageReference = storage.getReferenceFromUrl(products.get(position).getImg_url());

        // Dat anh lay tu Firebase cho item
        storageReference.getDownloadUrl()
                .addOnSuccessListener(uri -> Picasso.with(context).load(uri.toString()).into(holder.imageView))
                .addOnFailureListener(e -> Log.v("Error", "Error when get the images: " + e));

        holder.setItemClickListener((view, clickPosition, isLongClick) -> {
            if (isLongClick) {
                Toast.makeText(context, "Long click on product ID: " + holder.productId, Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("productDetail", products.get(position));

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public ImageView imageView;
        public TextView name, price;
        public String productId;
        private ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            imageView = itemView.findViewById(R.id.all_img);
            name = itemView.findViewById(R.id.all_product_name);
            price = itemView.findViewById(R.id.all_price);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), true);

            return true;
        }
    }

    public interface ItemClickListener {
        void onClick(View view, int position,boolean isLongClick);
    }
}
