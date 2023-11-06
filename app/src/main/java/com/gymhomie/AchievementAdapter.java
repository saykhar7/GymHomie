package com.gymhomie;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gymhomie.tools.Achievement;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder> {
    private List<Achievement> achievements;
    private Context context;
    private Map<String, Integer> imageResourceMap = new HashMap<>();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String userID = auth.getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public AchievementAdapter(Context context, List<Achievement> achievements) {
        this.context = context;
        this.achievements = achievements;
        // setup image map
        imageResourceMap.put("1 Unlocked", R.drawable.achievement_1_unlocked);
        imageResourceMap.put("1 Locked", R.drawable.achievement_1_locked);
        imageResourceMap.put("2 Unlocked", R.drawable.achievement_2_unlocked);
        imageResourceMap.put("2 Locked", R.drawable.achievement_2_locked);
        imageResourceMap.put("3 Unlocked", R.drawable.achievement_3_unlocked);
        imageResourceMap.put("3 Locked", R.drawable.achievement_3_locked);
        imageResourceMap.put("4 Unlocked", R.drawable.achievement_4_unlocked);
        imageResourceMap.put("4 Locked", R.drawable.achievement_4_locked);
    }

    @NonNull
    @Override
    public AchievementAdapter.AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.achievement_item, parent, false);
        return new AchievementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementAdapter.AchievementViewHolder holder, int position) {
        Achievement currentAchievement = achievements.get(position);
        holder.bind(currentAchievement);
    }

    @Override
    public int getItemCount() {
        return achievements.size();
    }

    public class AchievementViewHolder extends RecyclerView.ViewHolder {
        private TextView achievementName;
        private TextView achievementDescription;
        private ProgressBar achievementProgressBar;
        private ImageView achievementImage;
        public AchievementViewHolder(@NonNull View itemView) {
            super(itemView);
            achievementName = itemView.findViewById(R.id.achievementTitleTextView);
            achievementDescription = itemView.findViewById(R.id.achievementDescriptionTextView);
            achievementProgressBar = itemView.findViewById(R.id.achievementProgressBar);
            achievementImage = itemView.findViewById(R.id.achievementBadgeImageView);

        }
        public void bind(Achievement achievement) {
            // bind achievement data to UI
            achievementName.setText(achievement.getName());
            achievementDescription.setText(achievement.getDescription());
            String achID = achievement.getAchievementID();
            int progress = achievement.getProgress();
            int criteria = achievement.getCriteria();
            String achName = achievement.getName();
            boolean unlocked = achievement.getUnlocked();
            achievementProgressBar.setMax(criteria);
            achievementProgressBar.setProgress(progress);
            if (unlocked) {
                achievementProgressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
                String imageID = achID + " " + "Unlocked";
                Log.d("Achievement imageID", imageID);
                int resourceId = imageResourceMap.get(imageID);
                achievementImage.setImageResource(resourceId);
            }
            else {
                String imageID = achID + " " + "Locked";
                Log.d("Achievement imageID", imageID);
                int resourceId = imageResourceMap.get(imageID);
                achievementImage.setImageResource(resourceId);
            }
            achievementImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleAchievementImageClick(achievement);
                }
            });
        }
        public void handleAchievementImageClick(Achievement achievement) {
            if (achievement.getUnlocked() == false) {
                Toast.makeText(context, "Can't Showcase Locked Achievement", Toast.LENGTH_SHORT).show();
            }
            else {
                // need to update the db to showcase achievement
                DocumentReference docRef = db.collection("users").document(userID);
                docRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                docRef.update("badge", achievement.getAchievementID());
                                Toast.makeText(context, "Achievement Showcased!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Achievement Adapter", "Could not get user doc for badge showcase update");
                            }
                        });

            }
        }
    }
}
