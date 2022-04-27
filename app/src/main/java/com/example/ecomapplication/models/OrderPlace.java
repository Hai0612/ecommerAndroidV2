package com.example.ecomapplication.models;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.ecomapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OrderPlace extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    private TextView addressText;
    private TextView totalText;
    FirebaseFirestore db;

    private String address;
    private int total;
    public OrderPlace(Activity a , String address, int total) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.address = address;
        this.total = total;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.orderplace_dialog);
        db = FirebaseFirestore.getInstance();
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        totalText = findViewById(R.id.total_order);
        addressText = findViewById(R.id.address_order);
        totalText.setText(String.valueOf(total));
        addressText.setText(address);

        yes.setOnClickListener(this);

        no.setOnClickListener(this);

    }
    public void orderPlace(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date d  = Calendar.getInstance().getTime();
        Map<String, Object> order = new HashMap<>();
        order.put("id", "123");
        order.put("id_user", "1");
        order.put("orderAddress", address);
        order.put("orderDate", d);
        order.put("shippedDate", d);
        order.put("total", total);


// Add a new document with a generated ID
        db.collection("Order")
                .add(order)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        AddProductListToOrderDetail();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Them order that bai", e);
                    }
                });

    }
    public void AddProductListToOrderDetail(){
        Map<String, Object> order = new HashMap<>();
        order.put("id_order", 1);
        order.put("id_product", "2");
        order.put("product_quantity", 2);


// Add a new document with a generated ID
        db.collection("OrderDetail")
                .add(order)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.v(TAG, "ADD order detail thanh cong");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Them order detail that bai", e);
                    }
                });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:{
                orderPlace();
            }
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}