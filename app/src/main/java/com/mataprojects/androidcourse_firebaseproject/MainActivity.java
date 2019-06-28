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

public class MainActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;

    private Button signInButton;
    private Button signUpButton;

    private FirebaseAuth fAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));

        }
        progressDialog = new ProgressDialog(this);


        email = findViewById(R.id.email_singin);
        password = findViewById(R.id.password_singin);

        signInButton = findViewById(R.id.signin_Button);
        signUpButton = findViewById(R.id.signup_Button);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mMail = email.getText().toString().trim();
                String mPassword = password.getText().toString().trim();

                if (TextUtils.isEmpty(mMail)){
                    email.setError("Required Field...Mail");
                    return ;
                }
                if (TextUtils.isEmpty(mPassword)){
                    password.setError("Required Field....Password");
                    return;
                }
                progressDialog.setMessage("Processing...");
                progressDialog.show();

                fAuth.signInWithEmailAndPassword(mMail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Complete", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            progressDialog.dismiss();
                        }else{
                            Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT).show();
                            System.out.println(task.getException().toString());
                        }

                    }
                });
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));

            }
        });
    }
}
