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

import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder> {
    private List<Achievement> achievements;
    private Context context;
    public AchievementAdapter(Context context, List<Achievement> achievements) {
        this.context = context;
        this.achievements = achievements;
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
            int progress = achievement.getProgress();
            int criteria = achievement.getCriteria();
            boolean unlocked = achievement.getUnlocked();
            achievementProgressBar.setMax(criteria);
            achievementProgressBar.setProgress(progress);
            if (unlocked) {
                achievementProgressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
                achievementImage.setImageResource(R.drawable.trophy_unlocked);
            }

        }
    }
}
