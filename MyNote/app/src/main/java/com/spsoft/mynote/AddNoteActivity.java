package com.spsoft.mynote;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddNoteActivity extends AppCompatActivity implements LocationListener {

    private EditText addtitle, addnote,getlocation;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    Button btngetlocation;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);



        addtitle = findViewById(R.id.typetitle);
        addnote = findViewById(R.id.typenote);
        getlocation = findViewById(R.id.viewLocation);
        btngetlocation = findViewById(R.id.getlocation);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(ContextCompat.checkSelfPermission(AddNoteActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AddNoteActivity.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }

        btngetlocation.setOnClickListener(v -> getLocation());

        BottomNavigationView navigationView = findViewById(R.id.save_Layout);
        navigationView.setOnNavigationItemSelectedListener(navListener);

    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,AddNoteActivity.this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.cancel:
                            Intent intent = new Intent(AddNoteActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(AddNoteActivity.this, "cancel", Toast.LENGTH_SHORT).show();
                            break;

                        case R.id.save:

                            String title = addtitle.getText().toString();
                            String note = addnote.getText().toString();
                            String address= getlocation.getText().toString();


                            if (title.isEmpty() || note.isEmpty()) {
                                Toast.makeText(AddNoteActivity.this, "Both are Require", Toast.LENGTH_SHORT).show();
                            }
                            else if(address.isEmpty()){
                                Toast.makeText(AddNoteActivity.this, "Click on GET CURRENT LOCATION ", Toast.LENGTH_SHORT).show();

                            }

                            else {
                                Map<String, Object> not = new HashMap<>();
                                not.put("title", title);
                                not.put("note", note);
                                not.put("time", new Timestamp(new Date()));
                                not.put("address",address);


                                DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document();
                                documentReference.set(not).addOnSuccessListener(unused -> {
                                    // save toast
                                   startActivity(new Intent(AddNoteActivity.this, HomeActivity.class));
                                }).addOnFailureListener(e -> Toast.makeText(AddNoteActivity.this, "Failed Try Again", Toast.LENGTH_SHORT).show());
                            }
                            break;
                    }
                    return true;
                }

            };


    @Override
    public void onLocationChanged(@NonNull Location location) {
        Toast.makeText(AddNoteActivity.this, ""+location.getLatitude()+" , "+location.getLongitude(), Toast.LENGTH_SHORT).show();

        try {
            Geocoder geocoder =new Geocoder(AddNoteActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String addres = addresses.get(0).getAddressLine(0);
            getlocation.setText(addres);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}
