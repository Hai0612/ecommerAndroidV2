package com.example.ecomapplication.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ecomapplication.R;
import com.example.ecomapplication.models.MyCartModel;
import com.example.ecomapplication.models.Product;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class CheckoutAdapter  extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {

    private Context context;
    private List<MyCartModel> products;
    private FirebaseStorage storage;

    public CheckoutAdapter(Context context, List<MyCartModel> products) {
        this.context = context;
        this.products = products;
        this.storage = FirebaseStorage.getInstance();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_checkout_item , parent, false)) ;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Glide.with(context).load(popularProductAdapterList.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(products.get(position).getName());
        holder.quantity.setText(String.valueOf(products.get(position).getQuantity()));
//        holder.price.setText((Integer) products.get(position).getPrice());
        final long ONE_MEGABYTE = 1024 * 1024;
        StorageReference storageReference = storage.getReferenceFromUrl(products.get(position).getImg_url());

        // Dat anh lay tu Firebase cho item
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            DisplayMetrics displayMetrics = new DisplayMetrics();

            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            }

            holder.img_url.setImageBitmap(bitmap);
        }).addOnFailureListener(exception -> {
            // Handle any errors
            Log.v("Error", "Error when get the images");
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_url;
        TextView name, price,quantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_url = itemView.findViewById(R.id.img_url);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);

        }
    }
}
