package com.example.ecomapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import java.util.concurrent.Executor;

public class RegistrationFragment extends Fragment {

    private FirebaseAuth auth;
    private Button signUpButton;
    private EditText nameSignUp,emailSignUp,passwordSignUp;
    // TODO: Rename and change types and number of parameters
    public static RegistrationFragment newInstance(String param1, String param2) {
        RegistrationFragment fragment = new RegistrationFragment();

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
        View root =  inflater.inflate(R.layout.fragment_registration, container, false);
        auth = FirebaseAuth.getInstance();
        signUpButton = root.findViewById(R.id.signUpButton);
        nameSignUp = root.findViewById(R.id.nameSignUp);
        emailSignUp = root.findViewById(R.id.emailSignUp);
        passwordSignUp = root.findViewById(R.id.passwordSignUp);

        signUpButton.setOnClickListener(view -> {


            if (TextUtils.isEmpty(nameSignUp.getText())) {
                Toast.makeText(getContext(), "Please enter your name!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(emailSignUp.getText())) {
                Toast.makeText(getContext(), "Please enter your email!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(passwordSignUp.getText())) {
                Toast.makeText(getContext(), "Please enter your password!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (passwordSignUp.length() < 8) {
                Toast.makeText(getContext(), "Password length must be greater than 8", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(emailSignUp.getText().toString(), passwordSignUp.getText().toString())
                    .addOnCompleteListener( getActivity(), task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Sign Up Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), MainActivity.class));
                        } else {
                            Toast.makeText(getContext(), "Sign Up Failed " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
        return root;
    }
}