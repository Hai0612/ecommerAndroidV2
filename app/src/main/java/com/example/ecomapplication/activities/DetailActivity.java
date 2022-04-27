package com.example.ecomapplication.activities;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.ecomapplication.R;
import com.example.ecomapplication.models.Product;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity {
    ImageView addItem, removeItem, detailedImg;
    TextView detailedName, detailedDesc, detailedPrice, quantityOrder, ratingValue;
    RatingBar detailedRating;
    Button buyNow, addToCart;

    Product product;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_detail);
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


        final Object obj = getIntent().getSerializableExtra("detailed");
        if (obj instanceof Product) {
            product = (Product) obj;
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
}