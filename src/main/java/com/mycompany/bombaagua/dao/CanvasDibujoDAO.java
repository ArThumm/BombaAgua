/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bombaagua.dao;

//@author aoxle

import com.mycompany.bombaagua.interfaz.CanvasDibujoInterface;
import com.mycompany.bombaagua.dbConnection;
import com.mycompany.bombaagua.model.CanvasDibujo;
import com.mycompany.bombaagua.exceptions.DAOExceptions;
import java.sql.*;


public class CanvasDibujoDAO implements CanvasDibujoInterface {
    
    private static final String INSERT_SQL = "INSERT INTO canvasDibujo (canvas_ancho, canvas_altura) " +
                                "VALUES (?, ?)";
    private static final String DELETE_SQL = "DELETE FROM canvasDibujo WHERE dibujo_id = ?";
    
    @Override
    public void insert(CanvasDibujo dibujo) throws SQLException {
        

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL)) {

            pstmt.setInt(1, dibujo.getCanvasAncho());
            pstmt.setInt(2, dibujo.getCanvasAltura());

            pstmt.executeUpdate();
        }
    }
    
    @Override
    public boolean delete(int dibujoId) throws SQLException {
        
                
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {

            pstmt.setInt(1, dibujoId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DAOExceptions("Error al eliminar dibujo", e);
        }
    }
}