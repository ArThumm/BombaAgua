module com.mycompany.bombaagua {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.mycompany.bombaagua to javafx.fxml;
    exports com.mycompany.bombaagua;
    
    opens com.mycompany.bombaagua.controller to javafx.fxml;
    exports com.mycompany.bombaagua.controller;
}
