package com.qetuop.spotu2000;

import java.io.Serializable;

/**
 * Created by brian on 10/11/15.
 */
public class Exercise implements Serializable {
    private long id = 0; // PK col id
    private String exerciseName = null;
    private String exerciseType = null;
    private long userId = -1;

    // for custom row view - can it be removed?
    boolean selected = false;

    public Exercise() {
    }

   // used when creating FROM the DB?
    public Exercise(long userId, String exerciseName, String exerciseType) {
        this.exerciseType = exerciseType;
        this.exerciseName = exerciseName;
        this.userId = userId;
    }

    // used when adding TO the DB?
    public Exercise(String exerciseName, String exerciseType, long userId) {
        this.exerciseName = exerciseName;
        this.exerciseType = exerciseType;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    // Will be used by the ArrayAdapter in the ListView
/*    @Override
    public String toString() {
        return exerciseName;
    }*/

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", Name='" + exerciseName + '\'' +
                ", Type='" + exerciseType + '\'' +
                ", UserID='" + userId + '\'' +
                '}';
    }


    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
