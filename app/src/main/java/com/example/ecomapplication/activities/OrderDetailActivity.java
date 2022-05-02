package com.example.ecomapplication.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecomapplication.R;
import com.example.ecomapplication.models.OrderModel;


public class OrderDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        OrderModel orderModel = (OrderModel) bundle.get("object_order");

        TextView textViewOderDetail = findViewById(R.id.order_detail);
        textViewOderDetail.setText(orderModel.getOrderAddress());
    }
}
