/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bombaagua.model;

/**
 *
 * @author aoxle
 */
public class ColorLinea {
    private int color_id;
    private double color_red;
    private double color_green;
    private double color_blue;

    public ColorLinea() {
    }

    public ColorLinea(int color_id, double color_red, double color_green, double color_blue) {
        this.color_id = color_id;
        this.color_red = color_red;
        this.color_green = color_green;
        this.color_blue = color_blue;
    }

    public int getColor_id() {
        return color_id;
    }

    public double getColor_red() {
        return color_red;
    }

    public double getColor_green() {
        return color_green;
    }

    public double getColor_blue() {
        return color_blue;
    }

    public void setColor_id(int color_id) {
        this.color_id = color_id;
    }

    public void setColor_red(double color_red) {
        this.color_red = color_red;
    }

    public void setColor_green(double color_green) {
        this.color_green = color_green;
    }

    public void setColor_blue(double color_blue) {
        this.color_blue = color_blue;
    }

    @Override
    public String toString() {
        return "ColorLinea{" 
                + "color_id=" + color_id 
                + ", color_red=" + color_red 
                + ", color_green=" + color_green 
                + ", color_blue=" + color_blue 
                + '}';
    }
    
    
}
