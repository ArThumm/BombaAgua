package com.mycompany.bombaagua.interfaz;

//@author aoxle

import java.sql.SQLException;
import com.mycompany.bombaagua.model.CanvasDibujo;

public interface CanvasDibujoInterface {
    void insert(CanvasDibujo dibujo) throws SQLException;
    boolean delete(int dibujoId) throws SQLException;
}
