package com.example.travelmantics.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelmantics.R;
import com.example.travelmantics.models.Resort;
import com.example.travelmantics.utilities.FirebaseUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class EditDealActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private  EditText title;
    private  EditText price ;
    private  EditText description ;
    private ImageView selectedImg;
    private Resort resort;
    private static final int PICTURE_RESULT = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rating);
        firebaseDatabase = FirebaseUtil.firebaseDatabase;
        databaseReference = FirebaseUtil.databaseReference;

        title = findViewById(R.id.dealTitle);
        price = findViewById(R.id.dealPrice);
        description = findViewById(R.id.dealDescription);
        selectedImg = findViewById(R.id.selectedImg);

        Intent intent = getIntent();
        Resort resort = (Resort) intent.getSerializableExtra("Deal");

        if (resort == null){
            resort = new Resort();
        }

        this.resort = resort;
        title.setText(resort.getTitle());
        price.setText(resort.getPrice());
        description.setText(resort.getDescription());
        showImage(resort.getImageUrl());
        Button btnImage = findViewById(R.id.selectImg);
        btnImage.setOnClickListener(v->{
            Intent intentAction = new Intent(Intent.ACTION_GET_CONTENT);
            intentAction.setType("image/jpeg");
            intentAction.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            startActivityForResult(intentAction.createChooser(intentAction,
                    "Insert Picture"), PICTURE_RESULT);

        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_rating_menu, menu);

        if (FirebaseUtil.isAdmin) {
            menu.findItem(R.id.delete_rating).setVisible(true);
            menu.findItem(R.id.save_rating).setVisible(true);
            enableEditTexts(true);
            findViewById(R.id.selectImg).setEnabled(true);
        }
        else {
            menu.findItem(R.id.delete_rating).setVisible(false);
            menu.findItem(R.id.save_rating).setVisible(false);
            enableEditTexts(false);
            findViewById(R.id.selectImg).setEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_rating:
                saveDeal();
                Toast.makeText(this, "Deal has been saved", Toast.LENGTH_LONG).show();
                clean();
                backToList();
                break;
            case  R.id.delete_rating:
                deleteDeal();
                Toast.makeText(this, "Deal has been deleted", Toast.LENGTH_LONG).show();
                backToList();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE_RESULT && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            assert imageUri != null;
            StorageReference ref = FirebaseUtil.mStorageRef.child(imageUri.getLastPathSegment());
            ref.putFile(imageUri).addOnSuccessListener(this, taskSnapshot -> {
                String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                String pictureName = taskSnapshot.getStorage().getPath();
                resort.setImageUrl(url);
                resort.setImageName(pictureName);
                Log.d("Url: ", url);
                Log.d("Name", pictureName);
                showImage(url);
            });

        }
    }

    private void backToList() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void deleteDeal() {
        if (resort == null) {
            Toast.makeText(this, "Please save the deal before deleting", Toast.LENGTH_SHORT).show();
            return;
        }
        databaseReference.child(resort.getId()).removeValue();
        Log.d("image name", resort.getImageName());
        if(resort.getImageName() != null && !resort.getImageName().isEmpty()) {
            StorageReference picRef = FirebaseUtil.mStorage.getReference().child(resort.getImageName());
            picRef.delete().addOnSuccessListener(aVoid -> Log.d("Delete Image", "Image Successfully Deleted"))
                    .addOnFailureListener(e -> Log.d("Delete Image", Objects.requireNonNull(e.getMessage())));
        }

    }

    private void clean() {

        title.setText("");
        price.setText("");
        description.setText("");
        title.requestFocus();
    }

    private void saveDeal() {
        resort.setPrice(price.getText().toString().trim());
        resort.setDescription(description.getText().toString().trim());
        resort.setTitle(title.getText().toString().trim());
        //resort.setImageUrl("");
        if(resort.getId()==null) {
            databaseReference.push().setValue(resort);
            Log.d(EditDealActivity.class.getSimpleName(), "Saved");
        }
        else {
            databaseReference.child(resort.getId()).setValue(resort);
        }
    }
    private void enableEditTexts(boolean isEnabled) {
        title.setEnabled(isEnabled);
        description.setEnabled(isEnabled);
        price.setEnabled(isEnabled);
    }
    private void showImage(String url) {
        if (url != null && !url.isEmpty()) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
//            Glide.with(this)
//                    .load(url)
//                    .into(selectedImg);
            Picasso.with(this)
                    .load(url)
                   .resize(width, width*2/3)
                    .centerCrop()
                    .into(selectedImg);
        }
    }

}
