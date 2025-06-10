package com.mycompany.bombaagua.dao;

import com.mycompany.bombaagua.dbConnection;
import com.mycompany.bombaagua.exceptions.DAOExceptions;
import com.mycompany.bombaagua.interfaz.ColorLineaInterface;
import com.mycompany.bombaagua.model.ColorLinea;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//@author aoxle

public class ColorLineaDAO implements ColorLineaInterface {
    //private static final String GET_COLOR = "SELECT * FROM coloresLinea WHERE color_id = ?";

    @Override
    public ColorLinea getRGBColor(int colorId) throws SQLException {
    // Define the query explicitly to ensure it matches your schema
    final String GET_COLOR = "SELECT color_id, color_red, color_green, color_blue " +
                            "FROM coloresLinea WHERE color_id = ?";
    
    try (Connection conn = dbConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(GET_COLOR)) {
        
        pstmt.setInt(1, colorId);
        
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                ColorLinea color = new ColorLinea();
                // Verify column names match your database exactly
                color.setColor_id(rs.getInt("color_id"));
                color.setColor_red(rs.getDouble("color_red"));
                color.setColor_green(rs.getDouble("color_green"));
                color.setColor_blue(rs.getDouble("color_blue"));
                
                return color;
            } else {
                // More descriptive error message
                throw new SQLException("Color ID " + colorId + " not found in coloresLinea table. ");
            }
        }
    } catch (SQLException e) {
        throw new DAOExceptions("Error retrieving color with ID: " + colorId, e);
    }
} 
}
