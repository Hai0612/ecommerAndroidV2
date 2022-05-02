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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ecomapplication.R;

import com.example.ecomapplication.adapters.AddressAdapter;
import com.example.ecomapplication.models.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActitvity extends AppCompatActivity {
    List<String> listAddress;
    AddressAdapter addressAdapter;
    RecyclerView addressListView;
    private Button new_address_button;
    private Button click_to_payment;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_actitvity);
        db = FirebaseFirestore.getInstance();
        new_address_button = findViewById(R.id.new_address_button);
        addressListView = findViewById(R.id.list_address);
        click_to_payment = findViewById(R.id.click_to_payment);
        click_to_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckoutActitvity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });
        new_address_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        listAddress = new ArrayList<>();
        getListAddress();
        addressAdapter = new AddressAdapter(this, listAddress);
        addressListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        addressListView.setAdapter(addressAdapter);

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
                                addressAdapter.notifyDataSetChanged();
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }

                    }
                });
    }
}