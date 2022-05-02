package com.example.ecomapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecomapplication.MainActivity;
import com.example.ecomapplication.R;
import com.example.ecomapplication.models.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity {
    ImageView detailedImg;
    TextView detailedName, detailedDesc, detailedPrice, quantityOrder, ratingValue;
    RatingBar detailedRating;
    Button buyNow, addToCart, addItem, removeItem;

    Product product;
    FirebaseStorage storage;
    FirebaseFirestore firestore;

    int quantity;
    String productId;

    private void binding() {
        detailedImg = findViewById(R.id.detailed_img);
        detailedName = findViewById(R.id.detailed_name);
        detailedRating = findViewById(R.id.my_rating);
        ratingValue = findViewById(R.id.rating_value);
        detailedDesc = findViewById(R.id.detailed_desc);
        detailedPrice = findViewById(R.id.detailed_price);
        quantityOrder = findViewById(R.id.quantity);
        buyNow = findViewById(R.id.buy_now);
        addToCart = findViewById(R.id.add_to_cart);
        addItem = findViewById(R.id.add_item);
        removeItem = findViewById(R.id.remove_item);
        storage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    private void addProductToFirebaseCart(View view, Product newProduct) {
        firestore.collection("Cart").document("SXcZhdR7152RN49UawTz")
                .collection("Products")
                .add(newProduct).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(
                        view.getContext(),
                        "Added product ID " + newProduct.getProductId()
                                + " of " + newProduct.getQuantity() + " products to cart",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(
                        view.getContext(),
                        "Fail to add product to cart",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_detail);
        binding();

        final Object obj = getIntent().getSerializableExtra("productDetail");
        if (obj instanceof Product) {
            product = (Product) obj;
            quantity = 1;
            productId = product.getProductId();
            Log.v("Result", "Get product ID: " + productId);
        }

        if (product != null) {
            detailedName.setText(product.getName());
            detailedDesc.setText(product.getDescription());
            detailedPrice.setText(String.valueOf(product.getPrice()));
            ratingValue.setText(product.getRating());
            detailedRating.setRating(Float.parseFloat(product.getRating()));

            StorageReference storageReference = storage.getReferenceFromUrl(product.getImg_url());

            // Dat anh lay tu Firebase cho item
            storageReference.getDownloadUrl()
                    .addOnSuccessListener(uri -> Picasso.with(DetailActivity.this)
                            .load(uri.toString())
                            .fit().centerInside()
                            .into(detailedImg))
                    .addOnFailureListener(e -> Log.v("Error", "Error when get the images: " + e));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        addItem.setOnClickListener(view -> {
            quantity = Integer.parseInt((String) quantityOrder.getText());
            quantity = quantity + 1;
            quantityOrder.setText(String.valueOf(quantity));
        });

        removeItem.setOnClickListener(view -> {
            quantity = Integer.parseInt((String) quantityOrder.getText());
            if (quantity > 1) {
                quantity = quantity - 1;
                quantityOrder.setText(String.valueOf(quantity));
            }
        });

        addToCart.setOnClickListener(view -> {
            Product productCart = new Product(
                    product.getName(),
                    product.getImg_url(),
                    product.getId_category(),
                    Integer.parseInt(product.getPrice()),
                    product.getSize(),
                    quantity,
                    product.getDescription()
            );

            productCart.setProductId(productId);

            addProductToFirebaseCart(view, productCart);
        });

        buyNow.setOnClickListener(view -> {
            Product productCart = new Product(
                    product.getName(),
                    product.getImg_url(),
                    product.getId_category(),
                    Integer.parseInt(product.getPrice()),
                    product.getSize(),
                    quantity,
                    product.getDescription()
            );

            productCart.setProductId(productId);

            addProductToFirebaseCart(view, productCart);

            Intent intent = new Intent(view.getContext(), CheckoutActitvity.class);
            startActivity(intent);
        });
    }
}