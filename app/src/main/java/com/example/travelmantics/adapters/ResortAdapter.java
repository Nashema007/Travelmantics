package com.example.travelmantics.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelmantics.R;
import com.example.travelmantics.activities.EditDealActivity;
import com.example.travelmantics.models.Resort;
import com.example.travelmantics.utilities.FirebaseUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ResortAdapter extends RecyclerView.Adapter<ResortAdapter.MyViewHolder>{

    private ArrayList<Resort> resorts;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private ImageView image;

    public ResortAdapter() {
        firebaseDatabase = FirebaseUtil.firebaseDatabase;
        databaseReference = FirebaseUtil.databaseReference;
        this.resorts = FirebaseUtil.resorts;
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Resort resort = dataSnapshot.getValue(Resort.class);
                resort.setId(dataSnapshot.getKey());
                resorts.add(resort);
                notifyItemInserted(resorts.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addChildEventListener(childEventListener);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.price.setText(resorts.get(position).getPrice());
        holder.title.setText(resorts.get(position).getTitle());
        holder.description.setText(resorts.get(position).getDescription());

        holder.itemView.setOnClickListener(v->{
            Resort selectedResort = resorts.get(position);
            Intent intent = new Intent(holder.itemView.getContext(), EditDealActivity.class);
            intent.putExtra("Deal", selectedResort);
            holder.itemView.getContext().startActivity(intent);

        });
        String url = resorts.get(position).getImageUrl();
        if (url != null && !url.isEmpty()) {
            Picasso.with(image.getContext())
                    .load(url)
                    .resize(80, 80)
                    .centerCrop()
                    .into(image);
        }
    }

    @Override
    public int getItemCount() {
        return resorts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView price;
        private TextView description;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.resortTitle);
            description = itemView.findViewById(R.id.resortDescription);
            image = itemView.findViewById(R.id.resortImage);
            price = itemView.findViewById(R.id.resortPrice);
        }
    }
}
