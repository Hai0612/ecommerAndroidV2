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
import com.example.ecomapplication.models.Product;
import com.example.ecomapplication.models.SellerInfo;
import com.example.ecomapplication.models.SellerOrder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        Log.v("Date", String.valueOf(orderDate));

        holder.productName.setText(list.get(position).getId_product());
        holder.orderDate.setText(String.valueOf(ordered));
        holder.quantity.setText(String.valueOf(list.get(position).getQuantity()));
        holder.userInfo.setText(String.valueOf(list.get(position).getUser_name()));
        if (list.get(position).getStatus().equals("confirm")) {
            holder.buttonConfirm.setText("Đã xác nhận");
            holder.buttonConfirm.setEnabled(false);
            holder.buttonCancel.setEnabled(false);
            holder.buttonCancel.setVisibility(View.INVISIBLE);
        }else if (list.get(position).getStatus().equals("cancelled")) {
            holder.buttonConfirm.setEnabled(false);
            holder.buttonCancel.setEnabled(false);
            holder.buttonCancel.setText("Đã hủy bỏ");
            holder.buttonConfirm.setVisibility(View.INVISIBLE);

        } else {
            holder.buttonConfirm.setText("Xác nhận");
        }
        int i = position;
        holder.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("tagggggg", "huy");
                Toast.makeText(context, "Đã hủy đơn đặt hàng!", Toast.LENGTH_SHORT).show();
                holder.buttonCancel.setEnabled(false);
                holder.buttonCancel.setText("Đã hủy bỏ");
                holder.buttonConfirm.setVisibility(View.INVISIBLE);

                cancelOrder(i);
            }
        });

        holder.buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Đã xác nhận đơn đặt hàng!", Toast.LENGTH_SHORT).show();
                ConfirmOrder(i);
                holder.buttonConfirm.setEnabled(false);
                holder.buttonCancel.setVisibility(View.INVISIBLE);
                holder.buttonConfirm.setText("Đã xác nhận");
                holder.buttonCancel.setEnabled(false);
            }
        });
    }
    public void cancelOrder(int position ){
        db.collection("SellerOrder").document(list.get(position).getId_seller()).collection("Orders").document(list.get(position).getIdDocument()).update(
                "status", "cancelled");
        checkOrderStatus("cancelled",list.get(position).getId_order(), list.get(position).getId_user());
    }
//    public void checkOrderStatus(String id_order){
//        final String[] id_user = new String[1];
//        ArrayList<Product> listProductOfOrder = new ArrayList<>();
//        Log.v("fsdf", id_order);
//        db.collection("OrderDetail").document(id_order).collection("Products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (DocumentSnapshot doc :task.getResult().getDocuments()) {
////                        Log.v("Test", auth.getCurrentUser().getUid());
//                        Product product = doc.toObject(Product.class);
//                        product.setDocumentId(doc.getId());
//                        listProductOfOrder.add(product);
//
//                    }
//                    int[] check = {0};
//                    for(int i = 0 ; i <listProductOfOrder.size(); i++){
//                        Log.v("sellerOrder", listProductOfOrder.get(i).getId_seller());
//                        db.collection("SellerOrder").document(listProductOfOrder.get(i).getId_seller()).collection("Orders").whereEqualTo("id_order", id_order).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    for (DocumentSnapshot doc :task.getResult().getDocuments()) {
////                        Log.v("Test", auth.getCurrentUser().getUid());
//                                        SellerOrder order = doc.toObject(SellerOrder.class);
//                                        id_user[0] = order.getId_user();
//                                        if(order.getStatus().equals("confirm")){
//                                            check[0]++;
//                                            if(check[0] == listProductOfOrder.size()){
//                                                db.collection("Order").document(id_user[0]).collection("Orders").document(id_order).update(
//                                                        "status", "processed");
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        });
//                    }
//                }
//            }
//        });
//
//
//    }

    public void checkOrderStatus(String status, String id_order, String id_user){
        String newOderStatus = status.equals("confirm") ? "processed" : "cancelled";
        db.collection("Order").document(id_user).collection("Orders").document(id_order).update(
                "status", newOderStatus);
    }
    private void onClickGoToDetail(SellerOrder orderModel) {
//        Intent intent = new Intent(context, OrderDetailActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("object_order", orderModel);
//        intent.putExtras(bundle);
//        context.startActivity(intent);
    }
    public void ConfirmOrder(int position){
        db.collection("SellerOrder").document(list.get(position).getId_seller()).collection("Orders").document(list.get(position).getIdDocument()).update(
                "status", "confirm");
        checkOrderStatus("confirm",list.get(position).getId_order(), list.get(position).getId_user());

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
