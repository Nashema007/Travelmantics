package com.example.travelmantics.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelmantics.R;
import com.example.travelmantics.adapters.ResortAdapter;
import com.example.travelmantics.utilities.FirebaseUtil;
import com.firebase.ui.auth.AuthUI;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        MenuItem insertMenu = menu.findItem(R.id.edit_deal);
        if (FirebaseUtil.isAdmin) {
            insertMenu.setVisible(true);
        }
        else {
            insertMenu.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                AuthUI.getInstance().signOut(this)
                        .addOnCompleteListener(task -> FirebaseUtil.attachListner());
                FirebaseUtil.detachListner();
                break;
            case R.id.edit_deal:
                Intent intent = new Intent(this, EditDealActivity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUtil.detachListner();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUtil.openFbReference("traveldeals", this);
        RecyclerView resortRecyclerView = findViewById(R.id.resortRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        resortRecyclerView.setLayoutManager(layoutManager);
        resortRecyclerView.setHasFixedSize(true);
        ResortAdapter resortAdapter = new ResortAdapter();
        resortRecyclerView.setAdapter(resortAdapter);

        FirebaseUtil.attachListner();
    }
    public void showMenu() {
        invalidateOptionsMenu();
    }
}

