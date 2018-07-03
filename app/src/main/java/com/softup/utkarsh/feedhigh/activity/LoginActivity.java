package com.softup.utkarsh.feedhigh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.softup.utkarsh.feedhigh.R;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;

    private EditText email;
    private EditText password;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailpassword);

        mAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.et_email);
        password = (EditText) findViewById(R.id.et_password);

        findViewById(R.id.btn_registration).setOnClickListener(this);
        findViewById(R.id.btn_sign_in).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_sign_in){
            signin(email.getText().toString(), password.getText().toString());
        } else if (v.getId() == R.id.btn_registration) {
            registration(email.getText().toString(), password.getText().toString());
        }
    }

    public void signin(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Авторизация успешна", Toast.LENGTH_SHORT)
                            .show();
                             Intent intent = new Intent(LoginActivity.this, DepartmentReview.class);
                             startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Авторизация провалена", Toast.LENGTH_SHORT)
                            .show();
                        }
                    }
                });
    }
    public void registration(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT)
                            .show();
                        } else {
                            Log.e("register", task.getException().toString());
                            Toast.makeText(LoginActivity.this, "Регистрация провалена", Toast.LENGTH_SHORT)
                            .show();
                        }
                    }
                });
    }

    public static void signout(){
        FirebaseAuth.getInstance().signOut();
    }
}