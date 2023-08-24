package com.gymhomie.calculator;

import java.util.ArrayList;
//This will allow to see how much weight is on a barbell based on the plates loaded
public class BarbellCalculator {
    public ArrayList<WeightedPlate> getLoadedPlates() {
        return loadedPlates;
    }

    public void setLoadedPlates(ArrayList<WeightedPlate> loadedPlates) {
        this.loadedPlates = loadedPlates;
    }

    ArrayList<WeightedPlate> loadedPlates = new ArrayList<WeightedPlate>();
    double totalWeight;

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }




}
