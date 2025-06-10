package com.mycompany.bombaagua.model;

//@author aoxle

public class LineaDibujo {
    private int linea_id;
    private double start_x;
    private double start_y;
    private double end_x;
    private double end_y;
    private int color;
    private double width;
    private int save_id;

    public LineaDibujo() {
    }

    public LineaDibujo(int linea_id, double start_x, double start_y, double end_x, double end_y, int color, double width, int save_id) {
        this.linea_id = linea_id;
        this.start_x = start_x;
        this.start_y = start_y;
        this.end_x = end_x;
        this.end_y = end_y;
        this.color = color;
        this.width = width;
        this.save_id = save_id;
    }

    public int getLinea_id() {
        return linea_id;
    }

    public double getStart_x() {
        return start_x;
    }

    public double getStart_y() {
        return start_y;
    }

    public double getEnd_x() {
        return end_x;
    }

    public double getEnd_y() {
        return end_y;
    }

    public int getColor() {
        return color;
    }

    public double getWidth() {
        return width;
    }

    public int getSave_id() {
        return save_id;
    }

    public void setLinea_id(int linea_id) {
        this.linea_id = linea_id;
    }

    public void setStart_x(double start_x) {
        this.start_x = start_x;
    }

    public void setStart_y(double start_y) {
        this.start_y = start_y;
    }

    public void setEnd_x(double end_x) {
        this.end_x = end_x;
    }

    public void setEnd_y(double end_y) {
        this.end_y = end_y;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setSave_id(int save_id) {
        this.save_id = save_id;
    }

    @Override
    public String toString() {
        return "LineaDibujo{" 
                + "linea_id=" + linea_id 
                + ", start_x=" + start_x 
                + ", start_y=" + start_y 
                + ", end_x=" + end_x 
                + ", end_y=" + end_y 
                + ", color=" + color 
                + ", width=" + width 
                + ", save_id=" + save_id + '}';
    }

    
    
}
