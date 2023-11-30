package com.gymhomie;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class popup_ListHomies extends Activity {
    private LinearLayout sv;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public boolean isFriend;
    private ArrayList<Homie> requestList = new ArrayList<Homie>();
    private ArrayList<Homie> homieList = new ArrayList<Homie>();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    String userID = auth.getCurrentUser().getUid();
    String collectionPath = "users/" + userID + "/Homies";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_list_homies);
        sv = findViewById(R.id.linearHomies);
        getHomies();
        DisplayMetrics dm = new DisplayMetrics();

        Button viewRequestsBtn = new Button (this);
        viewRequestsBtn.setText("Requests");
        ConstraintLayout cl = findViewById(R.id.constraintHomieList);

        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .9), (int) (height * .7));
        ConstraintSet constraintSet= new ConstraintSet();
        constraintSet.clone(cl);


      //  constraintSet.connect()


    }

    //Grabs homies from databases and stores them in arraylist
    private void getHomies(){
        db.collection(collectionPath).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                           String homiesID = document.getString("HomieID");
                            String documentPath = "users/"+homiesID; // Replace with your actual collection name and document ID
                            if (document.getBoolean("isHomie")){
                                DocumentReference docRef = db.document(documentPath);

// Retrieve the document
                                docRef.get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (documentSnapshot.exists()) {
                                                    Homie temp = new Homie(documentSnapshot.getString("firstName"), documentSnapshot.getString("lastName"),documentSnapshot.getString("email"),docRef );
                                                    homieList.add(temp);
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Button tempView = new Button(getApplicationContext());
                                                            tempView.setText(temp.getFirstName() + " " + temp.getLastName());
                                                            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                                                            param.gravity = Gravity.CENTER;
                                                            tempView.setLayoutParams(param);
                                                            sv.addView(tempView);
                                                        }
                                                    });
//
//                                                    TextView tempView = new TextView(getApplicationContext());
//                                                   // tempView.setText(temp.getFirstName() + temp.getLastName());
//                                                    tempView.setText("Blue");
//                                                    tempView.setLayoutParams(
//                                                            new ConstraintLayout.LayoutParams(
//                                                                    ViewGroup.LayoutParams.WRAP_CONTENT,
//                                                                    ViewGroup.LayoutParams.WRAP_CONTENT));
//                                                    sv.addView(tempView);
                                                } else {

                                                }
                                            }
                                        });
                                //addToHomieList(documentPath);
                            } else if (!document.getBoolean("isSender")) {
                                addToRequestList(documentPath);

                            }


                        }
//                        sv.removeAllViews();
//                        for(int i = 0; i < homieList.size(); i++){
//
//                            TextView tempView = new TextView(getApplicationContext());
//                            tempView.setText(homieList.get(i).getFirstName() + homieList.get(i).getLastName());
//                            sv.addView(tempView);
//                        }
                    }



                });

    }

    private void addToHomieList(String documentPath) {
        DocumentReference docRef = db.document(documentPath);

// Retrieve the document
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Homie temp = new Homie(documentSnapshot.getString("firstName"), documentSnapshot.getString("lastName"),documentSnapshot.getString("email"),docRef );
                            homieList.add(temp);
                        } else {

                        }
                    }
                });
    }
    private void addToRequestList(String documentPath) {
        DocumentReference docRef = db.document(documentPath);

// Retrieve the document
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Homie temp = new Homie(documentSnapshot.getString("firstName"), documentSnapshot.getString("lastName"),documentSnapshot.getString("email"),docRef );
                            requestList.add(temp);
                        } else {

                        }
                    }
                });
    }

    private void populateFriends(){
        sv.removeAllViews();
        for(int i = 0; i < homieList.size(); i++){
            TextView temp = new TextView(this);
            temp.setText(homieList.get(i).getFirstName() + homieList.get(i).getLastName());
            sv.addView(temp);
        }
    }
    private void populateRequests(){

    }

     public class Homie{


         String firstName;
        String lastName;
        String email;
        DocumentReference ref;
        public Homie(String fLame, String lName, String email, DocumentReference ref){
            this.firstName = fLame;
            this.lastName = lName;
            this.email = email;
            this.ref = ref;
        }
         public String getFirstName() {
             return firstName;
         }

         public void setFirstName(String firstName) {
             this.firstName = firstName;
         }

         public String getLastName() {
             return lastName;
         }

         public void setLastName(String lastName) {
             this.lastName = lastName;
         }

         public String getEmail() {
             return email;
         }

         public void setEmail(String email) {
             this.email = email;
         }

         public DocumentReference getRef() {
             return ref;
         }

         public void setRef(DocumentReference ref) {
             this.ref = ref;
         }
     }
}
