package com.example.ecomapplication.adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    Context context;
    List<OrderModel> list;
//    private FirebaseStorage storage;
    FirebaseFirestore db;
    private FirebaseAuth auth;

    public OrderAdapter(Context context, List<OrderModel> list) {
        this.context = context;
        this.list = list;
//        this.storage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        return new OrderAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_item, parent, false));
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
        holder.status_order.setText(list.get(position).getStatus());
        holder.total.setText(String.valueOf(list.get(position).getTotal()));
        if(list.get(position).getStatus().equals("pending")){
            holder.buttonReceived.setEnabled(false);
            holder.status_order.setText("Đang chờ xử lí đơn hàng");
            holder.buttonReceived.setText("Đang chờ xử lí đơn hàng");
        }else if(list.get(position).getStatus().equals("processed")){
            holder.buttonReceived.setEnabled(true);
            holder.status_order.setText("Đã xử lí đơn hàng");
            holder.buttonReceived.setText("Nhận đơn hàng");

        }else if(list.get(position).getStatus().equals("cancelled")){
            holder.buttonReceived.setEnabled(false);
            holder.status_order.setText("Đơn hàng bị hủy bỏ");
            holder.buttonReceived.setText("Đặt hàng không thành công");
            holder.buttonReceived.setBackgroundColor(0xFFF38E99);

        }else if(list.get(position).getStatus().equals("received")){
            holder.buttonReceived.setEnabled(false);
            holder.status_order.setText("Đã nhận đơn hàng");
            holder.buttonReceived.setBackgroundColor(0xFF9EE639);
            holder.buttonReceived.setText("Đã nhận hàng");
        }
        int id = position;
        holder.buttonReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.buttonReceived.setText("Đã nhận hàng");
                holder.status_order.setText("Đã nhận đơn hàng");
                holder.buttonReceived.setBackgroundColor(0xFF9EE639);
                holder.buttonReceived.setEnabled(false);
                ReceiveOrder(id);
                Toast.makeText(context, "Bạn đã xác nhận hàng thành công!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ReceiveOrder(int position){
        db.collection("Order").document(auth.getUid()).collection("Orders").document(list.get(position).getId()).update(
                "status", "received");
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
        TextView orderAddress, orderDate, shippedDate, total, status_order;
        RelativeLayout layoutItem;
        Button buttonReceived;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderAddress = itemView.findViewById(R.id.order_address);
            orderDate = itemView.findViewById(R.id.order_date);
            shippedDate = itemView.findViewById(R.id.shipped_date);
            total = itemView.findViewById(R.id.total);
            status_order = itemView.findViewById(R.id.status_order);
            layoutItem = itemView.findViewById(R.id.order_item);
            buttonReceived = itemView.findViewById(R.id.btn_received);
        }
    }
}
