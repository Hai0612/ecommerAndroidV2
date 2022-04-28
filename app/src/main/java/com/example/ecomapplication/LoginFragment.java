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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecomapplication.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;

public class LoginFragment extends Fragment {
    ActivityLoginBinding loginBinding;
    private FirebaseAuth auth;
    private Button signInButton;
    private TextView linkToSignUp;
    private EditText emailSignIn,passwordSignIn;
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();

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
        View root =  inflater.inflate(R.layout.fragment_login, container, false);
        auth = FirebaseAuth.getInstance();
        signInButton = root.findViewById(R.id.signInButton);
        linkToSignUp = root.findViewById(R.id.linkToSignUp);
        emailSignIn = root.findViewById(R.id.emailSignIn);
        passwordSignIn = root.findViewById(R.id.passwordSignIn);

        signInButton.setOnClickListener(view -> {


            if (TextUtils.isEmpty(emailSignIn.getText())) {
                Toast.makeText(getContext(), "Please enter your email!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(passwordSignIn.getText())) {
                Toast.makeText(getContext(), "Please enter your password!", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(emailSignIn.getText().toString(), passwordSignIn.getText().toString())
                    .addOnCompleteListener(getActivity(), task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Sign In Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), MainActivity.class));
                        } else {
                            Toast.makeText(getContext(), "Email or Password Wrong " + task.getException() + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
        return root;
    }
}