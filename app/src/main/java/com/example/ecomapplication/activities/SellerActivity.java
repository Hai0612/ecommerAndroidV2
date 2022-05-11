package com.example.ecomapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecomapplication.R;
import com.example.ecomapplication.adapters.AddressAdapter;
import com.example.ecomapplication.adapters.PopularProductAdapter;
import com.example.ecomapplication.adapters.ProductSellerAdapter;
import com.example.ecomapplication.models.MyCartModel;
import com.example.ecomapplication.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class SellerActivity extends AppCompatActivity {

    ImageButton logOut, editProfile, addProduct;
    TextView userName, shopName, email, productTab, orderTab;
    RelativeLayout productRl, orderRl;
    FirebaseFirestore db;
    List<Product> myProduct;
    private RecyclerView  myProductRecyclerview ;
    private  ProductSellerAdapter myProductAdapter;

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
        myProductRecyclerview = findViewById(R.id.my_product_view);
        orderRl = findViewById(R.id.orderRl);
        db = FirebaseFirestore.getInstance();

        showProductUi();
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SellerActivity.this , NewProductActivity.class));
            }
        });
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

        getMyProduct();
        myProductRecyclerview.setLayoutManager(new GridLayoutManager(this,2));
        myProductAdapter = new ProductSellerAdapter(this, myProduct);
        myProductRecyclerview.setAdapter(myProductAdapter);

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
    public  void getMyProduct(){
        myProduct = new ArrayList<>();
        db.collection("Product").whereEqualTo("id_seller", "SXcZhdR7152RN49UawTz").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc :task.getResult().getDocuments()) {
//                        Log.v("Test", auth.getCurrentUser().getUid());
                        Product product = doc.toObject(Product.class);
                        product.setDocumentId(doc.getId());
                        myProduct.add(product);
                        myProductAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

}
