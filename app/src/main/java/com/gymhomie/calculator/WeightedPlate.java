package com.gymhomie.calculator;

public class WeightedPlate {

    //how much does the plate work
    double value;



    //how many plates
    int quantity ;

    //The weight of the plates are measures in kilo's to simplify conversion
    public WeightedPlate(double value) {
        this.value = value;
        this.quantity = 0;
    }
    public WeightedPlate(double value, int quantity) {
        this.value = value;
        this.quantity = quantity;
    }
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

}