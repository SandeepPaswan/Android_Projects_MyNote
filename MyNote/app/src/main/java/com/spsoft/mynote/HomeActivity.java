package com.spsoft.mynote;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HomeActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    FirebaseUser firebaseUser;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    FirestoreRecyclerAdapter<Notemodel,NoteViewHolder>noteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);




        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);



        FloatingActionButton addnote = (FloatingActionButton) findViewById(R.id.flotaddnote);
        addnote.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "Add notes", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HomeActivity.this, AddNoteActivity.class);
            startActivity(intent);
        });

        Query query = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").orderBy("time",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Notemodel> allnotes = new FirestoreRecyclerOptions.Builder<Notemodel>().setQuery(query,Notemodel.class).build();

        noteAdapter = new FirestoreRecyclerAdapter<Notemodel, NoteViewHolder>(allnotes) {  //2
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Notemodel model) {

                holder.noteView.setText(model.getNote());
                holder.titleView.setText(model.getTitle());
                holder.addressView.setText(model.getaddress());
                CharSequence time = DateFormat.format("EEEE, MMM d, yyyy hh:mm:ss a", model.getTime().toDate());     //1
                holder.dateView.setText(time);





                String docId = noteAdapter.getSnapshots().getSnapshot(0).getId();
            holder.itemView.setOnClickListener(v -> {
                Intent i = new Intent(v.getContext(), ShowNoteActivity.class);
                i.putExtra("title",model.getTitle());
                i.putExtra("note",model.getNote());
                i.putExtra("noteId",docId);
                v.getContext().startActivity(i);
            });
                ImageView delbtn = holder.itemView.findViewById(R.id.delbtn);
                delbtn.setOnClickListener(v -> {
                    PopupMenu opPopup = new PopupMenu(v.getContext(),v);
                    opPopup.setGravity(Gravity.END);
                    opPopup.getMenu().add("Edit").setOnMenuItemClickListener(item -> {

                        Intent intent = new Intent(v.getContext(), ShowNoteActivity.class);
                        intent.putExtra("title",model.getTitle());
                        intent.putExtra("note",model.getNote());
                        intent.putExtra("noteId",docId);
                        v.getContext().startActivity(intent);

                        finish();
                        return false;
                    });

                    opPopup.getMenu().add("delete").setOnMenuItemClickListener(item -> {
                        DocumentReference documentReference = firebaseFirestore.collection("notes")
                                .document(firebaseUser.getUid()).collection("myNotes").document(docId);
                        documentReference.delete().addOnSuccessListener(unused -> {
                            Toast.makeText(HomeActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent(HomeActivity.this, HomeActivity.class );
                            startActivity(intent);
                            finish();
                        }).addOnFailureListener(e -> Toast.makeText(HomeActivity.this, "Failed to Deleted", Toast.LENGTH_SHORT).show());
                        return false;
                    });
                    opPopup.show();
                });


            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout,parent,false);
                return new NoteViewHolder(view);
            }
        };

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        recyclerView.setAdapter(noteAdapter);
    }


    public static class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView noteView,titleView,dateView,addressView;
        LinearLayout rlnote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            addressView = itemView.findViewById(R.id.addressView);
            noteView = itemView.findViewById(R.id.notecontent);
            titleView = itemView.findViewById(R.id.notetitle);
            dateView = itemView.findViewById(R.id.dateview);
            rlnote = itemView.findViewById(R.id.notess);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(noteAdapter != null) {
            noteAdapter.stopListening();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search note...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.setting:{
                finish();
                Intent intent =new Intent(HomeActivity.this, SettingsActivity.class );
                startActivity(intent);
                Toast.makeText(HomeActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}