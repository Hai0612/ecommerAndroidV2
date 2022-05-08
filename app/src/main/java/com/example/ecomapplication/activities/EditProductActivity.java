package com.example.ecomapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecomapplication.MainActivity;
import com.example.ecomapplication.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProductActivity extends AppCompatActivity {

    TextInputLayout product_name, product_desc, product_category, product_price, product_quantity, product_size, product_rating;
    String product_name_, product_desc_, product_category_, product_price_, product_quantity_, product_size_, product_rating_, document_;
    TextView update_product;
    String id_product;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        db = FirebaseFirestore.getInstance();


        product_name = findViewById(R.id.product_name_update);
        product_desc = findViewById(R.id.product_desc_update);
        product_category = findViewById(R.id.product_category_update);
        product_price = findViewById(R.id.price_update);
        product_quantity = findViewById(R.id.quantity_update);
        product_size = findViewById(R.id.size_update);
        product_rating = findViewById(R.id.rating_update);
        update_product = findViewById(R.id.update_product);

        update_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SellerActivity.class);
                updateProduct();
                startActivity(intent);
            }
        });

        product_name_ = getIntent().getExtras().getString("Product Name");
        product_desc_ = getIntent().getExtras().getString("Description");
        product_category_ = getIntent().getExtras().getString("Category");
        product_price_ = getIntent().getExtras().getString("Price");
        product_quantity_ = getIntent().getExtras().getString("Quantity");
        product_size_ = getIntent().getExtras().getString("Size");
        product_rating_ = getIntent().getExtras().getString("Rating");
        document_ = getIntent().getExtras().getString("ID");

//        Log.v("Ajinomoto", document_);

        product_name.getEditText().setText(product_name_);
        product_desc.getEditText().setText(product_desc_);
        product_category.getEditText().setText(product_category_);
        product_price.getEditText().setText(product_price_);
        product_quantity.getEditText().setText(product_quantity_);
        product_size.getEditText().setText(product_size_);
        product_rating.getEditText().setText(product_rating_);
    }

    public void updateProduct(){
        if (!isProductNameChanged() && !isProductDescChanged() && !isProductCateChanged() &&
                !isProductPriceChanged() && !isProductQuantityChanged() && !isProductSizeChanged() && !isProductRateChanged()){
            Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
        }
        else {
            db.collection("Product").document(document_)
                    .update("name", product_name.getEditText().getText().toString(),
                            "description", product_desc.getEditText().getText().toString(),
                            "id_category", product_category.getEditText().getText().toString(),
                            "price",  Integer.valueOf(product_price.getEditText().getText().toString()),
                            "quantity",  Integer.valueOf(product_quantity.getEditText().getText().toString()),
                            "rating", product_rating.getEditText().getText().toString(),
                            "size", product_size.getEditText().getText().toString());
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isProductNameChanged(){
        if (!product_name_.equals(product_name.getEditText().getText().toString())){
            return true;
        }
        return false;
    }

    public boolean isProductDescChanged(){
        if (!product_desc_.equals(product_desc.getEditText().getText().toString())){
            return true;
        }
        return false;
    }

    public boolean isProductCateChanged(){
        if (!product_category_.equals(product_category.getEditText().getText().toString())){
            return true;
        }
        return false;
    }

    public boolean isProductPriceChanged(){
        if (!product_price_.equals(product_price.getEditText().getText().toString())){
            return true;
        }
        return false;
    }

    public boolean isProductQuantityChanged(){
        if (!product_quantity_.equals(product_quantity.getEditText().getText().toString())){
            return true;
        }
        return false;
    }

    public boolean isProductSizeChanged(){
        if (!product_size_.equals(product_size.getEditText().getText().toString())){
            return true;
        }
        return false;
    }

    public boolean isProductRateChanged(){
        if (!product_rating_.equals(product_rating.getEditText().getText().toString())){
            return true;
        }
        return false;
    }
}