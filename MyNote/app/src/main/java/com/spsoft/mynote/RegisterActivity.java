package com.spsoft.mynote;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email, password;
    private Button btnsubmit;
    private TextView gologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.regemail);
        password = findViewById(R.id.regpassword);
        btnsubmit = findViewById(R.id.btnregsubmit);
        gologin = findViewById(R.id.btntologin);

        gologin.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });


        btnsubmit.setOnClickListener(view -> btnsubmit());

    }

    private void btnsubmit() {
        String user = email.getText().toString().trim();
        String pass1 = password.getText().toString().trim();
        if (user.isEmpty()) {
            email.setError("Email can not be empty");
            email.requestFocus();
        }
        else if (pass1.isEmpty()) {
            password.setError("Password can not be empty");
            password.requestFocus();
        } else {
            mAuth.createUserWithEmailAndPassword(user, pass1).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                finish();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                //    sendEmailVerification();

                } else {
                    Toast.makeText(RegisterActivity.this, "Registration Failed" , Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}