package com.gymhomie;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.gymhomie.workouts.WorkoutHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class popup_ListHomies extends Activity {
    private LinearLayout sv;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public boolean isFriend;
    private int count = 0;
    private ArrayList<Homie> requestList = new ArrayList<Homie>();
    private ArrayList<Homie> homieList = new ArrayList<Homie>();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    String userID = auth.getCurrentUser().getUid();
    Homie homie = new Homie();
    ArrayList<String> paths = homie.getPaths();
    String collectionPath = "users/" + userID + "/Homies";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_list_homies);
        sv = findViewById(R.id.linearHomies);
        getHomies();
        DisplayMetrics dm = new DisplayMetrics();

        //Button viewRequestsBtn = new Button (this);
        //viewRequestsBtn.setText("Requests");
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
                        WorkoutHelper wh = new WorkoutHelper();
                        wh.setHomieWorkouts(paths);
                        ArrayList<ArrayList<Map<String, Object>>> workouts = wh.getHomieWorkouts();
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
                                                    final int currentCount = count;
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
                                                            //ArrayList<Map<String, Object>> workouts = wh.getHomieWorkout(currentCount);
                                                            // Set an OnClickListener for each button
                                                            //int finalCount = count;
                                                            tempView.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    showProfileConfirmationDialog(workouts.get(currentCount), temp.getFirstName() + " " + temp.getLastName(), currentCount, v);
                                                                }
                                                            });
                                                            count++;
                                                        }
                                                    });
                                                } else {

                                                }
                                            }
                                        });
                                //addToHomieList(documentPath);
                            } else if (!document.getBoolean("isSender")) {
                                addToRequestList(documentPath);

                            }
                        }
                    }



                });

    }
    // Show a confirmation dialog to view the homie's profile
    private void showProfileConfirmationDialog(ArrayList<Map<String, Object>> workouts, String homieName, int count, View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("View Profile");
        builder.setMessage("Do you want to view " + homieName + "'s profile?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the user's decision to view the profile
                // You can launch the profile activity or perform any other action here
                // Now, you have a list of workout objects. You can update your UI here.
                updateUI(homieName, workouts, v);
                Toast.makeText(popup_ListHomies.this, "Viewing " + homieName + "'s profile", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the user's decision not to view the profile
                dialog.dismiss();
            }
        });

        builder.show();
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
    private void updateUI(String homieName, ArrayList<Map<String, Object>> workouts, View v) {
        // Inflate the homie_profile_view.xml layout
        View profileView = getLayoutInflater().inflate(R.layout.homie_profile, null);

        // Set the homieNameTextView
        TextView homieNameTextView = profileView.findViewById(R.id.homieNameTextView);
        homieNameTextView.setText(homieName);

        // Find the RecyclerView in the profileView
        RecyclerView recyclerView = profileView.findViewById(R.id.workoutsRecyclerView);

        // Create a new instance of HomieProfileViewAdapter for each homie
        HomieProfileViewAdapter adapter = new HomieProfileViewAdapter(workouts);

        // Set the item click listener
        adapter.setOnItemClickListener(new HomieProfileViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle the item click here
                showAddWorkoutConfirmationDialog(workouts.get(position));
            }
        });

        // Set the adapter to your RecyclerView
        recyclerView.setAdapter(adapter);

        // Optionally, you can set a layout manager to define how items are arranged
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Show the profileView in a dialog or any other way you want
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(profileView);
        builder.show();
    }

    private void showAddWorkoutConfirmationDialog(Map<String, Object> workout) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Workout");
        builder.setMessage("Do you want to add this workout?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the user's decision to add the workout
                // You can add the workout to the user's collection here
                // ...
                addWorkoutToFirestore(workout);
                Toast.makeText(popup_ListHomies.this, "Workout added!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the user's decision not to add the workout
                dialog.dismiss();
                dialog.dismiss();
            }
        });

        builder.show();
    }
    private void addWorkoutToFirestore(Map<String, Object> workout) {
        // Access a Cloud Firestore instance
        Map<String, Object> homieWorkout = new HashMap<>();
        homieWorkout.put("Workout",workout);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userID = auth.getCurrentUser().getUid();
        String userWorkoutsPath = "users/" + userID + "/Workouts";

        // Assuming you have a collection named "workouts" in your Firestore
        // and you want to add the workout data to this collection
        db.collection(userWorkoutsPath)
                .add(homieWorkout)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // DocumentSnapshot added with ID: documentReference.getId()
                        Log.d(TAG, "Workout added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding workout", e);
                        Toast.makeText(popup_ListHomies.this, "Error adding workout", Toast.LENGTH_SHORT).show();
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
         public Homie(){
             paths = new ArrayList<>();
             homie = new ArrayList<>();
             setPaths(paths);
         }
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
         ArrayList<String> homie;
         public ArrayList<String> getPaths() {
             return paths;
         }

         public void setPaths(ArrayList<String> homiePath) {
             String userID = auth.getCurrentUser().getUid();
             String thisUserPathtoHomies = "users/" + userID + "/Homies";
             db.collection(thisUserPathtoHomies).get()
                     .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                         @Override
                         public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                             for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                 String id = document.getString("HomieID");
                                 homiePath.add("users/" + id);
                             }
                             for(String path: homiePath){
                                 setHomies(path);
                             }
                         }
                     });
             this.paths = homiePath;
         }

         ArrayList<String> paths;
         public ArrayList<String> getHomies(){
             return homie;
         }
         public void setHomies(String path){
             String userID = auth.getCurrentUser().getUid();
             String currUserPathtoHomies = path;
             db.collection("users").get()
                     .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                         @Override
                         public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                             for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                                 if(documentSnapshot.getId().equalsIgnoreCase(currUserPathtoHomies.substring(6,currUserPathtoHomies.length()-1)))
                                 {
                                     String firstName = documentSnapshot.get("firstName").toString();
                                     String lastName = documentSnapshot.get("lastName").toString();
                                     homie.add(firstName + " " + lastName);
                                 }
                             }
                         }
                     });
         }
     }
}
