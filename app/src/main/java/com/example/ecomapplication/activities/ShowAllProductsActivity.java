package com.example.ecomapplication.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.example.ecomapplication.R;
import com.example.ecomapplication.adapters.CategoryAdapter;
import com.example.ecomapplication.adapters.NewProductAdapter;
import com.example.ecomapplication.adapters.PopularProductAdapter;
import com.example.ecomapplication.adapters.ProductAdapter;
import com.example.ecomapplication.models.Category;
import com.example.ecomapplication.models.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class ShowAllProductsActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    GridView productView;
    ProductAdapter productAdapter;

    List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        firestore = FirebaseFirestore.getInstance();
        productView = findViewById(R.id.productList);

        getProductFromFireBase();
        productAdapter = new ProductAdapter(this, productList);
        productView.setAdapter(productAdapter);
    }

    private void getProductFromFireBase() {
        productList = new ArrayList<>();

        firestore.collection("Product").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Product product = document.toObject(Product.class);
                    productList.add(product);
                    productAdapter.notifyDataSetChanged();
                }
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
    }
}