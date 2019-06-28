package com.mataprojects.androidcourse_firebaseproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {
    private EditText registrationMail;
    private EditText registrationPassword;

    private Button registrationSignInButton;
    private Button registrationSignUpButton;

    private FirebaseAuth fAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        fAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);


        registrationMail = findViewById(R.id.registration_email_singUp);
        registrationPassword = findViewById(R.id.registration_password_singUp);

        registrationSignInButton = findViewById(R.id.registration_signIn_Button);
        registrationSignUpButton = findViewById(R.id.registration_signUp_Button);

        registrationSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mMail = registrationMail.getText().toString().trim();
                String mPassword = registrationPassword.getText().toString().trim();

                if (TextUtils.isEmpty(mMail)){
                    registrationMail.setError("Required Field...Mail");
                    return;
                }
                if (TextUtils.isEmpty(mPassword)){
                    registrationPassword.setError("Required Field....Password");
                    return;
                }
                progressDialog.setMessage("Processing...");
                progressDialog.show();
                fAuth.createUserWithEmailAndPassword(mMail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            Toast.makeText(getApplicationContext(),"Correct ", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }else{
                            Toast.makeText(getApplicationContext(),"Registration error...",Toast.LENGTH_SHORT).show();
                            System.out.println(task.getException());

                        }
                    }
                });
            }
        });

        registrationSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });


    }
}
