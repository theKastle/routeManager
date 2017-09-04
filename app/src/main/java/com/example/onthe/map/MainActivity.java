package com.example.onthe.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.onthe.map.MapUtils.RouteReadModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<RouteReadModel> routes;
    private RoutesAdapter adapter;

    private DrawerLayout mDrawLayout;
    private ActionBarDrawerToggle mToggle;

    Button btnGetSharedRoute;
    EditText etEnterSharedKey;

    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser mFirebaseUser;

    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case 0:
                removeRoute(item.getGroupId());
                break;
            case 1:
                goToMap(item.getGroupId());
                break;
            case 2:
                RouteReadModel route = routes.get(item.getGroupId());
                goToViewRouteDetail(route);
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void goToViewRouteDetail(RouteReadModel route) {

        Intent intent = new Intent(getApplicationContext(), ViewRouteDetailActivity.class);
        intent.putExtra("routeKey", route.key);

        startActivity(intent);
    }

    private void goToMap(int position) {
        RouteReadModel route = routes.get(position);
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);

        intent.putExtra("startAddress", route.startAddress);
        intent.putExtra("endAddress", route.endAddress);

        startActivity(intent);
    }

    private void loadLogInView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            // Not logged in, launch the Log In activity
            loadLogInView();
            return;
        }

        setContentView(R.layout.activity_main);

        loadMenu();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users").child(mFirebaseUser.getUid());

        routes = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.routes_list);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(lin);
        adapter = new RoutesAdapter(routes);
        recyclerView.setAdapter(adapter);

        btnGetSharedRoute = (Button) findViewById(R.id.get_shared_route);
        etEnterSharedKey = (EditText) findViewById(R.id.enter_shared_key);

        btnGetSharedRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewRouteDetailActivity.class);
                intent.putExtra("sharedRouteKey", etEnterSharedKey.getText().toString());

                startActivity(intent);
            }
        });

        updateList();
    }

    private void loadMenu() {
        mDrawLayout = (DrawerLayout) findViewById(R.id.activity_main);
        mToggle = new ActionBarDrawerToggle(this, mDrawLayout, R.string.open, R.string.close);

        mDrawLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_menu);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.sign_out:
                        mFirebaseAuth.signOut();
                        loadLogInView();
                        break;
                    case R.id.find_route:
                        Intent findRoute = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(findRoute);
                        break;
                    case R.id.routes_list:
                        Intent viewRoutesList = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(viewRoutesList);
                        break;
                }
                return true;
            }
        });
    }

    private void updateList() {
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("snap SHOT:: ", dataSnapshot.toString());
                RouteReadModel model = (RouteReadModel) dataSnapshot.getValue(RouteReadModel.class);
                routes.add(model);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                RouteReadModel aRoute = dataSnapshot.getValue(RouteReadModel.class);

                int index = getItemIndex(aRoute);

                routes.set(index, aRoute);
                adapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                RouteReadModel aRoute = dataSnapshot.getValue(RouteReadModel.class);

                int index = getItemIndex(aRoute);

                routes.remove(index);
                adapter.notifyItemRemoved(index);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private int getItemIndex (RouteReadModel route) {
        int index = -1;

        for (int i = 0; i < routes.size(); i++) {
            if (routes.get(i).key.equals(route.key)) {
                index = i;
                break;
            }
        }

        return index;
    }

    private void removeRoute(int position){
        myRef.child(routes.get(position).key).removeValue();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected((item))){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
