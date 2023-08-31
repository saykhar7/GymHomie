/**
 *     The BarbellCalculator class will allow users
 *     to view how much weight(LBs) is on the barbell(bar)
 *     by inputting what weights are one side through the GUI buttons
 *
 */

package com.gymhomie.calculator;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BarbellCalculator {
    ArrayList<WeightedPlate> loadedPlates = new ArrayList<>(); //Keep a list of the plates WeightedPlates on the bar

    /**
     *     The calculateLoadedWeight method will take in a list
     *     and iterate through that list and calculate the total weight
     */
    public double calculateLoadedWeight(){
        double currentSum = 0; //default weight
        double barbellWeight = 45; //Most barbells are 20 kilos so about 44 lbs but 45 is easier to work with
        currentSum += barbellWeight; // Adding the barbell weight to current sum


        for (int i = 0; i < loadedPlates.size() && i < 50; i++){ //50 is to cap total possible weight
            currentSum += loadedPlates.get(i).getValue();
        }
        return currentSum;
    }

    /**
     *     Setter and Getter for loadedPlates
     */
    public ArrayList<WeightedPlate> getLoadedPlates() {
        return loadedPlates;
    }
    public void setLoadedPlates(ArrayList<WeightedPlate> loadedPlates) {
        this.loadedPlates = loadedPlates;
    }
}