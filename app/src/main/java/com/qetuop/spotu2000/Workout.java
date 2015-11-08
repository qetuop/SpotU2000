package com.qetuop.spotu2000;

import java.io.Serializable;

/**
 * Created by brian on 10/18/15.
 */
public class Workout implements Serializable {
    private long id = 0; // PK col id
    private long date = 0;
    private long exercise_id = 0;
    private int sets;
    private String reps;
    private String weight;

    public Workout() {
    }

    public Workout(long date, long exercise_id) {
        this.date = date;
        this.exercise_id = exercise_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getExercise_id() {
        return exercise_id;
    }

    public void setExercise_id(long exercise_id) {
        this.exercise_id = exercise_id;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Workout{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", exercise_id='" + exercise_id + '\'' +
                ", sets='" + sets + '\'' +
                ", reps='" + reps + '\'' +
                ", weight='" + weight + '\'' +
                '}';
    }

}
