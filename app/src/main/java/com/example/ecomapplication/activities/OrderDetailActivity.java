package com.example.ecomapplication.activities;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecomapplication.R;
import com.example.ecomapplication.adapters.ProductAdapter;
import com.example.ecomapplication.adapters.ProductOrderAdapter;
import com.example.ecomapplication.models.OrderModel;
import com.example.ecomapplication.models.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class OrderDetailActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    GridView productView;
    ProductOrderAdapter productAdapter;
    TextView total;

    List<Product> productList;
    String id;
    int totalPr = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        OrderModel orderModel = (OrderModel) bundle.get("object_order");
        id = orderModel.getId();
        firestore = FirebaseFirestore.getInstance();
        productView = findViewById(R.id.productList);
        total = findViewById(R.id.all_total_price_order);

        productList = new ArrayList<>();
        firestore.collection("OrderDetail").document(id)
                .collection("Products").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Product product = document.toObject(Product.class);
                    if(!product.getName().equals("init")){
                        productList.add(product);
                        productAdapter.notifyDataSetChanged();
                        totalPr += product.getQuantity() * product.getPrice();
                        Log.v("Testtt", String.valueOf(totalPr));
                        total.setText(totalPr + " VNĐ");
                    }

                }
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
        Log.v("Testtt", String.valueOf(totalPr));
        productAdapter = new ProductOrderAdapter(this, productList);
        productView.setAdapter(productAdapter);
    }
}
