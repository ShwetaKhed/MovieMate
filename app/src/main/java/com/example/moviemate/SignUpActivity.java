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
import com.example.moviemate.model.User;
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
    private DatabaseReference mDatabaseRef;
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
        EditText first_name = findViewById(R.id.first_name);
        EditText last_name = findViewById(R.id.last_name);
        EditText edit_dob = findViewById(R.id.edit_dob);
        EditText add = findViewById(R.id.add);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_txt = emailEditText.getText().toString();
                String password_txt = passwordEditText.getText().toString();
                String firstNameTxt = first_name.getText().toString();
                String lastNameTxt = last_name.getText().toString();
                String dob = edit_dob.getText().toString();
                String addTxt = add.getText().toString();

                if(TextUtils.isEmpty(firstNameTxt)) {
                    first_name.setError( "First Name is required!" );
                    toastMsg("Please enter First Name");
                    return;
                }

                if(TextUtils.isEmpty(lastNameTxt)) {
                    last_name.setError( "Last Name is required!" );
                    toastMsg("Please enter Last Name");
                    return;
                }

                if(TextUtils.isEmpty(lastNameTxt)) {
                    last_name.setError( "Last Name is required!" );
                    toastMsg("Please enter Last Name");
                    return;
                }

                if(TextUtils.isEmpty(dob)) {
                    edit_dob.setError( "Date of birth is required!" );
                    toastMsg("Please enter Date of birth");
                    return;
                }
                if(TextUtils.isEmpty(addTxt)) {
                    add.setError( "Address is required!" );
                    toastMsg("Please enter Address");
                    return;
                }

                if(TextUtils.isEmpty(email_txt)) {
                    emailEditText.setError( "Email is required!" );
                    toastMsg("Please enter Email");
                    return;
                }

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
                    //Save on firebase
                    saveUserDataOnRealtimeFirebase();
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

    private void saveUserDataOnRealtimeFirebase()
    {
        String firstName=binding.firstName.getText().toString();
        String lastName=binding.lastName.getText().toString();
        String newDob=binding.editDob.getText().toString();
        String email = binding.emailCreate.getText().toString();
        String address = binding.add.getText().toString();

        String username = email.split("@")[0];

        User user = new User(email, firstName, lastName, newDob, "", "", address );

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        mDatabaseRef.child(username).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
               System.out.println("Successfully updated user data on firebase");
            }
        });
    }
    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.getTime());
        System.out.println("selected date " + selectedDate);
        binding.editDob.setText(selectedDate);
    }
}
