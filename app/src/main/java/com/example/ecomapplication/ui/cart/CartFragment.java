package com.example.ecomapplication.ui.cart;

import static android.content.ContentValues.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ecomapplication.R;
import com.example.ecomapplication.activities.CheckoutActitvity;
import com.example.ecomapplication.activities.PaymentActivity;
import com.example.ecomapplication.adapters.MyCartAdapter;
import com.example.ecomapplication.models.CartModel;
import com.example.ecomapplication.models.MyCartModel;
import com.example.ecomapplication.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    Button buttonMinus, buttonPlus, button_buy_now;
    int overAllTotalAmount;
    TextView overAllAmount;
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<MyCartModel> cartModelList;
    MyCartAdapter cartAdapter;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_cart, container, false);

        firestore = FirebaseFirestore.getInstance();

        toolbar = root.findViewById(R.id.my_cart_toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        get data from my cart adapter
        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(mMessageReceiver, new IntentFilter("MyTotalAmount"));
        auth = FirebaseAuth.getInstance();
        buttonMinus = root.findViewById(R.id.button_minus);
        buttonPlus = root.findViewById(R.id.button_plus);
        overAllAmount = root.findViewById(R.id.total_amount);
        recyclerView = root.findViewById(R.id.cart_rec);
        button_buy_now = root.findViewById(R.id.button_buy_now);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(getContext(), cartModelList);
        recyclerView.setAdapter(cartAdapter);
        button_buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CheckoutActitvity.class);
                intent.putExtra("totalOrder",overAllTotalAmount);
                startActivity(intent);
            }
        });

        firestore.collection("Cart").document(auth.getUid())
//        firestore.collection("Cart").document(auth.getCurrentUser().getUid())
                .collection("Products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc :task.getResult().getDocuments()) {
//                        Log.v("Test", auth.getCurrentUser().getUid());
                        MyCartModel myCartModel = doc.toObject(MyCartModel.class);
                        myCartModel.setDocumentId(doc.getId());
                        if(!myCartModel.getName().equals("init")){
                            cartModelList.add(myCartModel);
                            cartAdapter.notifyDataSetChanged();
                        }

                    }
                }
            }
        });
        return root;
    }
    public void getProductData(){
        final List<CartModel> productModelList = new ArrayList<>();
//        db.collection("Cart").get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                           @Override
//                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                               if (task.isSuccessful()) {
//                                                   for (final DocumentSnapshot document : task.getResult()) {
//
//
//                                                       DocumentReference docRef = db.collection("Product").document("2");
//                                                       docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                                           @Override
//                                                           public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                                               final CartModel cartModel = document.toObject(CartModel.class);
//                                                               final String id_product = cartModel.getId_product();
//                                                               Log.v("TAG", id_product);
//                                                               cartModel.setProduct(documentSnapshot.toObject(Product.class));
//                                                               cartModelList.add(cartModel);
//                                                           }
//                                                       });
////                                productModelList.add(feedModel);
////                                fireCallback.onProduct(productModelList);
//                                                   }
//                                               }
//                                           }
//                                       }
//                );
//        Log.v(TAG, String.valueOf(cartModelList.size()));
    }
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            overAllTotalAmount = intent.getIntExtra("totalAmount", 0);
            overAllAmount.setText(overAllTotalAmount + "VND");
        }
    };

    public void refreshActivtiy() {
//        recreate();
    }
}