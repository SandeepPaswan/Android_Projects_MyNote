package com.spsoft.mynote;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;


public class ForgotActivity extends AppCompatActivity {

    private EditText forgotpass;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);


        forgotpass = findViewById(R.id.foremail);
        Button submit = findViewById(R.id.btnforgotsubmit);
        mAuth  = FirebaseAuth.getInstance();

        submit.setOnClickListener(view -> {
            String mail = forgotpass.getText().toString().trim();
            if(mail.isEmpty())
            {
                Toast.makeText(ForgotActivity.this, "Enter Your Email First", Toast.LENGTH_SHORT).show();
            }
            else
            {
                mAuth.sendPasswordResetEmail(mail).addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(ForgotActivity.this, "Mail Sent", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(ForgotActivity.this, LoginActivity.class));
                    }
                    else
                    {
                        Toast.makeText(ForgotActivity.this, "Account Not Exist", Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });


    }
}