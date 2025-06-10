package com.mycompany.bombaagua.interfaz;

//@author aoxle

import java.sql.SQLException;
import com.mycompany.bombaagua.model.LineaDibujo;
import java.util.List;

public interface LineaDibujoInterface {
    void insert(LineaDibujo linea) throws SQLException;
    List<LineaDibujo> getAll() throws SQLException;
    void deleteAll() throws SQLException;
    void deleteLast() throws SQLException;
    List<LineaDibujo> getBySaveId(int saveId) throws SQLException;
    int getMaxSaveId() throws SQLException;
}
