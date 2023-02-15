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

public class LoginActivity extends AppCompatActivity {

    private EditText txtEmail, txtpassword;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //set find by id
        mAuth = FirebaseAuth.getInstance();

        Button btnsubmit = findViewById(R.id.btnsubmit);
        TextView btnforgot = findViewById(R.id.btnforgot);
        TextView btnRegister = findViewById(R.id.btnsignup);
        txtEmail = findViewById(R.id.username);
        txtpassword = findViewById(R.id.password);

        // set button
        btnsubmit.setOnClickListener(view -> login());

        btnforgot.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, ForgotActivity.class)));

        btnRegister.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    //set login
    private void login()
    {
        String user =txtEmail.getText().toString().trim();
        String pass1 = txtpassword.getText().toString().trim();
        if(user.isEmpty())
        {
            txtEmail.setError("Email can not be empty");
            txtEmail.requestFocus();
        }
        else if(pass1.isEmpty())
        {
            txtpassword.setError("Password can not be empty");
            txtpassword.requestFocus();
        }
        else
        {
            mAuth.signInWithEmailAndPassword(user,pass1).addOnCompleteListener(task -> {

                if(task.isSuccessful())
                {
                //    Toast.makeText(LoginActivity.this, "Login Successfully"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                //    emailverification();
                    Toast.makeText(LoginActivity.this,"Login Successfully",Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                }
            });
        }
    }


@Override
    protected void onStart() {

        super.onStart();


        if (mAuth.getCurrentUser() != null){
                    Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                    finish();
        }
        else {
//            startActivity(new Intent(this,HomeActivity.class));
        }
    }

}