package com.example.mobilepaindiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mobilepaindiary.Model.User;
import com.example.mobilepaindiary.databinding.ActivitySignInBinding;
import com.example.mobilepaindiary.viewmodel.SharedViewModel;
import com.google.android.gms.common.util.JsonUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.ls.LSOutput;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;
    //  declare an instance of firebaseAuth
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        initialize the firebaseAuth instance
        auth = FirebaseAuth.getInstance();


        binding.showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                show the password
                if (isChecked) {
                    binding.userPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                } else {
                    binding.userPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


/**
 * sign in
 */
// this is an event listener, and it contains the a single callback method (onclick) to be called when the View
// to which the listener has been registered is triggered by user interaction with the UI item
        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                get the user input with email and password
                String userEmail = binding.userEmail.getText().toString();
                String userPassword = binding.userPassword.getText().toString();

                if (userEmail.isEmpty()||userPassword.isEmpty()){
                    Toast.makeText(SignInActivity.this, "Please input your email and password!", Toast.LENGTH_SHORT).show();
                }else {
//                use firebase to verify the user information
                    auth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignInActivity.this, "Wrong email or password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                go to the register activity to let user input some registration information
                Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}
