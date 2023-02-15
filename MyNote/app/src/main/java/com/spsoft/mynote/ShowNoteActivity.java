package com.spsoft.mynote;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShowNoteActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note);


        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        firebaseFirestore = FirebaseFirestore.getInstance();

        data = getIntent();

        TextView note = findViewById(R.id.noteshowcontent);
        TextView title = findViewById(R.id.noteshowtitle);


        note.setMovementMethod(new ScrollingMovementMethod());
        title.setMovementMethod(new ScrollingMovementMethod());


        note.setText(data.getStringExtra("note"));
        title.setText(data.getStringExtra("title"));



        BottomNavigationView navigationView = findViewById(R.id.edit_menu_Layout);
        navigationView.setOnNavigationItemSelectedListener(naveListener);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener naveListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.cancel:
                            Intent intent =new Intent(ShowNoteActivity.this, HomeActivity.class );
                            startActivity(intent);
                            finish();
                            Toast.makeText(ShowNoteActivity.this, "cancel", Toast.LENGTH_SHORT).show();
                            break;

                        case R.id.edit:
                            Toast.makeText(ShowNoteActivity.this, "Edit Your Nate", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(getApplicationContext(),EditNoteActivity.class);
                            i.putExtra("title",data.getStringExtra("title"));
                            i.putExtra("note",data.getStringExtra("note"));
                            i.putExtra("noteId",data.getStringExtra("noteId"));
                            startActivity(i);

                            break;
                    }
                    return true;
                }
            };
}