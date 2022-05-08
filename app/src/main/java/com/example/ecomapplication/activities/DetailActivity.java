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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ecomapplication.R;
import com.example.ecomapplication.adapters.CommentAdapter;
import com.example.ecomapplication.adapters.OrderAdapter;
import com.example.ecomapplication.models.Comment;
import com.example.ecomapplication.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

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
    Button buyNow, addToCart, addItem, removeItem,addComment;
    Product product;
    FirebaseStorage storage;
    FirebaseFirestore firestore;
    CircularImageView userCommentImg;
    EditText postDetailComment;
    RecyclerView RvComment;
    String PostKey;
    private FirebaseAuth auth;

    FirebaseFirestore db;
    FirebaseDatabase firebaseDatabase;
    List<Comment> list;
    CommentAdapter commentAdapter;

    int quantity;
    String productId;

    private void binding() {
        storage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_detail);
        RvComment = findViewById(R.id.rv_comment);
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
        userCommentImg = findViewById(R.id.user_comment_img);
        addComment = findViewById(R.id.add_comment_btn);
        postDetailComment = findViewById(R.id.post_detail_comment);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

    }

    private void addProductToFirebaseCart(View view, Product newProduct) {
        firestore.collection("Cart").document(auth.getUid())
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
        iniRvComment();


        RvComment.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(getApplicationContext(), list);
        RvComment.setAdapter(commentAdapter);

//        RvComment.setLayoutManager(new LinearLayoutManager(this));
//        commentAdapter = new CommentAdapter(getApplicationContext(), list);
//        RvComment.setAdapter(commentAdapter);



        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniRvComment();
                commentAdapter = new CommentAdapter(getApplicationContext(), list);
                RvComment.setAdapter(commentAdapter);
                addComment.setVisibility(View.INVISIBLE);
                String _imgUrl = "https://firebasestorage.googleapis.com/v0/b/ecommerce-de4aa.appspot.com/o/274736835_677983293549819_1786662699780048436_n.jpg?alt=media&token=23b9dcff-f1ce-436b-af77-101af401075f".trim();
                String _id = "1".trim();
                String _comment = postDetailComment.getText().toString().trim();
                String _user = "dfdfsfdsf".trim();
                Object date = ServerValue.TIMESTAMP;
                AddCommentToFireBase(_comment, date, _id, _user, _imgUrl);
            }
        });



    }
    public void AddCommentToFireBase(String content, Object date, String id_product, String id_user, String user_img){
        String docId = UUID.randomUUID().toString();

        Map<String, Object> doc = new HashMap<>();
        doc.put("content", content);
        doc.put("date", ServerValue.TIMESTAMP);
        doc.put("id_product", id_product);
        doc.put("id_user", id_user);
        doc.put("user_img", user_img);

        db.collection("Comment").document(docId).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        postDetailComment.setText("");
                        showMessage("Comment Successed");
                        addComment.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("Comment Failed");
                    }
                });
    }

    public void iniRvComment(){
        list = new ArrayList<>();


        db.collection("Comment").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.v("comment" , "document.getId()");

                try {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Comment comment = document.toObject(Comment.class);
                        Log.v("name", comment.getContent());
                        list.add(comment);
                        commentAdapter.notifyDataSetChanged();
                    }

                }
                catch (Exception e ) {
                    e.printStackTrace();
                }
            } else {
                Log.w("TAG", "Error getting documents.", task.getException());
            }
        });
    }

    public void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
            Product productCart = product;
            productCart.setProductId(productId);

            addProductToFirebaseCart(view, productCart);
        });

        buyNow.setOnClickListener(view -> {
            Product productCart = new Product(
                    product.getName(),
                    product.getImg_url(),
                    product.getId_category(),
                    product.getPrice(),
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