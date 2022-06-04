package com.example.ecomapplication.activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ecomapplication.MainActivity;
import com.example.ecomapplication.R;
import com.example.ecomapplication.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class NewProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button productImg, addProduct;
    EditText productName, productDesc, productPrice, productQuantity, productCategory, productSize, productRating;
    FirebaseFirestore db;
    Spinner category_;
    String text;
    Product product;
    private FirebaseAuth auth;

    String id = UUID.randomUUID().toString();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_product);
        // Inflate the layout for this fragment
        productImg = findViewById(R.id.product_Img);
        auth = FirebaseAuth.getInstance();

        productName = findViewById(R.id.product_name);
        productDesc = findViewById(R.id.product_desc);
        productPrice = findViewById(R.id.product_price);
        productQuantity = findViewById(R.id.product_quantity);
        //productCategory = findViewById(R.id.product_category);
        category_ = findViewById(R.id.product_category);
        productSize = findViewById(R.id.product_size);
        productRating = findViewById(R.id.product_rating);

        addProduct = findViewById(R.id.add_product);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        category_.setOnItemSelectedListener(this);
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _productName = productName.getText().toString().trim();
                String _productDesc = productDesc.getText().toString().trim();
//                String _productCategory = productCategory.getText().toString().trim();
                String _productCategory = text.trim();
                String _productPrice = productPrice.getText().toString().trim();
                String _productQuantity = productQuantity.getText().toString().trim();
                String _productSize = productSize.getText().toString().trim();
                String _productRating = productRating.getText().toString().trim();
                String _id = id.trim();
                String _imgUrl = "https://firebasestorage.googleapis.com/v0/b/ecommerce-de4aa.appspot.com/o/277813743_1349786442170861_4234593667266222737_n.jpg?alt=media&token=c92e92bc-0802-499a-b08e-73265d2433c1".trim();

                Log.v("Aloha", _productName);

                AddProductToFireBase(_productDesc, _id, _productCategory, _imgUrl, _productName,
                        Integer.valueOf(_productPrice), Integer.valueOf(_productQuantity), _productRating, _productSize);
                Toast.makeText(view.getContext(), "Add Successed!", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(NewProductActivity.this , SellerActivity.class));
            }
        });
        productImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImg();
            }
        });
     }
     public void selectImg(){
        
     }

    public void AddProductToFireBase(String description, String id, String id_category, String img_url, String name,
                                     int price, int quantity, String rating, String size){

//        String docId = UUID.randomUUID().toString();
        String docId = id;

        Map<String, Object> doc = new HashMap<>();
        doc.put("description", description);
        doc.put("id_category", id_category);
        doc.put("img_url", img_url);
        doc.put("name", name);
        doc.put("price", price);
        doc.put("quantity", quantity);
        doc.put("rating", rating);
        doc.put("size", size);
        doc.put("id_seller", auth.getUid());
        db.collection("Product").document(docId).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        text = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}