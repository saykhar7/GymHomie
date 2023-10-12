package com.gymhomie.tools;

public class Achievement {
    private int achievementID; // each achievement has a unique ID starting from 1
    private String name; // name for the achievement (some title to make the user proud)
    private String description; // description of the achievement (typically illustrates the criteria)
    private int criteria; // progress required to unlock the achievement (e.g. 3 liters of water)
    private int progress; // progress made by the user toward the criteria (e.g. 2 liters out of 3).
    private Boolean unlocked; // has the achievement been unlocked?
    // when progress >= criteria -> unlocked = True

    public Achievement(int achievementID, String name, String description, int criteria, int progress, Boolean unlocked) {
        this.achievementID = achievementID;
        this.name = name;
        this.description = description;
        this.criteria = criteria;
        this.progress = progress;
        this.unlocked = unlocked;
    }

    public int getAchievementID() {
        return achievementID;
    }

    public void setAchievementID(int achievementID) {
        this.achievementID = achievementID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCriteria() {
        return criteria;
    }

    public void setCriteria(int criteria) {
        this.criteria = criteria;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public Boolean getUnlocked() {
        return unlocked;
    }

    public void setUnlocked(Boolean unlocked) {
        this.unlocked = unlocked;
    }

    public boolean checkAchievementProgress() {
        if (progress >= criteria) {
            // achievement unlocked
            setUnlocked(true);
            return true;
        }
        else {
            return false;
        }
    }
}
