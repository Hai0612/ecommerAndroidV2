package com.example.ecomapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecomapplication.R;
import com.example.ecomapplication.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Objects;

//public class MyCartAdapter extends FirestoreRecyclerAdapter.Adapter<MyCartAdapter.ViewHolder> {
public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {
    Context context;
    List<MyCartModel> list;
    private FirebaseStorage storage;
    int totalAmount = 0;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private MyCartAdapter cartAdapter;

    public MyCartAdapter(Context context, List<MyCartModel> list) {
        this.context = context;
        this.list = list;
        this.storage = FirebaseStorage.getInstance();
        this.cartAdapter = this;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_test, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        int ref = getItemViewType(position);
//        getSnapshots().getSnapshot(position).getId();
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        MyCartModel cartModel = list.get(position);
        String id_document = list.get(position).getDocumentId();
        holder.buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, ref + "", Toast.LENGTH_SHORT).show();
                if (cartModel.getQuantity() <= 1) {
                    return;
                } else {
                        firestore.collection("Cart").document("SXcZhdR7152RN49UawTz")
                            .collection("Products").document(id_document)
                            .update("quantity", cartModel.getQuantity() - 1);
                        list.get(position).setQuantity(cartModel.getQuantity() - 1);
                        holder.quantity.setText(String.valueOf(list.get(position).getQuantity()) );

                    //reload??
//                    cartAdapter.notifyDataSetChanged();
//                    listener.refreshActivity();
                }
                int totalCart = 0;
                for(int i = 0 ; i< list.size() ; i++){
                    totalCart = list.get(i).getPrice() * list.get(i).getQuantity();
                }
                int totalPrice = list.get(position).getPrice() * list.get(position).getQuantity();
                holder.price.setText(String.valueOf(totalPrice));
                Intent intent = new Intent("MyTotalAmount");
                intent.putExtra("totalAmount", totalCart);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }

        });
        holder.buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, cartModel.getQuantity() + "", Toast.LENGTH_SHORT).show();
                firestore.collection("Cart").document("SXcZhdR7152RN49UawTz")
                        .collection("Products").document(id_document)
                        .update("quantity", cartModel.getQuantity() + 1);
                list.get(position).setQuantity(cartModel.getQuantity() + 1);
                holder.quantity.setText(String.valueOf(list.get(position).getQuantity()) );

//                reload?
                int totalCart = 0;
                for(int i = 0 ; i< list.size() ; i++){
                    totalCart = list.get(i).getPrice() * list.get(i).getQuantity();
                }
                int totalPrice = list.get(position).getPrice() * list.get(position).getQuantity();
                holder.price.setText(String.valueOf(totalPrice));
                Intent intent = new Intent("MyTotalAmount");
                intent.putExtra("totalAmount", totalCart);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });

        int totalPrice = list.get(position).getPrice() * list.get(position).getQuantity();
        totalAmount = totalAmount + totalPrice;
        Intent intent = new Intent("MyTotalAmount");
        intent.putExtra("totalAmount", totalAmount);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

        holder.name.setText(list.get(position).getName());
        holder.quantity.setText(String.valueOf(list.get(position).getQuantity()));
//        holder.price.setText(String.valueOf(list.get(position).getPrice()));
        holder.price.setText(String.valueOf(totalPrice));

//        Load IMG
        final long ONE_MEGABYTE = 1024 * 1024;
        StorageReference storageReference = storage.getReferenceFromUrl(list.get(position).getImg_url());

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
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_url;
        //        TextView name, price, quantity, img_url;
        TextView name, price, quantity;
        LinearLayout layoutItem;
        Button buttonMinus, buttonPlus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            img_url = itemView.findViewById(R.id.img_url);
            quantity = itemView.findViewById(R.id.quantity);
            layoutItem = itemView.findViewById(R.id.cart_item);
            buttonMinus = itemView.findViewById(R.id.button_minus);
            buttonPlus = itemView.findViewById(R.id.button_plus);
        }
    }
}
