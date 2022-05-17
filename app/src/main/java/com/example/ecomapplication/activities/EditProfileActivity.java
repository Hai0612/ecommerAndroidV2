package com.example.ecomapplication.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecomapplication.MainActivity;
import com.example.ecomapplication.R;
import com.example.ecomapplication.models.UserInfo;
import com.example.ecomapplication.ui.profile.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class EditProfileActivity extends AppCompatActivity {

    TextInputLayout firstName, lastName, userName, city, email, phone;
    TextView saveProfile;

    String _firstName, _lastName, _userName, _city, _email, _phone;

    DatabaseReference reference;
    FirebaseFirestore db;
    UserInfo userInfo;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        firstName = findViewById(R.id.first_name_update);
        city = findViewById(R.id.city_update);
        email = findViewById(R.id.email_update);
        phone = findViewById(R.id.phone_update);

        saveProfile = findViewById(R.id.update_profile);

        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                updateProfile();
                startActivity(intent);
            }
        });

        _firstName = getIntent().getExtras().getString("First Name");
        _city = getIntent().getExtras().getString("City");
        _phone = getIntent().getExtras().getString("Phone");
        _email = getIntent().getExtras().getString("Email");

        // Get data from user activity
        firstName.getEditText().setText(_firstName);
        city.getEditText().setText(_city);
        email.getEditText().setText(_email);
        phone.getEditText().setText(_phone);
    }


    public void updateProfile(){
        if (!isFirstNameChanged() && !isCityChanged() && !isEmailChanged() && !isPhoneChanged()){
            Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
        }
        else {
            db.collection("UserInfo").document(auth.getUid())
                    .update("firstName", firstName.getEditText().getText().toString(),
                            "city", city.getEditText().getText().toString(),
                            "email", email.getEditText().getText().toString(),
                            "phone", phone.getEditText().getText().toString());
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isFirstNameChanged(){
        if (!_firstName.equals(firstName.getEditText().getText().toString())){
            return true;
        }
        return false;
    }

    public boolean isCityChanged(){
        if (!_city.equals(city.getEditText().getText().toString())){
            return true;
        }
        return false;
    }

    public boolean isEmailChanged(){
        if (!_email.equals(email.getEditText().getText().toString())){
            return true;
        }
        return false;
    }

    public boolean isPhoneChanged(){
        if (!_phone.equals(phone.getEditText().getText().toString())){
            return true;
        }
        return false;
    }
}