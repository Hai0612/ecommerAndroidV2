package com.example.ecomapplication.activities;
import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ecomapplication.MainActivity;
import com.example.ecomapplication.R;
import com.example.ecomapplication.adapters.PaymentAdapter;
import com.example.ecomapplication.models.OrderPlace;
import com.example.ecomapplication.models.Payment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Values;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {
    private RecyclerView recyclerListPayment;
    List<Payment> paymentList;
    PaymentAdapter paymentAdapter;
    FirebaseFirestore db;

    Button makePayment,cancelOrder, zaloPay, momo, cod;
    TextView newPaypalText, newGooglePay, newMasterCard ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        paymentList = new ArrayList<>();
        mapping();
        recyclerListPayment = findViewById(R.id.list_payment_rec);
        recyclerListPayment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        paymentAdapter = new PaymentAdapter(this, paymentList);
        recyclerListPayment.setAdapter(paymentAdapter);
        db = FirebaseFirestore.getInstance();

        getPaymentFireBase();
        makePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AlertDialog.Builder builder1 = new AlertDialog.Builder(PaymentActivity.this);
//                builder1.setMessage("Bạn muốn đặt hàng ?");
//                builder1.setCancelable(true);
//
//                builder1.setPositiveButton(
//                        "Yes",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//
//                builder1.setNegativeButton(
//                        "No",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//
//                AlertDialog alert11 = builder1.create();
//                alert11.show();
                OrderPlace cdd=new OrderPlace(PaymentActivity.this, "120 Nguyễn Tuan, Ha Noi", 123000, "1jk1kj31j2k",new Date() , new Date(), 3);
                cdd.show();
            }
        });
        zaloPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("processs" , "fsfdsfs");
                Intent intent = new Intent(PaymentActivity.this, ZaloPayActivity.class);
                startActivity(intent);
            }
        });
        momo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("processs" , "fsfdsfs");

                Intent intent = new Intent(PaymentActivity.this, MomoActivity.class);
                startActivity(intent);
            }
        });

        cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        newPaypalText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPaymentMethod("Paypal");
            }
        });
        newMasterCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPaymentMethod("Master Card");
            }
        });
        newGooglePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPaymentMethod("Google Play");
            }
        });
//        newGoogle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                newPaymentMethod("Google Pay");
//            }
//        });
    }
    public void newPaymentMethod(String payment_type){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //you should edit this to fit your needs
        builder.setTitle("Double Edit Text");

        final EditText one = new EditText(this);
        one.setHint("Account Number");//optional
        final EditText two = new EditText(this);
        two.setHint("Expiry Date");//optional
        final EditText three = new EditText(this);
        three.setHint("Provider");//optional
        //in my example i use TYPE_CLASS_NUMBER for input only numbers
        one.setInputType(InputType.TYPE_CLASS_TEXT);
        two.setInputType(InputType.TYPE_CLASS_DATETIME);
        three.setInputType(InputType.TYPE_CLASS_TEXT);

        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(one);
        lay.addView(two);
        lay.addView(three);

        builder.setView(lay);

        // Set up the buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //get the two inputs
                String number = one.getText().toString();
                Log.v("payment", String.valueOf(two.getText()));
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date d  = Calendar.getInstance().getTime();
                try {
                     d = dateFormat.parse(String.valueOf(two.getText()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String provider = three.getText().toString();
                addDataToFireBase(number, d, provider, payment_type);

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    public void mapping(){
        newPaypalText = findViewById(R.id.paypal);
        makePayment = findViewById(R.id.makePayment);
        cancelOrder = findViewById(R.id.cancel_order);
        zaloPay = findViewById(R.id.zalopay_payment);
        momo = findViewById(R.id.momo_payment);
        cod = findViewById(R.id.cos_payment);
        newGooglePay = findViewById(R.id.googlepay);
        newMasterCard = findViewById(R.id.master_card);
    }
    public void addDataToFireBase (String number, Date date, String provider, String payment_type){
        // Create a new user with a first and last name
        Map<String, Object> payment = new HashMap<>();
        payment.put("account_nb", number);
        payment.put("expired", date);
        payment.put("id", "1815");
        payment.put("id_user", "1");
        payment.put("payment_type", payment_type);
        payment.put("provider", provider);


// Add a new document with a generated ID
        db.collection("Payment")
                .add(payment)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
    public void getPaymentFireBase(){
        db.collection("Payment")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            try{
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    Payment payment = document.toObject(Payment.class);
                                    if(payment.getId_user().trim().equals("1")){
                                        Log.v("user_id_if" , payment.getId_user());

                                        paymentList.add(payment);
                                    }
                                    paymentAdapter.notifyDataSetChanged();
                                }
                            }
                            catch (Exception e ){
                                e.printStackTrace();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}