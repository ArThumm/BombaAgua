package com.mycompany.bombaagua.dao;

//@author aoxle

import com.mycompany.bombaagua.interfaz.LineaDibujoInterface;
import com.mycompany.bombaagua.dbConnection;
import com.mycompany.bombaagua.model.LineaDibujo;
import com.mycompany.bombaagua.exceptions.DAOExceptions;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LineaDibujoDAO implements LineaDibujoInterface {
    
    private static final String INSERT_SQL =        "INSERT INTO lineasDibujo (start_x, start_y, end_x, end_y, color_id, linea_ancho, save_id) " +
                                                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String GET_SQL_LOAD =      "SELECT * FROM lineasDibujo";
    private static final String DELETE_ALL_SQL =    "TRUNCATE TABLE lineasDibujo";
    private static final String GET_MAX_SAVE =      "SELECT MAX(save_id) FROM lineasDibujo";
    private static final String GET_BY_SAVE_ID =    "SELECT * FROM lineasDibujo WHERE save_id = ?";
   
    @Override
    public void insert (LineaDibujo linea) throws SQLException {
        
        try (Connection conn = dbConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL)) {

                pstmt.setDouble(1, linea.getStart_x());
                pstmt.setDouble(2, linea.getStart_y());
                pstmt.setDouble(3, linea.getEnd_x());
                pstmt.setDouble(4, linea.getEnd_y());
                pstmt.setInt(5, linea.getColor());
                pstmt.setDouble(6, linea.getWidth());
                pstmt.setInt(7, linea.getSave_id());
                pstmt.executeUpdate();
                
        } catch (SQLException e) {
            throw new DAOExceptions("Error al insertar linea", e);
        }
    }
    
    @Override
    public List<LineaDibujo> getBySaveId(int saveId) throws SQLException {
        List<LineaDibujo> lineas = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_BY_SAVE_ID)) {

            pstmt.setInt(1, saveId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    LineaDibujo linea = new LineaDibujo();
                    linea.setLinea_id(rs.getInt("linea_id"));
                    linea.setStart_x(rs.getDouble("start_x"));
                    linea.setStart_y(rs.getDouble("start_y"));
                    linea.setEnd_x(rs.getDouble("end_x"));
                    linea.setEnd_y(rs.getDouble("end_y"));
                    linea.setColor(rs.getInt("color_id"));
                    linea.setWidth(rs.getDouble("linea_ancho"));
                    linea.setSave_id(rs.getInt("save_id"));
                    lineas.add(linea);
                }
            }
        } catch (SQLException e) {
            // Add more specific error information
            String errorMsg = String.format("Error loading lines for save_id %d: %s", saveId, e.getMessage());
            throw new DAOExceptions(errorMsg, e);
        }
        return lineas;
    }
    
    @Override
    public int getMaxSaveId() throws SQLException {
        try (Connection conn = dbConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(GET_MAX_SAVE);
            ResultSet rs = pstmt.executeQuery())
        {
            if (rs.next()) {
                int maxId = rs.getInt(1);
                return rs.wasNull() ? 0 : maxId;
            }
            return 0;
            
        } catch (SQLException e) {
            throw new DAOExceptions("Error loading all lines", e);
        }
    }
    
    @Override
    public List<LineaDibujo> getAll() throws SQLException {
        List<LineaDibujo> lineas = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_SQL_LOAD);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                LineaDibujo linea = new LineaDibujo();
                linea.setLinea_id(rs.getInt("linea_id"));
                linea.setStart_x(rs.getDouble("start_x"));
                linea.setStart_y(rs.getDouble("start_y"));
                linea.setEnd_x(rs.getDouble("end_x"));
                linea.setEnd_y(rs.getDouble("end_y"));
                linea.setColor(rs.getInt("color_id"));
                linea.setWidth(rs.getDouble("linea_ancho"));
                linea.setSave_id(rs.getInt("save_id"));
                lineas.add(linea);
            }
        } catch (SQLException e) {
            throw new DAOExceptions("Error loading all lines", e);
        }
        return lineas;
    }
    
    @Override
    public void deleteAll() throws SQLException {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_ALL_SQL)) {

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOExceptions("Error deleting all lines", e);
        }
    }
    
    @Override
    public void deleteLast() throws SQLException {
    try (Connection conn = dbConnection.getConnection()) {
        int maxId;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT MAX(linea_id) FROM lineasDibujo")) {
            rs.next();
            maxId = rs.getInt(1);
        }

        try (PreparedStatement pstmt = conn.prepareStatement(
                "DELETE FROM lineasDibujo WHERE linea_id = ?")) {
            pstmt.setInt(1, maxId);
            int rowsDeleted = pstmt.executeUpdate();
            
            if (rowsDeleted == 0) {
                throw new DAOExceptions("No lines found to delete");
            }
        }
    }
}
    
    /*@Override
    public int getNextSaveId() throws SQLException {
    String sql = "SELECT MAX(save_id) + 1 AS next_id FROM lineasDibujo";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("next_id") == 0 ? 1 : rs.getInt("next_id");
            }
            return 1; // default if no saves exist
        }
    }*/
    
    /*
    @Override
    public List<ColorRGB> getColor(int id_color) throws SQLException {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_ALL_SQL)) {
            
            
            pstmt.setDouble(1, linea.getStart_x());
            pstmt.setDouble(2, linea.getStart_x());
            pstmt.setDouble(3, linea.getStart_x());
        }
    }*/
}
