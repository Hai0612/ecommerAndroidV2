package com.example.ecomapplication.activities;
import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ecomapplication.R;

import com.example.ecomapplication.adapters.AddressAdapter;
import com.example.ecomapplication.adapters.CheckoutAdapter;
import com.example.ecomapplication.adapters.MyCartAdapter;
import com.example.ecomapplication.models.MyCartModel;
import com.example.ecomapplication.models.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActitvity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    TextView totalAmount;
    List<String> listAddress;
    AddressAdapter addressAdapter;
    RecyclerView addressListView;
    private Button new_address_button;
    private Button click_to_payment;
        List<MyCartModel> cartModelList;
    CheckoutAdapter cartAdapter;
    RecyclerView productsCheckoutRecyclerView;
    FirebaseFirestore db;
    private FirebaseAuth auth;
    Spinner listAddressSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_actitvity);
        db = FirebaseFirestore.getInstance();
        new_address_button = findViewById(R.id.new_address_button);
        click_to_payment = findViewById(R.id.click_to_payment);
        productsCheckoutRecyclerView = findViewById(R.id.orderList);
        listAddressSpinner = findViewById(R.id.list_drop_address);
        totalAmount = findViewById(R.id.totalLabel2);

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mMessageReceiver, new IntentFilter("MyTotalAmount"));

        //getListAddress
        listAddress = new ArrayList<>();
        getListAddress();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listAddress);
//set the spinners adapter to the previously created one.
        listAddressSpinner.setAdapter(adapter);
        listAddressSpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        click_to_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckoutActitvity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });
        auth = FirebaseAuth.getInstance();

        new_address_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });


        //get product
        getProductCheckout();
        productsCheckoutRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        cartAdapter = new CheckoutAdapter(this, cartModelList);
        productsCheckoutRecyclerView.setAdapter(cartAdapter);

    }
    public void getProductCheckout () {
        cartModelList = new ArrayList<>();
        db.collection("Cart").document(auth.getUid())
//        firestore.collection("Cart").document(auth.getCurrentUser().getUid())
                .collection("Products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc :task.getResult().getDocuments()) {
//                        Log.v("Test", auth.getCurrentUser().getUid());
                        MyCartModel myCartModel = doc.toObject(MyCartModel.class);
                        myCartModel.setDocumentId(doc.getId());
                        cartModelList.add(myCartModel);
                        Log.v("Tag", myCartModel.getDescription());
                        cartAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                Log.v("TAGGG" , "(String) parent.getItemAtPosition(position)");
                break;
            case 1:
                Log.v("TAGGG" , (String) parent.getItemAtPosition(position));
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void showDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Thêm địa chỉ nhận hàng");
        alert.setMessage("Nhập địa chỉ nhận hàng mới");

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                Log.v("address_fire", value);
                db.collection("UserInfo").document("CuGpKPBNtXlDfVpUEqY9").update(
                        "address", FieldValue.arrayUnion(value));
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }
    public void getListAddress(){
        db.collection("UserInfo")
                .document("CuGpKPBNtXlDfVpUEqY9")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Log.v("address_fire", "fdsfds");

                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.v("id_document", document.getId());
                                UserInfo user = document.toObject(UserInfo.class);
                                for(int i = 0; i < user.getAddress().size() ; i++){
                                    listAddress.add(user.getAddress().get(i));
                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }

                    }
                });
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int totalBill = intent.getIntExtra("totalAmount", 0);
            totalAmount.setText(totalBill + "vnđ");
        }
    };
}