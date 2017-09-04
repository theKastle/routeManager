package com.example.onthe.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onthe.map.MapUtils.RouteReadModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ViewRouteDetailActivity extends AppCompatActivity {
    EditText etFrom;
    EditText etTo;
    EditText etDescription;
    EditText etSharedKey;
    Button btnSave;
    Button btnShare;

    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");
    DatabaseReference sharedRef = database.getReference("shared");

    String key;
    RouteReadModel value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_route_detail);

        etFrom = (EditText) findViewById(R.id.etStartName);
        etTo = (EditText) findViewById(R.id.etEndName);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etSharedKey = (EditText) findViewById(R.id.shared_key);
        btnSave = (Button) findViewById(R.id.save_route_detail);
        btnShare = (Button) findViewById(R.id.share);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null && bundle.containsKey("sharedRouteKey")) {
            key = bundle.getString("sharedRouteKey");

            sharedRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        value = dataSnapshot.getValue(RouteReadModel.class);

                        String origin = value.startAddress;
                        String destination = value.endAddress;
                        String description = value.description;

                        etFrom.setText(origin);
                        etTo.setText(destination);
                        etDescription.setText(description);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if (bundle != null && bundle.containsKey("routeKey")) {
            key = bundle.getString("routeKey");

            myRef.child(mFirebaseUser.getUid()).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        value = dataSnapshot.getValue(RouteReadModel.class);

                        String origin = value.startAddress;
                        String destination = value.endAddress;
                        String description = value.description;

                        etFrom.setText(origin);
                        etTo.setText(destination);
                        etDescription.setText(description);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("BUTTON _ CLICKED ", "Click");
                Map<String, Object> updateValues = value.toMap();
                Map<String, Object> childUpdates = new HashMap<>();

                updateValues.put("description", etDescription.getText().toString());
                childUpdates.put(key, updateValues);

                myRef.child(mFirebaseUser.getUid()).updateChildren(childUpdates);

                Toast.makeText(getApplicationContext(),"Route Saved", Toast.LENGTH_LONG).show();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("SHARED _ CLICKED ", "Click");
                String sharedkey = Long.toHexString(Double.doubleToLongBits(Math.random())).substring(0, 5);
                DatabaseReference sharedRouteKey = sharedRef.child(sharedkey);
                value.key = sharedkey;
                //String sharedkey = sharedRouteKey.getKey();
                sharedRouteKey.setValue(value);

                etSharedKey.setText(sharedkey);
            }
        });
    }

}
