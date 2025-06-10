package com.mycompany.bombaagua.interfaz;

import com.mycompany.bombaagua.model.ColorLinea;
import java.sql.SQLException;

//@author aoxle

public interface ColorLineaInterface {
    ColorLinea getRGBColor(int colorId) throws SQLException;
}
