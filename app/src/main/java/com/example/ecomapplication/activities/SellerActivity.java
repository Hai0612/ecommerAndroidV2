package com.example.ecomapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ecomapplication.R;
import com.example.ecomapplication.adapters.AddressAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class SellerActivity extends AppCompatActivity {

    ImageButton logOut, editProfile, addProduct;
    TextView userName, shopName, email, productTab, orderTab;
    RelativeLayout productRl, orderRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_seller_home);

        logOut = findViewById(R.id.logout_btn);
        editProfile = findViewById(R.id.edit_profile_btn);
        addProduct = findViewById(R.id.add_product_btn);
        userName = findViewById(R.id.username_seller);
        shopName = findViewById(R.id.shop_name);
        email = findViewById(R.id.shop_email);
        productTab = findViewById(R.id.product_tab);
        orderTab = findViewById(R.id.order_tab);
        productRl = findViewById(R.id.productRl);
        orderRl = findViewById(R.id.orderRl);
        showProductUi();

        productTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProductUi();
            }
        });

        orderTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOrderUi();
            }
        });
    }

    public void showProductUi(){
        //show products and hide orders
        productRl.setVisibility(View.VISIBLE);
        orderRl.setVisibility(View.GONE);

        productTab.setTextColor(getResources().getColor(R.color.black));
        productTab.setBackgroundResource(R.drawable.shape_rect02);

        orderTab.setTextColor(getResources().getColor(R.color.white));
        orderTab.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    public void showOrderUi(){
        //show orders and hide products
        productRl.setVisibility(View.GONE);
        orderRl.setVisibility(View.VISIBLE);

        productTab.setTextColor(getResources().getColor(R.color.white));
        productTab.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        orderTab.setTextColor(getResources().getColor(R.color.black));
        orderTab.setBackgroundResource(R.drawable.shape_rect02);
    }

}
