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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gymhomie.tools.Achievement;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder> {
    private final List<Achievement> achievements;
    private final Context context;
    private final Map<String, Integer> imageResourceMap = new HashMap<>();

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
        private final TextView achievementName;
        private final TextView achievementDescription;
        private final ProgressBar achievementProgressBar;
        private final ImageView achievementImage;
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

        }
    }
}
