package com.gymhomie.workouts;

public class exercise {

    String name;
    int num_sets;
    int time;
    int num_reps;
    int weight;

    public exercise(String name, int num_sets, int num_reps, int weight)
    {
        this.name = name;
        this.num_sets = num_sets;
        this.num_reps = num_reps;
        this.weight = weight;
    }

    public exercise(String name, int num_sets, int time, int num_reps, int weight)
    {
        this.name = name;
        this.num_sets = 0;
        this.time = 0;
        this.num_reps = 0;
        this.weight = 0;
    }

    //gets name of the exercise
    public String get_name() {return name;}

    //gets number of sets for the exercise
    public int get_sets() {return num_sets;}

    //gets amount of time for the exercise
    public int get_time() {return time;}

    //gets number of reps in one set
    public int get_reps() {return num_reps;}

    //gets amount of weight for each set
    public int get_weight() {return weight;}


    //sets name of the exercise
    public void set_name() {this.name = name;}

    //sets number of sets for the exercise
    public void set_sets() {this.num_sets = num_sets;}

    //sets amount of time for the exercise
    public void set_time() {this.time = time;}

    //sets number of reps in one set
    public void set_reps() {this.num_reps = num_reps;}

    //sets amount of weight for each set
    public void set_weight() {this.weight = weight;}





}
