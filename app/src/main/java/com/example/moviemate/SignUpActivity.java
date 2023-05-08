package com.example.moviemate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.moviemate.databinding.ActivityMainBinding;
import com.example.moviemate.databinding.LaunchScreenBinding;
import com.example.moviemate.databinding.SignupBinding;
import com.example.moviemate.viewmodel.SharedViewModel;
import com.example.moviemate.viewmodel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private FirebaseAuth auth;
    private SignupBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.editDob.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    System.out.println("Clicked on edit date of birth");
                    pickDateOfBirth();
                    // Do what you want
                    return true;
                }
                return false;
            }
        });
        Button loginBackButton = findViewById(R.id.loginBackButton);
        firebaseAuthentication();

        loginBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void pickDateOfBirth()
    {
        //DialogFragment
        DatePicker mDatePickerDialogFragment = new DatePicker();
        mDatePickerDialogFragment.show(this.getSupportFragmentManager(), DatePicker.TAG);
    }

    public void firebaseAuthentication() {
        auth = FirebaseAuth.getInstance();
        Button registerButton = findViewById(R.id.addButton);

        EditText emailEditText = findViewById(R.id.email_create);
        EditText passwordEditText = findViewById(R.id.password_create);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email_txt = emailEditText.getText().toString();
                String password_txt = passwordEditText.getText().toString();
                if(TextUtils.isEmpty(email_txt)) {
                    emailEditText.setError( "Email is required!" );
                    toastMsg("Please enter Email");
                    return;
                }
                /*if (email_txt.contains("@")) {
                    toastMsg("Invalid Email");
                    return;
                }*/
                if(TextUtils.isEmpty(password_txt)) {
                    passwordEditText.setError( "Password is required!" );
                    toastMsg("Please enter Password");
                    return;
                }
                if(password_txt.length() <= 6) {
                    passwordEditText.setError( "Invalid Password!" );
                    toastMsg("Password too short.");
                    return;
                }
                if(!password_txt.matches("(?=.*[a-z])(?=.*[A-Z]).+")) {
                    passwordEditText.setError( "Invalid Password!" );
                    toastMsg("Password too short.");
                    return;
                }
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
                    /*Intent intent = new Intent(SignUpActivity.this, LaunchActivity.class);
                    intent.putExtra("userEmail", email_txt);

                    startActivity(intent);*/
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
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

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.getTime());
        //tvDate.setText(selectedDate);
        System.out.println("selected date " + selectedDate);
        //model.setDateOfBirth(selectedDate);
        binding.editDob.setText(selectedDate);
    }
}
