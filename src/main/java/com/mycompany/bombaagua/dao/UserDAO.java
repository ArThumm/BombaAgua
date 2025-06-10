package com.mycompany.bombaagua.dao;

import com.mycompany.bombaagua.interfaz.UserInterface;
import com.mycompany.bombaagua.dbConnection;
import com.mycompany.bombaagua.exceptions.DAOExceptions;
import com.mycompany.bombaagua.model.User;
import java.sql.*;
import java.util.*;

public class UserDAO implements UserInterface {
    
    private static final String GET_USER = "SELECT * FROM bombaUser";
    private static final String INSERT_USER = "INSERT INTO bombauser (user_name, user_email, user_password) " +
                                             "VALUES (?, ?, ?)";
    
    @Override
    public List<User> getUser() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(GET_USER);
            ResultSet rs = pstmt.executeQuery()) {
        
            while (rs.next()) {
                User user = new User();
                user.setUser_name(rs.getString("user_name"));
                user.setUser_email(rs.getString("user_email"));
                user.setUser_password(rs.getString("user_password"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new SQLException("Error getting user", e);
        }
        return users;
    }
    
    @Override
    public void addUser(User user) throws SQLException {
        try (Connection conn = dbConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(INSERT_USER)) {

            pstmt.setString(1, user.getUser_name());
            pstmt.setString(2, user.getUser_email());
            pstmt.setString(3, user.getUser_password());
            pstmt.executeUpdate();
                
        } catch (SQLException e) {
            throw new DAOExceptions("Error adding user", e);
        }
    }
}