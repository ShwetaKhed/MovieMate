package com.example.moviemate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.moviemate.viewmodel.SharedViewModel;
import com.example.moviemate.viewmodel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        firebaseAuthentication();
    }

    public void firebaseAuthentication() {
        auth = FirebaseAuth.getInstance();
        Button registerButton = findViewById(R.id.addButton);
       // EditText emailEditText = findViewById(R.id.emailEditText);
       // EditText passwordEditText = findViewById(R.id.passwordEditText);
        EditText emailEditText = findViewById(R.id.email_create);
        EditText passwordEditText = findViewById(R.id.password_create);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(SignUpActivity.this,
//                        LaunchActivity.class));
                // change the text in signup.xml
                String email_txt = emailEditText.getText().toString();
                String password_txt = passwordEditText.getText().toString();
                if (TextUtils.isEmpty(email_txt) || TextUtils.isEmpty(password_txt)) {
                    String msg = "Empty Username or Password";
                } else if (password_txt.length() < 6) {
                    String msg = "Password is too short";
                } else
                    registerUser(email_txt, password_txt);
            }
        });
    }

    private void registerUser(String email_txt, String password_txt) {
        // To create username and password

        auth.createUserWithEmailAndPassword(email_txt, password_txt).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String msg = "Registration Successful";
//                    startActivity(new Intent(SignUpActivity.this,
//                            LaunchActivity.class));
                    Intent intent = new Intent(SignUpActivity.this, LaunchActivity.class);
                    intent.putExtra("userEmail", email_txt);

                    startActivity(intent);
                } else {
                    System.out.println(task.getException());
                    String msg = "Registration Unsuccessful";
                    toastMsg(msg);
                }
            }
        });
    }

    public void toastMsg(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}


