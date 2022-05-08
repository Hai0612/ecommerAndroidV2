package com.example.ecomapplication.ui.profile;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ecomapplication.R;
import com.example.ecomapplication.activities.EditProfileActivity;
import com.example.ecomapplication.models.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {


    TextView fullName, firstName, lastName, userName, city, password, email, phone, editProfile;
    FirebaseFirestore db;

    UserInfo userInfo;
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        fullName = root.findViewById(R.id.user_full_name);
        firstName =  root.findViewById(R.id.first_name);
        userName =  root.findViewById(R.id.username);
        lastName =  root.findViewById(R.id.last_name);
        city =  root.findViewById(R.id.city);
        email =  root.findViewById(R.id.email);
        phone =  root.findViewById(R.id.phone);

        editProfile =  root.findViewById(R.id.edit_profile);

        db = FirebaseFirestore.getInstance();
        getUserInfoFromFireBase();
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);

                String first_name_up = firstName.getText().toString();
                String last_name_up = lastName.getText().toString();
                String user_name_up = userName.getText().toString();
                String city_up = city.getText().toString();
                String email_up = email.getText().toString();
                String phone_up = phone.getText().toString();

                intent.putExtra("First Name", first_name_up);
                intent.putExtra("Last Name", last_name_up);
                intent.putExtra("User Name", user_name_up);
                intent.putExtra("City", city_up);
                intent.putExtra("Email", email_up);
                intent.putExtra("Phone", phone_up);

                // start the Intent
                startActivity(intent);
            }
        });
        return root;
    }
    public void getUserInfoFromFireBase(){
        db.collection("UserInfo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            try{
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    userInfo = document.toObject(UserInfo.class);
                                    //Log.v("hello", userInfo.getFirstName());
                                    if (userInfo.getId().equals("dfdfsfdsf")) {
                                        String first_name = userInfo.getFirstName();
                                        String last_name = userInfo.getLastName();
                                        String user_name = userInfo.getId();
                                        String email_ = userInfo.getEmail();
                                        String phone_ = userInfo.getPhone();
                                        String city_ = userInfo.getCity();

                                        fullName.setText(first_name + ' ' + last_name);
                                        firstName.setText(first_name);
                                        lastName.setText(last_name);
                                        userName.setText(user_name);
                                        city.setText(city_);
                                        email.setText(email_);
                                        phone.setText(phone_);
                                    }
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