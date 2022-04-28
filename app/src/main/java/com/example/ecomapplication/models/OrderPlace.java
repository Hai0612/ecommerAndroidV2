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

    public Activity activity;
    public Dialog d;
    public Button yes, no;
    private TextView addressText;
    private TextView totalText;
    FirebaseFirestore db;
    private String orderAddress;
    private  String id_user;
    private Date orderDate;
    private Date shippedDate;
     private int number;
     private String id;
    private int total;
    public OrderPlace(Activity activity , String orderAddress, int total ,String id_user  , Date orderDate , Date shippedDate, int number) {
        super(activity);
        this.activity = activity;
        this.orderAddress = orderAddress;
        this.total = total;
        this.id_user = id_user;
        this.orderDate = orderDate;
        this.shippedDate = shippedDate;
        this.number = number;
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
        addressText.setText(orderAddress);

        yes.setOnClickListener(this);

        no.setOnClickListener(this);

    }
    public void orderPlace(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date d  = Calendar.getInstance().getTime();
        Map<String, Object> order = new HashMap<>();
        id = getAlphaNumericString(5);
        order.put("id", id);
        order.put("id_user", id_user);
        order.put("orderAddress", orderAddress);
        order.put("orderDate", orderDate);
        order.put("shippedDate", shippedDate);
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
        order.put(id, 1);
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
    public String getAlphaNumericString(int n)
    {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}