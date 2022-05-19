package com.example.ecomapplication.adapters;


import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecomapplication.R;
import com.example.ecomapplication.activities.OrderDetailActivity;
import com.example.ecomapplication.models.OrderModel;
import com.example.ecomapplication.models.SellerInfo;
import com.example.ecomapplication.models.SellerOrder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderAdapterSeller extends RecyclerView.Adapter<OrderAdapterSeller.ViewHolder> {
    Context context;
    List<SellerOrder> list;
//    private FirebaseStorage storage;
    FirebaseFirestore db;
    private FirebaseAuth auth;

    public OrderAdapterSeller(Context context, List<SellerOrder> list) {
        this.context = context;
        this.list = list;
//        this.storage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        return new OrderAdapterSeller.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_seller, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SellerOrder orderModel = list.get(position);
//        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onClickGoToDetail(orderModel);
//            }
//        });
        Date ordered = list.get(position).getOrderDate();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String orderDate = dateFormat.format(ordered);

        holder.productName.setText(list.get(position).getId_product());
        holder.orderDate.setText(orderDate);
        holder.quantity.setText(String.valueOf(list.get(position).getQuantity()));
        holder.userInfo.setText(String.valueOf(list.get(position).getId_user()));
        Log.v("confirm" ,list.get(position).getStatus());
        if (list.get(position).getStatus().equals("confirm")) {
            holder.buttonConfirm.setText("Đã xác nhận");
            holder.buttonConfirm.setEnabled(false);
            holder.buttonCancel.setEnabled(false);

        } else {
            holder.buttonConfirm.setText("Xác nhận");
        }
        holder.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.buttonCancel.setEnabled(false);
            }
        });
        int i = position;
        holder.buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Đã xác nhận đơn đặt hàng!", Toast.LENGTH_SHORT).show();
                ConfirmOrder(i);
                holder.buttonConfirm.setEnabled(false);
                holder.buttonConfirm.setText("Đã xác nhận");
                holder.buttonCancel.setEnabled(false);


            }
        });
    }

    private void onClickGoToDetail(SellerOrder orderModel) {
//        Intent intent = new Intent(context, OrderDetailActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("object_order", orderModel);
//        intent.putExtras(bundle);
//        context.startActivity(intent);
    }
    public void ConfirmOrder(int position){
        Log.v("comfim" , list.get(position).getIdDocument());
        db.collection("SellerOrder").document(list.get(position).getId_seller()).collection("Orders").document(list.get(position).getIdDocument()).update(
                "status", "confirm");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userInfo, orderDate, productName, quantity;
        RelativeLayout layoutItem;
        Button buttonCancel, buttonConfirm;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDate = itemView.findViewById(R.id.order_date);
            productName = itemView.findViewById(R.id.product_name);
            quantity = itemView.findViewById(R.id.quantity);
            userInfo = itemView.findViewById(R.id.user_info);
            buttonCancel = itemView.findViewById(R.id.btn_cancel);
            buttonConfirm = itemView.findViewById(R.id.btn_confirm);
        }
    }
}
