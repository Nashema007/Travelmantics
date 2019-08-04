package com.example.travelmantics.utilities;

import android.widget.Toast;

import com.example.travelmantics.activities.MainActivity;
import com.example.travelmantics.models.Resort;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseUtil {

    private static final int RC_SIGN_IN = 123;
    public static FirebaseDatabase firebaseDatabase;
    public static DatabaseReference databaseReference;
    public static FirebaseAuth firebaseAuth;
    public static FirebaseAuth.AuthStateListener authStateListener;
    private static FirebaseUtil firebaseUtil;
    public static ArrayList<Resort> resorts;
    private static MainActivity caller;
    public static boolean isAdmin;
    public static FirebaseStorage mStorage;
    public static StorageReference mStorageRef;

    private FirebaseUtil(){}

    public static void openFbReference(String ref, MainActivity callerActivity){

        if(firebaseUtil == null){
            firebaseUtil = new FirebaseUtil();
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();
            caller = callerActivity;
            authStateListener = firebaseAuth -> {
                if(firebaseAuth.getCurrentUser() == null) {
                    FirebaseUtil.singIn();
                }
                else {
                    String userId = firebaseAuth.getUid();
                    checkAdmin(userId);
                }
                Toast.makeText(callerActivity.getBaseContext(), "Welcome back!", Toast.LENGTH_LONG).show();
            };
            connectStorage();
        }
        resorts = new ArrayList<>();
        databaseReference = firebaseDatabase.getReference().child(ref);
    }

    private static void singIn(){
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );


        caller.startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers).build(), RC_SIGN_IN);

    }

    private static void checkAdmin(String uid) {
        FirebaseUtil.isAdmin=false;
        DatabaseReference ref = firebaseDatabase.getReference().child("Administrator")
                .child(uid);
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FirebaseUtil.isAdmin=true;
                caller.showMenu();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        ref.addChildEventListener(listener);
    }

    public static void attachListner(){
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public static void detachListner(){
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    public static void connectStorage() {
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference().child("deals_pictures");
    }
}
