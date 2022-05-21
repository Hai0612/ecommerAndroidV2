package com.example.ecomapplication.models;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.ecomapplication.MainActivity;
import com.example.ecomapplication.R;
import com.example.ecomapplication.activities.RegistrationActivity;
import com.example.ecomapplication.adapters.CheckoutAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderPlace extends Dialog implements
        android.view.View.OnClickListener {

    public Activity activity;
    public Dialog d;
    public Button yes, no;
    private TextView addressText;
    private TextView totalText;
    FirebaseFirestore db;
    private String orderAddress;
    private  String id_user;
    private Date orderDate;
    CheckoutAdapter cartAdapter;
    List<Product> cartModelList;
    private Date shippedDate;
     private int number;
     private String id;
    ProgressDialog progressDialog;

    private FirebaseAuth auth;
    private  Payment selectedPayment;
    private int total;
    public OrderPlace(Activity activity , String orderAddress, int total ,String id_user  , Date orderDate , Date shippedDate , Payment selectedPayment) {
        super(activity);
        this.activity = activity;
        this.orderAddress = orderAddress;
        this.total = total;
        this.id_user = id_user;
        this.orderDate = orderDate;
        this.shippedDate = shippedDate;
        this.selectedPayment = selectedPayment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.orderplace_dialog);
        db = FirebaseFirestore.getInstance();
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        totalText = findViewById(R.id.total_order);
        addressText = findViewById(R.id.address_order);
        totalText.setText(String.valueOf(total));
        addressText.setText(orderAddress);
        auth = FirebaseAuth.getInstance();
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        getProductToPayment();

    }
    public void addPaymentOfOrder(String id_order){

        Map<String, Object> payment = new HashMap<>();
        payment.put("account_nb", selectedPayment.getAccount_nb());
        payment.put("expired", new Date());
        payment.put("id_order", id_order);
        payment.put("id_user", auth.getUid());
        payment.put("payment_type", selectedPayment.getPayment_type());
        payment.put("provider", selectedPayment.getProvider());
        db.collection("Payment")
                .add(payment)
                .addOnSuccessListener(documentReference -> {
                    Log.v(TAG, "ADD order detail thanh cong");
                    deleteProductsInCartOfUser();
                })
                .addOnFailureListener(e -> Log.w(TAG, "Them order detail that bai", e));
    }
    public void orderPlace(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date d  = Calendar.getInstance().getTime();
        Map<String, Object> order = new HashMap<>();
        id = getAlphaNumericString(5);
        order.put("orderAddress", orderAddress);
        order.put("orderDate", orderDate);
        order.put("shippedDate", shippedDate);
        order.put("status", "pending");
        order.put("total", total);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Đang thanh toán");
        progressDialog.setMessage("Vui lòng chờ...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                //Do something here
                progressDialog.dismiss();
            }
        }, 1500);
// Add a new document with a generated ID
        db.collection("Order")
                .document(auth.getUid())
                .collection("Orders")
                .add(order).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String id_order = task.getResult().getId();
                AddProductListToOrderDetail(id_order);
                addPaymentOfOrder(id_order);
            } else {
                Log.w(TAG, "Them order that bai");
            }
        });

    }
    public void getProductToPayment(){
        cartModelList = new ArrayList<>();
        db.collection("Cart").document(auth.getUid())
                .collection("Products").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot doc :task.getResult().getDocuments()) {
                            Log.v("dfsfsf", doc.getId());
                            Product myCartModel = doc.toObject(Product.class);
                            myCartModel.setDocumentId(doc.getId());
                            cartModelList.add(myCartModel);
                        }
                    }
                });
    }
    public void AddProductListToOrderDetail(String id_order){
         boolean[] check = {false};
        for(int i  = 0 ; i < cartModelList.size(); i ++){
            db.collection("OrderDetail").document(id_order)
                    .collection("Products").document(cartModelList.get(i).getDocumentId())
                    .set(cartModelList.get(i))
                    .addOnSuccessListener(documentReference -> {
                        check[0] = true;
                        Log.v("Result", "ADD order detail thanh cong");
                    })
                    .addOnFailureListener(e -> Log.v("Result", "Them order detail that bai", e));
        }
            FireOrderToSeller(id_order);



// Add a new document with a generated ID

    }
    public void FireOrderToSeller(String id_order){
        for(int i  = 0 ; i < cartModelList.size(); i ++){
            Log.v("dfsfd", String.valueOf(cartModelList.size()));
            Log.v("dfsfd", String.valueOf(cartModelList.get(i).getId_seller()));
            SellerOrder sellerOrder = new SellerOrder(id_order,cartModelList.get(i).getDocumentId(),auth.getUid(),new Date(), new Date(), cartModelList.get(i).getQuantity(), "pending", cartModelList.get(i).getId_seller());
            db.collection("SellerOrder").document(cartModelList.get(i).getId_seller())
                    .collection("Orders")
                    .add(sellerOrder)
                    .addOnSuccessListener(documentReference -> {
                        Log.v("Result", "ADD order seller thanh cong");
                    })
                    .addOnFailureListener(e -> Log.v("Result", "Them order detail that bai", e));
        }
        deleteProductsInCartOfUser();




// Add a new document with a generated ID

    }
    public void deleteProductsInCartOfUser() {
        Log.v("ID_user" , auth.getUid());
        db.collection("Cart").document(auth.getUid()).collection("Products")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("Cart").document(auth.getUid()).
                                        collection("Products").document(document.getId()).delete();
                            }
                            getContext().startActivity(new Intent(getContext(), MainActivity.class));
                        } else {
                        }
                    }
                });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:{
                orderPlace();
            }
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
    public String getAlphaNumericString(int n)
    {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}