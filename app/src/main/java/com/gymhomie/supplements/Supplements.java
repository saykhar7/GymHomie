package com.gymhomie.supplements;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.gymhomie.R;

public class Supplements extends AppCompatActivity {




    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String userID = auth.getCurrentUser().getUid();

    SuppmentAdapter suppmentAdapter;

    FloatingActionButton addSupplementsBtn;

    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplements);


        addSupplementsBtn = findViewById(R.id.addSupplement_btnID);
        recyclerView = findViewById(R.id.supp_recyclev_ID);

        setupRecyclerView();
        addSupplementsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Supplements.this, AddSupplementsActivity.class);
                startActivity(i);


            }
        });

    }

    private void setupRecyclerView() {






        Query query =  FirebaseFirestore.getInstance().collection("Supplements").orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<SupplementPost> options = new FirestoreRecyclerOptions.Builder<SupplementPost>()
                .setQuery(query, SupplementPost.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        suppmentAdapter = new SuppmentAdapter(options, this);
        recyclerView.setAdapter(suppmentAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        suppmentAdapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        suppmentAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        suppmentAdapter.notifyDataSetChanged();
    }
}