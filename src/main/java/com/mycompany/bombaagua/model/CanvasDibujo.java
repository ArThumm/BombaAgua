package com.mycompany.bombaagua.model;

//@author aoxle

import java.sql.Timestamp; 

public class CanvasDibujo {
    private int dibujo_id;
    private int canvas_ancho;
    private int canvas_altura;
    private Timestamp creado_en;
    
    public CanvasDibujo() {    
    }
    
    public CanvasDibujo(int dibujo_id, int canvas_ancho, int canvas_altura, Timestamp creado_en) {
        this.dibujo_id = dibujo_id;
        this.canvas_ancho = canvas_ancho;
        this.canvas_altura = canvas_altura;
        this.creado_en = creado_en;
    }
    
    public int getDibujoId() {
        return dibujo_id;
    }
    
    public int getCanvasAncho() {
        return canvas_ancho;
    }
    
    public int getCanvasAltura() {
        return canvas_altura;
    }
    
    public Timestamp getCreadoEn() {
        return creado_en;
    }

    public void setCanvas_ancho(int canvas_ancho) {
        this.canvas_ancho = canvas_ancho;
    }

    public void setCanvas_altura(int canvas_altura) {
        this.canvas_altura = canvas_altura;
    }

    @Override
    public String toString() {
        return "CanvasDibujo{" 
                + "dibujo_id=" + dibujo_id 
                + ", canvas_ancho=" + canvas_ancho 
                + ", canvas_altura=" + canvas_altura 
                + ", creado_en=" + creado_en 
                + '}';
    }
    
    
    
}
