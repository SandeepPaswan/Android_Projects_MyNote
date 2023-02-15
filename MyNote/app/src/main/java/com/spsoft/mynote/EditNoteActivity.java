package com.spsoft.mynote;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditNoteActivity extends AppCompatActivity {

    Intent data;

    EditText editnote,edittitle;


    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);



        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        data = getIntent();

         editnote = findViewById(R.id.noteeditcontent);
         edittitle = findViewById(R.id.noteedittitle);


        editnote.setMovementMethod(new ScrollingMovementMethod());
        edittitle.setMovementMethod(new ScrollingMovementMethod());


        editnote.setText(data.getStringExtra("note"));
        edittitle.setText(data.getStringExtra("title"));




        BottomNavigationView navigationView = findViewById(R.id.save_edit_Layout);

        navigationView.setOnNavigationItemSelectedListener(navsListener);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navsListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.cancel:
                            Intent intent = new Intent(EditNoteActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(EditNoteActivity.this, "cancel", Toast.LENGTH_SHORT).show();
                            break;

                        case R.id.save:
                        //    Toast.makeText(EditNoteActivity.this, " Save update ", Toast.LENGTH_SHORT).show();

                            String newtitle = edittitle.getText().toString();
                            String newnote = editnote.getText().toString();

                            if(newnote.isEmpty() || newtitle.isEmpty())
                            {
                                Toast.makeText(EditNoteActivity.this, "Empty error", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Map<String,Object> note= new HashMap<>();
                                note.put("title",newtitle);
                                note.put("note",newnote);
                                note.put("time",new Timestamp(new Date()));


                                DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(data.getStringExtra("noteId"));
                                documentReference.set(note).addOnSuccessListener(unused -> {
                                    Toast.makeText(EditNoteActivity.this, "Note Updated", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(EditNoteActivity.this,HomeActivity.class));
                                    finish();

                                }).addOnFailureListener(e -> Toast.makeText(EditNoteActivity.this, "Update Failed", Toast.LENGTH_SHORT).show());
                            }
                            break;
                    }
                    return true;
                }
            };
}