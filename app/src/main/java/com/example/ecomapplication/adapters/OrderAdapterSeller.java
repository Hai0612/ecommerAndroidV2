package com.example.ecomapplication.adapters;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderAdapterSeller extends RecyclerView.Adapter<OrderAdapterSeller.ViewHolder> {
    Context context;
    List<OrderModel> list;
//    private FirebaseStorage storage;

    public OrderAdapterSeller(Context context, List<OrderModel> list) {
        this.context = context;
        this.list = list;
//        this.storage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderAdapterSeller.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_seller, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel orderModel = list.get(position);
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGoToDetail(orderModel);
            }
        });

        Date ordered = list.get(position).getOrderDate();
        Date shipped = list.get(position).getShippedDate();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String orderDate = dateFormat.format(ordered);
        String shippedDate = dateFormat.format(shipped);

        holder.orderAddress.setText(list.get(position).getOrderAddress());
        holder.orderDate.setText(orderDate);
        holder.shippedDate.setText(shippedDate);
        holder.total.setText(String.valueOf(list.get(position).getTotal()));

        holder.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Button Cancel is clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        holder.buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Button Confirm is clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onClickGoToDetail(OrderModel orderModel) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_order", orderModel);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderAddress, orderDate, shippedDate, total;
        RelativeLayout layoutItem;
        Button buttonCancel, buttonConfirm;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderAddress = itemView.findViewById(R.id.order_address);
            orderDate = itemView.findViewById(R.id.order_date);
            shippedDate = itemView.findViewById(R.id.shipped_date);
            total = itemView.findViewById(R.id.total);
            layoutItem = itemView.findViewById(R.id.order_item);
            buttonCancel = itemView.findViewById(R.id.btn_cancel);
            buttonConfirm = itemView.findViewById(R.id.btn_confirm);
        }
    }
}