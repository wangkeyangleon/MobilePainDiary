package com.example.mobilepaindiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.mobilepaindiary.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    //    declare the instance of firebase
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        initialize the firebaseAuth instance
        auth = FirebaseAuth.getInstance();

//        show the password
        binding.showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.userPassWord.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    binding.confirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    binding.userPassWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

//        submit the registration information to the firebase
        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the user email
                String userEmail = binding.userEmail.getText().toString();
                String userPassword = binding.userPassWord.getText().toString();
                String confirmPassword = binding.confirmPassword.getText().toString();

//                check whether use input the value
                if (userEmail.isEmpty() || userPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this,
                            "Please input all the required value", Toast.LENGTH_SHORT).show();

                } else {

                    if (!validEmail(userEmail)) {
                        Toast.makeText(RegisterActivity.this,
                                "Please check the email format", Toast.LENGTH_SHORT).show();
                    } else if (!validPassword(userPassword)) {
                        Toast.makeText(RegisterActivity.this,
                                "The password length should larger than 6", Toast.LENGTH_SHORT).show();
                    } else if (!userPassword.equals(confirmPassword)) {
                        Toast.makeText(RegisterActivity.this,
                                "Two passwords are not same", Toast.LENGTH_SHORT).show();
                    } else {
                        auth.createUserWithEmailAndPassword(userEmail, confirmPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
//                                go to the sign in activity if successfully register
                                    Intent intent = new Intent(RegisterActivity.this, SignInActivity.class);
                                    Toast.makeText(RegisterActivity.this,
                                            "Registration completed successfully !", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                } else {
//                                this message will never show on the screen
                                    Toast.makeText(RegisterActivity.this, "Sorry,this email has already been registered", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                }
            }
        });
    }

    //    check whether the email follows the email format or not
    public Boolean validEmail(String email) {
        return email != null && email.contains("@");
    }

    // check whether the password length larger than 6
    public Boolean validPassword(String password) {
        return password.length() >= 6;
    }
}