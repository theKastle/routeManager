package com.example.onthe.map.MapUtils;



/**
 * Created by onthe on 6/18/2017.
 */

public class Distance {
    public String text;
    public Double value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Distance(){}

    public Distance(String text, double value) {
        this.text = text;
        this.value = value;
    }
}
