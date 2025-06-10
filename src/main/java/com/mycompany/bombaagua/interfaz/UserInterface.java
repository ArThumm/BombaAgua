package com.mycompany.bombaagua.interfaz;

import com.mycompany.bombaagua.model.User;
import java.sql.SQLException;
import java.util.List;

public interface UserInterface {
    void addUser(User user) throws SQLException;
    List<User> getUser() throws SQLException;
}