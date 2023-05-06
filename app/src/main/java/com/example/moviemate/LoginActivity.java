package com.example.moviemate;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.lifecycle.ViewModelProvider;

import com.example.moviemate.viewmodel.SharedViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        firebaseAuthentication();
      /*  startActivity(new Intent(LoginActivity.this,
                LaunchActivity.class));*/

    }
    public void firebaseAuthentication()
    {
        auth = FirebaseAuth.getInstance();
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button registerButton =findViewById(R.id.signupButton);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,
                        SignUpActivity.class));
            }
        });

        Button loginButton =findViewById(R.id.signinButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*startActivity(new Intent(LoginActivity.this,
                        LaunchActivity.class));*/
                String txt_Email = emailEditText.getText().toString().trim();
                String txt_Pwd = passwordEditText.getText().toString().trim();

                if(TextUtils.isEmpty(txt_Email)) {
                    emailEditText.setError( "Email is required!" );
                    toastMsg("Please enter Email");
                    return;
                }
               /* if (txt_Email.contains("@")) {
                    toastMsg("Invalid Email");
                    return;
                }*/
                if(TextUtils.isEmpty(txt_Pwd)) {
                    passwordEditText.setError( "Password is required!" );
                    toastMsg("Please enter Password");
                    return;
                }
                if(txt_Pwd.length() <= 6) {
                    passwordEditText.setError( "Invalid Password!" );
                    toastMsg("Password too short.");
                    return;
                }
                if(!txt_Pwd.matches("(?=.*[a-z])(?=.*[A-Z]).+")) {
                    passwordEditText.setError( "Invalid Password!" );
                    toastMsg("Password too short.");
                    return;
                }
                loginUser(txt_Email, txt_Pwd);
            }
        });
    }


    private void loginUser(String txt_email, String txt_pwd) {


        // call the object and provide it with email id and password
       auth.signInWithEmailAndPassword(txt_email,
                txt_pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String msg = "Login Successful";
                toastMsg(msg);
               // startActivity(new Intent(LoginActivity.this, LaunchActivity.class));
                Intent intent = new Intent(LoginActivity.this, LaunchActivity.class);
                intent.putExtra("userEmail", txt_email);
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                toastMsg("Login Failed");
                return;
            }
        });
    }

    public void toastMsg(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}

