package com.gymhomie.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gymhomie.Achievement_Activity;
import com.gymhomie.Goal_Activity;
import com.gymhomie.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;


public class profile_fragment extends Fragment {
    private Button btnLogout;
    private Button btnGoals;
    private Button btnAchievements;
    private ImageView profileBadge;
    private ImageView profilePicture;
    private TextView profileName;
    private TextView profileEmail;
    private TextView profileBadgeName;
    private OnLogoutClickListener onLogoutClickListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String userID = auth.getCurrentUser().getUid();
    private static final int achReqCode = 100;
    private static final int PICK_IMAGE_REQUEST = 2;
    private View view;
    public profile_fragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DocumentReference userDoc = db.collection("users").document(userID);
        userDoc.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String first = (documentSnapshot.get("firstName").toString());
                        String last = (documentSnapshot.get("lastName").toString());
                        profileName.setText(first+ " " + last);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Profile Fragment", "No user doc info found");
                        profileName.setText("DNE");
                    }
                });
        String email = auth.getCurrentUser().getEmail();
        profileEmail.setText(email);

        profileBadge = view.findViewById(R.id.profileBadge);
        profileBadgeName = view.findViewById(R.id.profileBadgeLabel);
        profilePicture = view.findViewById(R.id.profilePicture);
        btnLogout = view.findViewById(R.id.logoutBtn);
        btnGoals = view.findViewById(R.id.goalsBtn);

        updateBadge(view);
        Bitmap profilePictureBitmap = loadProfilePicture();
        if (profilePictureBitmap != null){
            profilePicture.setImageBitmap(profilePictureBitmap);
        }

        btnGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Goal_Activity.class);
                startActivity(intent);
            }
        });
        btnAchievements = view.findViewById(R.id.achievementsButton);

        btnLogout.setOnClickListener(v -> {
            if (onLogoutClickListener != null) {
                onLogoutClickListener.onLogout();
            }
        });
        btnAchievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start the achievements activity
                Intent intent = new Intent(getActivity(), Achievement_Activity.class);
                startActivity(intent);
            }
        });
        profileBadge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the achievements so the user can pick their favorite badge
                Intent intent = new Intent(getActivity(), Achievement_Activity.class);
                startActivity(intent);
            }
        });
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFilesPermission()) {
                    openFilePicker();
                }
            }
        });
        return view;
    }
    private Bitmap loadProfilePicture() {
        // loads profile picture that was previously saved
        try {
            File internalStorageDir = getActivity().getFilesDir();
            File profilePictureFile = new File(internalStorageDir, "profile_picture.png");
            if (profilePictureFile.exists()) {
                return BitmapFactory.decodeFile(profilePictureFile.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

     }
    private void handleSelectedProfilePicture(Uri profilePictureData) {
        // handles a passed uri (from user selection in openFilePicker()), displays on profile
        Bitmap bitmap = loadBitmapFromUri(profilePictureData);
        if (bitmap != null) {
            profilePicture.setImageBitmap(bitmap);
            saveProfilePicture(bitmap);
        }
    }
    private void saveProfilePicture(Bitmap bitmap) {
        // saves a profile picture that was selected by the user
        try {
            File internalStorageDir = getActivity().getFilesDir();
            File profilePictureFile = new File(internalStorageDir, "profile_picture.png");
            FileOutputStream outputStream = new FileOutputStream(profilePictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Bitmap loadBitmapFromUri(Uri uri) {
        // uri mapping into image resource
        try {
            // Use the content resolver to open the stream for the specified URI
            getActivity().getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private void openFilePicker() {
        // opens files for user to choose a profile picture
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            handleSelectedProfilePicture(data.getData());
        }
    }
    private void updateBadge(View view) {
        // let's update the badge icon on the users profile
        Map<String, Integer> imageResourceMap = new HashMap<>();
        imageResourceMap.put("1", R.drawable.achievement_1_unlocked);
        imageResourceMap.put("2", R.drawable.achievement_2_unlocked);
        imageResourceMap.put("3", R.drawable.achievement_3_unlocked);
        imageResourceMap.put("4", R.drawable.achievement_4_unlocked);

        DocumentReference docRef = db.collection("users").document(userID);
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String achID;
                        String achName;
                        if (!documentSnapshot.contains("badge")) {
                            // badge has not been set for current user, set to -1 for default
                            achID = "-1";
                            docRef.update("badge", achID);
                            docRef.update("achName", "Achievement");
                            profileBadge.setImageResource(R.drawable.trophy_unlocked);
                        }
                        else if (!documentSnapshot.contains("achName")) {
                            docRef.update("achName", "Achievement");
                            profileBadge.setImageResource(R.drawable.trophy_unlocked);
                        }
                        else {
                            achID = documentSnapshot.get("badge").toString();
                            achName = documentSnapshot.get("achName").toString();
                            if (achID.equals("-1")) {
                                profileBadge.setImageResource(R.drawable.trophy_unlocked);
                            }
                            else {
                                // dynamically set their badge
                                Log.d("Profile Achievement Update", achID);
                                profileBadge.setImageResource(imageResourceMap.get(achID));
                                profileBadgeName.setText(achName);
                            }
                        }
                    }
                });
    }

    public void setOnLogoutClickListener(OnLogoutClickListener listener) {
        this.onLogoutClickListener = listener;
    }

    public interface OnLogoutClickListener {
        void onLogout();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateBadge(view);
    }
    private boolean checkFilesPermission() {
        // TODO: when accepted, does not return true
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 1);
            return false;
        }
        return true;
    }
}