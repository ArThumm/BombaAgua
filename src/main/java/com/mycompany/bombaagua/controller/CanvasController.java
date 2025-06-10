package com.mycompany.bombaagua.controller;

import com.mycompany.bombaagua.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import java.sql.SQLException;
import com.mycompany.bombaagua.model.LineaDibujo;
import com.mycompany.bombaagua.dao.LineaDibujoDAO;
import com.mycompany.bombaagua.model.ColorLinea;
import com.mycompany.bombaagua.dao.ColorLineaDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CanvasController {
    @FXML
    private Canvas designCanvas;
    
    public void initialize() {
        isFirstClick = true;
    }
    
    private int saveId = 0;
    private double startX, startY, snapX, snapY;
    private boolean isFirstClick = true;
    private boolean isCanvasClickEnabled = false;
    private boolean snap = false;
    private boolean empty = true;
    private String[] drawModes = {"Default", "Straight", "Square"};
    private String[] drawSnaps = {"Default", "Snap"};
    private List<List<Double>> listaLineasEnCanvas = new ArrayList<>();
    
    @FXML
    private HBox drawButtonsBox;
    @FXML
    private HBox deleteButtonsBox;
    
    @FXML
    private Button switchClickDraw;
    @FXML
    private Button switchClickDrawMode;
    @FXML
    private Button switchClickDrawSnap;
    @FXML
    private Button buttonClickDrawClear;
    
    @FXML
    private Button buttonClickSave;
    @FXML
    private Button buttonClickLoad;
    
    @FXML
    private Button switchClickDelete;
    @FXML
    private Button buttonClickDeleteLast;
    @FXML
    private Button buttonClickDeleteAll;
    
    @FXML
    private void handleToggleClickDraw(ActionEvent event) {
        drawButtonsBox.setVisible(!drawButtonsBox.isVisible());
        drawButtonsBox.setManaged(!drawButtonsBox.isManaged());
        isCanvasClickEnabled = !isCanvasClickEnabled;
        System.out.println("Canvas click: " + (isCanvasClickEnabled ? "ENABLED" : "DISABLED"));
        switchClickDraw.setText(isCanvasClickEnabled ? "Disable" : "Enable");
    }
    
    @FXML
    private void handleToggleClickDelete(ActionEvent event) {
        deleteButtonsBox.setVisible(!deleteButtonsBox.isVisible());
        deleteButtonsBox.setManaged(!deleteButtonsBox.isManaged());
        isCanvasClickEnabled = !isCanvasClickEnabled;
        System.out.println("Delete click: " + (isCanvasClickEnabled ? "ENABLED" : "DISABLED"));
    }
    
    @FXML
    private void handleToggleClickDrawMode(ActionEvent event) {
        if (!isCanvasClickEnabled) return;
        
        String currentMode = switchClickDrawMode.getText();
        String newMode;

        if (drawModes[0].equals(currentMode)) {
            newMode = drawModes[1];
        } else if (drawModes[1].equals(currentMode)) {
            newMode = drawModes[2];
        } else {
            newMode = drawModes[0];
        }

        switchClickDrawMode.setText(newMode);
    }
    
    @FXML
    private void handleToggleClickDrawSnap(ActionEvent event) {
        if (!isCanvasClickEnabled) return;
        
        switchClickDrawSnap.setText(
            drawSnaps[0].equals(switchClickDrawSnap.getText()) ? drawSnaps[1] : drawSnaps[0]
        );
    }
    
    @FXML
    private void handleToggleClickDrawClear() {
        GraphicsContext gc = designCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, designCanvas.getWidth(), designCanvas.getHeight());
        empty = true;
        isFirstClick = true;
        snapX = 0;
        snapY = 0;
        startX = 0;
        startY = 0;
    }
    
    @FXML
    private void handleToggleClickSave() throws SQLException {
        if (empty == false) {
            
            LineaDibujoDAO dao = new LineaDibujoDAO();
            saveId = dao.getMaxSaveId() + 1;
            System.out.println(saveId);
            System.out.println(dao.getMaxSaveId());
            
            for (int i = 0; i < listaLineasEnCanvas.size(); i++) {
                List<Double> line = listaLineasEnCanvas.get(i);
                LineaDibujo linea = new LineaDibujo (
                    1,
                    line.get(0),
                    line.get(1),
                    line.get(2),
                    line.get(3),
                    (int) Math.floor(line.get(4)),
                    line.get(5),
                    saveId
                );
                System.out.println("LineaDibujo{" 
                + "linea_id=" + linea.getLinea_id()
                + ", start_x=" + linea.getStart_x()
                + ", start_y=" + linea.getStart_y()
                + ", end_x=" + linea.getEnd_x()
                + ", end_y=" + linea.getEnd_y()
                + ", color=" + linea.getColor()
                + ", width=" + linea.getWidth()
                + ", save_id=" + linea.getSave_id() + '}');
                try {
                    dao.insert(linea);
                    System.out.println("CanvasDibujo insertado correctamente");
                } catch (SQLException e) {
                    System.err.println("Error insertando CanvasDibujo: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            listaLineasEnCanvas.clear();
        }
    }
    
    
    
    @FXML
    private void handleToggleClickLoad() {
        
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Load Drawing");
        dialog.setHeaderText("Enter Save ID to load");
        dialog.setContentText("Save ID:");
        
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int saveId = Integer.parseInt(result.get());
                loadDrawingBySaveId(saveId);
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a valid number for Save ID");
            }
        }
        
        /*GraphicsContext gc = designCanvas.getGraphicsContext2D();
        LineaDibujoDAO dao = new LineaDibujoDAO();

        if (empty == true) {
            try {
                gc.clearRect(0, 0, designCanvas.getWidth(), designCanvas.getHeight());
                List<LineaDibujo> allLines = dao.getAll();
                for (LineaDibujo linea : allLines) {
                    ColorLineaDAO colorDAO = new ColorLineaDAO();
                    ColorLinea colorDB = colorDAO.getRGBColor(linea.getColor());
                    colorDB = colorDAO.getRGBColor(linea.getColor());
                    Color RGB = new Color(colorDB.getColor_red(), colorDB.getColor_green(), colorDB.getColor_blue(), 1);
                    gc.setStroke(RGB); 
                    gc.setLineWidth(linea.getWidth());
                    
                    switch (linea.getColor()) {
                        case 1:
                            gc.strokeLine(linea.getStart_x(),linea.getStart_y(),linea.getStart_x(),linea.getEnd_y());
                            gc.strokeLine(linea.getStart_x(),linea.getStart_y(),linea.getEnd_x(),linea.getStart_y());
                            gc.strokeLine(linea.getEnd_x(),linea.getEnd_y(),linea.getEnd_x(),linea.getStart_y());
                            gc.strokeLine(linea.getEnd_x(),linea.getEnd_y(),linea.getStart_x(),linea.getEnd_y());
                            break;
                        case 2:
                            gc.strokeLine(linea.getStart_x(), linea.getStart_y(), linea.getEnd_x(), linea.getEnd_y());
                            break;
                        case 3:
                            gc.strokeLine(linea.getStart_x(), linea.getStart_y(), linea.getEnd_x(), linea.getEnd_y());
                            break;
                    }
                    
                    System.out.println("LineaDibujo{" 
                    + "linea_id=" + linea.getLinea_id()
                    + ", start_x=" + linea.getStart_x()
                    + ", start_y=" + linea.getStart_y()
                    + ", end_x=" + linea.getEnd_x()
                    + ", end_y=" + linea.getEnd_y()
                    + ", color=" + linea.getColor()
                    + ", width=" + linea.getWidth()
                    + ", save_id=" + linea.getSave_id() + '}');
                }
            } catch (SQLException e) {
                System.err.println("Error loading lines: " + e.getMessage());
                e.printStackTrace();
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Loading Error");
                    alert.setHeaderText("Failed to load drawings");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                });
            }
        } else {}*/
    }
    
    @FXML
    private void loadDrawingBySaveId(int saveId) {
        GraphicsContext gc = designCanvas.getGraphicsContext2D();
        LineaDibujoDAO dao = new LineaDibujoDAO();

        try {
            gc.clearRect(0, 0, designCanvas.getWidth(), designCanvas.getHeight());
            List<LineaDibujo> lines = dao.getBySaveId(saveId);

            if (lines.isEmpty()) {
                showAlert("No Data", "No drawing found with Save ID: " + saveId);
                return;
            }

            for (LineaDibujo linea : lines) {
                ColorLineaDAO colorDAO = new ColorLineaDAO();
                ColorLinea colorDB = colorDAO.getRGBColor(linea.getColor());
                Color RGB = new Color(colorDB.getColor_red(), colorDB.getColor_green(), colorDB.getColor_blue(), 1);
                gc.setStroke(RGB); 
                gc.setLineWidth(linea.getWidth());

                switch (linea.getColor()) {
                    case 1: // Square
                        gc.strokeLine(linea.getStart_x(),linea.getStart_y(),linea.getStart_x(),linea.getEnd_y());
                        gc.strokeLine(linea.getStart_x(),linea.getStart_y(),linea.getEnd_x(),linea.getStart_y());
                        gc.strokeLine(linea.getEnd_x(),linea.getEnd_y(),linea.getEnd_x(),linea.getStart_y());
                        gc.strokeLine(linea.getEnd_x(),linea.getEnd_y(),linea.getStart_x(),linea.getEnd_y());
                        break;
                    case 2: // Straight line
                    case 3: // Default line
                        gc.strokeLine(linea.getStart_x(), linea.getStart_y(), linea.getEnd_x(), linea.getEnd_y());
                        break;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading lines: " + e.getMessage());
            e.printStackTrace();
            showAlert("Loading Error", "Failed to load drawing: " + e.getMessage());
        }
    }
    
    @FXML
    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
    
    @FXML
    private void handleToggleClickDeleteAll() {
        LineaDibujoDAO dao = new LineaDibujoDAO();
        try {
            dao.deleteAll();
            handleToggleClickLoad();
        } catch (SQLException e) {
            System.out.println("handleToggleClickDeleteAll -> ERROR");
        }
    }
    
    @FXML
    private void handleToggleClickDeleteLast() {
        LineaDibujoDAO dao = new LineaDibujoDAO();
        try {
            dao.deleteLast();
            handleToggleClickLoad();
        } catch (SQLException e) {
            System.out.println("handleToggleClickDeleteLast -> ERROR");
        }
    }
    
    @FXML
    private void handleCanvasStoreLine(GraphicsContext gc, double startX, double startY, 
                             double endX, double endY, double color_id, double width) {
        try {
            ColorLineaDAO colorDAO = new ColorLineaDAO();
            ColorLinea colorDB = new ColorLinea();
            colorDB = colorDAO.getRGBColor((int)color_id);
            Color RGB = new Color(colorDB.getColor_red(), colorDB.getColor_green(), colorDB.getColor_blue(), 1);
            
            switch ((int)color_id) {
                case 1:
                    gc.setStroke(RGB);
                    gc.setLineWidth(width);
                    gc.strokeLine((int) startX, (int) startY, (int) endX, (int) startY);   
                    gc.strokeLine((int) endX, (int) startY, (int) endX, (int) endY);       
                    gc.strokeLine((int) endX, (int) endY, (int) startX, (int) endY);       
                    gc.strokeLine((int) startX, (int) endY, (int) startX, (int) startY);   
                    System.out.println("Case is: " + 1);
                    break;
                case 2:
                    gc.setStroke(RGB);
                    gc.setLineWidth(width);
                    gc.strokeLine((int)startX, (int)startY, (int)endX, (int)endY);
                    System.out.println("Case is: " + 2);
                    break;
                case 3:
                    gc.setStroke(RGB);
                    gc.setLineWidth(width);
                    gc.strokeLine((int)startX, (int)startY, (int)endX, (int)endY);
                    System.out.println("Case is: " + 3);
                    break;
            }
            
            List<Double> lineasEnCanvas = new ArrayList<>();
            lineasEnCanvas.add(startX);
            lineasEnCanvas.add(startY);
            lineasEnCanvas.add(endX);
            lineasEnCanvas.add(endY);
            lineasEnCanvas.add(color_id);
            lineasEnCanvas.add(width);
            listaLineasEnCanvas.add(lineasEnCanvas);
            System.out.println("ColorID es:" + lineasEnCanvas.get(4));
        } catch (SQLException e) {
            System.err.println("Error guardaando Linea: " + e.getMessage());
            e.printStackTrace();
        } 
    }
    
    @FXML
    private void handleCanvasClick(MouseEvent event) throws SQLException {
        if (!isCanvasClickEnabled) return;
        
        if (isFirstClick) {
            empty = false;
            startX = event.getX();
            startY = event.getY();
            isFirstClick = false;
            
            if (drawSnaps[1].equals(switchClickDrawSnap.getText())) {
                if (Double.compare(snapX, 0) == 0 && Double.compare(snapY, 0) == 0) {
                }
                else {
                    snap = false;
                    startX = snapX;
                    startY = snapY;
                }
            }
        } 
        else {
            empty = false;
            double endX = event.getX();
            double endY = event.getY();
            snapX = endX;
            snapY = endY;
            double deltaX = Math.abs(endX - startX);
            double deltaY = Math.abs(endY - startY);
            
            GraphicsContext gc = designCanvas.getGraphicsContext2D();
            
            if (drawModes[0].equals(switchClickDrawMode.getText())) {
                double color_id = 3.0;
                double width = 2.0;
                handleCanvasStoreLine(gc, startX, startY, 
                             endX, endY, color_id, width);
            }
            if (drawModes[1].equals(switchClickDrawMode.getText())) {
                double color_id = 2.0;
                double width = 2.0;
                if (deltaX > deltaY){
                    handleCanvasStoreLine(gc, startX, startY, 
                             endX, startY, color_id, width);
                    snapX = endX;
                    snapY = startY;
                }
                else{
                    handleCanvasStoreLine(gc, startX, startY, 
                             startX, endY, color_id, width);
                    snapX = startX;
                    snapY = endY;
                }
            }
            if (drawModes[2].equals(switchClickDrawMode.getText())) {
                double color_id = 1.0;
                double width = 2.0; 
                handleCanvasStoreLine(gc, startX, startY, 
                             endX, endY, color_id, width);
            }
            double dx = endX - startX;
            double dy = endY - startY;
            double length = Math.sqrt(dx * dx + dy * dy);
            System.out.println("Length is: " + length);
            System.out.println("startX: " + (int)startX);
            System.out.println("startY: " + (int)startY);
            System.out.println("endX: " + (int)endX);
            System.out.println("endY: " + (int)endY);
            System.out.println("deltaX: " + (int)deltaX);
            System.out.println("deltaY: " + (int)deltaY);
            System.out.println("HashColor: " + (double)Color.GREEN.hashCode());
            System.out.println("Lista General: " + listaLineasEnCanvas);
            
            isFirstClick = true;
        }
    }
}

/*package com.mycompany.bombaagua.controller;
//@author aoxle

import com.mycompany.bombaagua.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import java.sql.SQLException;
import com.mycompany.bombaagua.model.LineaDibujo;
import com.mycompany.bombaagua.dao.LineaDibujoDAO;
import com.mycompany.bombaagua.model.ColorLinea;
import com.mycompany.bombaagua.dao.ColorLineaDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CanvasController {
    @FXML
    private Canvas designCanvas;
    
    public void initialize() {
        isFirstClick = true;
    }
    
    private double startX, startY, snapX, snapY;  // Stores the first click coordinates
    private boolean isFirstClick = true;  // Tracks whether we're waiting for the second click
    private boolean isCanvasClickEnabled = false;
    private boolean snap = false;
    private boolean empty = true;
    private String[] drawModes = {"Default", "Straight", "Square"};
    private String[] drawSnaps = {"Default", "Snap"};
    private List<List<Double>> listaLineasEnCanvas = new ArrayList<>();
    
    @FXML
    private HBox drawButtonsBox;
    @FXML
    private HBox deleteButtonsBox;
    
    @FXML
    private Button switchClickDraw;
    @FXML
    private Button switchClickDrawMode;
    @FXML
    private Button switchClickDrawSnap;
    @FXML
    private Button buttonClickDrawClear;
    
    @FXML
    private Button buttonClickSave;
    @FXML
    private Button buttonClickLoad;
    
    @FXML
    private Button switchClickDelete;
    @FXML
    private Button buttonClickDeleteLast;
    @FXML
    private Button buttonClickDeleteAll;
    
    //@FXML
    //private Button buttonClickLogin;

    
    /*@FXML
    private void handleToggleClickLogin(ActionEvent event) throws IOException {
        // 1. Load the FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/bombaagua/login.fxml"));
        Parent root = loader.load();

        // 2. Create a new Stage (window)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Dialog Window");
        dialogStage.setScene(new Scene(root, 300, 200));

        // 3. (Optional) Configure modality
        dialogStage.initModality(Modality.WINDOW_MODAL); // Blocks parent window
        dialogStage.initOwner(((Node) event.getSource()).getScene().getWindow()); // Sets parent

        // 4. Show the new window
        dialogStage.show();
    }*/
    
    /*@FXML
    private void handleToggleClickDraw(ActionEvent event) {
        drawButtonsBox.setVisible(!drawButtonsBox.isVisible());
        drawButtonsBox.setManaged(!drawButtonsBox.isManaged());
        isCanvasClickEnabled = !isCanvasClickEnabled;
        System.out.println("Canvas click: " + (isCanvasClickEnabled ? "ENABLED" : "DISABLED"));
        switchClickDraw.setText(isCanvasClickEnabled ? "Disable" : "Enable");
        //System.out.println(switchClickDraw.getText());
        //switchClickDrawMode.setVisible(!switchClickDraw.getText().equals("Enable"));
        //switchClickDrawSnap.setVisible(!switchClickDraw.getText().equals("Enable"));
    }
    
    @FXML
    private void handleToggleClickDelete(ActionEvent event) {
        deleteButtonsBox.setVisible(!deleteButtonsBox.isVisible());
        deleteButtonsBox.setManaged(!deleteButtonsBox.isManaged());
        isCanvasClickEnabled = !isCanvasClickEnabled;
        System.out.println("Delete click: " + (isCanvasClickEnabled ? "ENABLED" : "DISABLED"));
        switchClickDelete.setText(isCanvasClickEnabled ? "Disable" : "Enable");
        //System.out.println(switchClickDelete.getText());
        //buttonClickDeleteLast.setVisible(!switchClickDelete.getText().equals("Enable"));
        //buttonClickDeleteAll.setVisible(!switchClickDelete.getText().equals("Enable"));
    }
    
    @FXML
    private void handleToggleClickDrawMode(ActionEvent event) {
        if (!isCanvasClickEnabled) return;
        /*switchClickDrawMode.setText(
            drawModes[0].equals(switchClickDrawMode.getText()) ? drawModes[1] : drawModes[0]
        );
        
        String currentMode = switchClickDrawMode.getText();
        String newMode;

        if (drawModes[0].equals(currentMode)) {
            newMode = drawModes[1];  // Red → Blue
        } else if (drawModes[1].equals(currentMode)) {
            newMode = drawModes[2];  // Blue → Green
        } else {
            newMode = drawModes[0];  // Green → Red (or any other case)
        }

        switchClickDrawMode.setText(newMode);
    }
    
    @FXML
    private void handleToggleClickDrawSnap(ActionEvent event) {
        if (!isCanvasClickEnabled) return;
        
        switchClickDrawSnap.setText(
            drawSnaps[0].equals(switchClickDrawSnap.getText()) ? drawSnaps[1] : drawSnaps[0]
        );
    }
    
    @FXML
    private void handleToggleClickDrawClear() {
        GraphicsContext gc = designCanvas.getGraphicsContext2D();

        // Clear the entire canvas
        gc.clearRect(0, 0, designCanvas.getWidth(), designCanvas.getHeight());

        // Also reset any tracking variables if needed
        empty = true;
        isFirstClick = true;
        snapX = 0;
        snapY = 0;
        startX = 0;
        startY = 0;
    }
    
    @FXML
    private void handleToggleClickSave() throws SQLException {
        
        if (empty == false) {
            LineaDibujoDAO dao = new LineaDibujoDAO();
            int saveId;
            for (int i = 0; i < listaLineasEnCanvas.size(); i++) {
                if (i == 0) {
                    saveId = dao.getMaxSaveId() + 1;
                } else {
                    saveId = dao.getMaxSaveId();
                }
                List<Double> line = listaLineasEnCanvas.get(i);
                LineaDibujo linea = new LineaDibujo (
                    1,
                    line.get(0),
                    line.get(1),
                    line.get(2),
                    line.get(3),
                    (int) Math.floor(line.get(4)),
                    line.get(5),
                    saveId
                );
                System.out.println("LineaDibujo{" 
                + "linea_id=" + linea.getLinea_id()
                + ", start_x=" + linea.getStart_x()
                + ", start_y=" + linea.getStart_y()
                + ", end_x=" + linea.getEnd_x()
                + ", end_y=" + linea.getEnd_y()
                + ", color=" + linea.getColor()
                + ", width=" + linea.getWidth()
                + ", save_id=" + linea.getSave_id() + '}');
                try {
                        dao.insert(linea);
                        System.out.println("CanvasDibujo insertado correctamente");
                } catch (SQLException e) {
                            System.err.println("Error insertando CanvasDibujo: " + e.getMessage());
                            e.printStackTrace();
                }
            }
            listaLineasEnCanvas.clear();
        }
    }
    
    @FXML
    private void handleToggleClickLoad() {
        GraphicsContext gc = designCanvas.getGraphicsContext2D();
        LineaDibujoDAO dao = new LineaDibujoDAO();

        if (empty == true) {
            try {
                // Clear canvas first
                gc.clearRect(0, 0, designCanvas.getWidth(), designCanvas.getHeight());

                // Get all lines from database
                List<LineaDibujo> allLines = dao.getAll();

                // Draw each line
                for (LineaDibujo linea : allLines) {
                    // Set line style based on properties
                    ColorLineaDAO colorDAO = new ColorLineaDAO();
                    ColorLinea colorDB = colorDAO.getRGBColor(linea.getColor());
                    colorDB = colorDAO.getRGBColor(linea.getColor());
                    Color RGB = new Color(colorDB.getColor_red(), colorDB.getColor_green(), colorDB.getColor_blue(), 1);
                    gc.setStroke(RGB); 
                    gc.setLineWidth(linea.getWidth());
                    
                    switch (linea.getColor()) {
                        case 1:
                            gc.strokeLine(linea.getStart_x(),linea.getStart_y(),linea.getStart_x(),linea.getEnd_y());
                            gc.strokeLine(linea.getStart_x(),linea.getStart_y(),linea.getEnd_x(),linea.getStart_y());
                            gc.strokeLine(linea.getEnd_x(),linea.getEnd_y(),linea.getEnd_x(),linea.getStart_y());
                            gc.strokeLine(linea.getEnd_x(),linea.getEnd_y(),linea.getStart_x(),linea.getEnd_y());
                            break;
                        case 2:
                            gc.strokeLine(linea.getStart_x(), linea.getStart_y(), linea.getEnd_x(), linea.getEnd_y());
                            break;
                        case 3:
                            gc.strokeLine(linea.getStart_x(), linea.getStart_y(), linea.getEnd_x(), linea.getEnd_y());
                            break;
                    }
                    
                    System.out.println("LineaDibujo{" 
                    + "linea_id=" + linea.getLinea_id()
                    + ", start_x=" + linea.getStart_x()
                    + ", start_y=" + linea.getStart_y()
                    + ", end_x=" + linea.getEnd_x()
                    + ", end_y=" + linea.getEnd_y()
                    + ", color=" + linea.getColor()
                    + ", width=" + linea.getWidth()
                    + ", save_id=" + linea.getSave_id() + '}');
                }
            } catch (SQLException e) {
                // Handle database errors
                System.err.println("Error loading lines: " + e.getMessage());
                e.printStackTrace();

                // Optional: show alert to user
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Loading Error");
                    alert.setHeaderText("Failed to load drawings");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                });
            }
        } else {}
    }
    
    @FXML
    private void handleToggleClickDeleteAll() {
        LineaDibujoDAO dao = new LineaDibujoDAO();
        try {
                dao.deleteAll();
                handleToggleClickLoad();
            } catch (SQLException e) {
                System.out.println("handleToggleClickDeleteAll -> ERROR");
            }
    }
    
    @FXML
    private void handleToggleClickDeleteLast() {
        LineaDibujoDAO dao = new LineaDibujoDAO();
        try {
                dao.deleteLast();
                handleToggleClickLoad();
                
            } catch (SQLException e) {
                System.out.println("handleToggleClickDeleteLast -> ERROR");
            }
    }
    
    @FXML
    private void handleCanvasStoreLine(GraphicsContext gc, double startX, double startY, 
                             double endX, double endY, double color_id, double width) {
        
        try {
            ColorLineaDAO colorDAO = new ColorLineaDAO();
            ColorLinea colorDB = new ColorLinea();
            colorDB = colorDAO.getRGBColor((int)color_id);
            Color RGB = new Color(colorDB.getColor_red(), colorDB.getColor_green(), colorDB.getColor_blue(), 1);
            
            //gc.setStroke(RGB);
            //gc.setLineWidth(width);
            //gc.strokeLine((int)startX, (int)startY, (int)endX, (int)endY);
            
            switch ((int)color_id) {
                        case 1:
                            gc.setStroke(RGB);
                            gc.setLineWidth(width);
                            gc.strokeLine((int) startX, (int) startY, (int) endX, (int) startY);   
                            gc.strokeLine((int) endX, (int) startY, (int) endX, (int) endY);       
                            gc.strokeLine((int) endX, (int) endY, (int) startX, (int) endY);       
                            gc.strokeLine((int) startX, (int) endY, (int) startX, (int) startY);   
                            System.out.println("Case is: " + 1);
                            break;
                        case 2:
                            gc.setStroke(RGB);
                            gc.setLineWidth(width);
                            gc.strokeLine((int)startX, (int)startY, (int)endX, (int)endY);
                            System.out.println("Case is: " + 2);
                            break;
                        case 3:
                            gc.setStroke(RGB);
                            gc.setLineWidth(width);
                            gc.strokeLine((int)startX, (int)startY, (int)endX, (int)endY);
                            System.out.println("Case is: " + 3);
                            break;
                    }
            
            List<Double> lineasEnCanvas = new ArrayList<>();
            lineasEnCanvas.add(startX);
            lineasEnCanvas.add(startY);
            lineasEnCanvas.add(endX);
            lineasEnCanvas.add(endY);
            lineasEnCanvas.add(color_id);
            lineasEnCanvas.add(width);
            listaLineasEnCanvas.add(lineasEnCanvas);
            System.out.println("ColorID es:" + lineasEnCanvas.get(4));
        } catch (SQLException e) {
                    System.err.println("Error guardaando Linea: " + e.getMessage());
                    e.printStackTrace();
        } 
    }
    
    @FXML
    private void handleCanvasClick(MouseEvent event) throws SQLException {
        
        if (!isCanvasClickEnabled) return;
        
        if (isFirstClick) {
            // First click: Store the starting point
            empty = false;
            startX = event.getX();
            startY = event.getY();
            isFirstClick = false;
            
            if (drawSnaps[1].equals(switchClickDrawSnap.getText())) {
                if (Double.compare(snapX, 0) == 0 && Double.compare(snapY, 0) == 0) {
                    
                }
                else {
                    snap = false;
                    startX = snapX;
                    startY = snapY;
                }
            }
        } 
        
        else {
            // Second click: Draw a line from the first point to this one
            empty = false;
            double endX = event.getX();
            double endY = event.getY();
            snapX = endX;
            snapY = endY;
            double deltaX = Math.abs(endX - startX);
            double deltaY = Math.abs(endY - startY);
            
            GraphicsContext gc = designCanvas.getGraphicsContext2D();
            
            if (drawModes[0].equals(switchClickDrawMode.getText())) {
                double color_id = 3.0;
                double width = 2.0;
                handleCanvasStoreLine(gc, startX, startY, 
                             endX, endY, color_id, width);
                handleToggleClickSave();
            }
            if (drawModes[1].equals(switchClickDrawMode.getText())) {
                double color_id = 2.0;
                double width = 2.0;
                if (deltaX > deltaY){
                    handleCanvasStoreLine(gc, startX, startY, 
                             endX, startY, color_id, width);
                    handleToggleClickSave();
                    snapX = endX;
                    snapY = startY;
                    }
                else{
                    handleCanvasStoreLine(gc, startX, startY, 
                             startX, endY, color_id, width);
                    handleToggleClickSave();
                    snapX = startX;
                    snapY = endY;
                }
            }
            if (drawModes[2].equals(switchClickDrawMode.getText())) {
                double color_id = 1.0;
                double width = 2.0; 
                handleCanvasStoreLine(gc, startX, startY, 
                             endX, endY, color_id, width);
            }
            double dx = endX - startX;
            double dy = endY - startY;
            double length = Math.sqrt(dx * dx + dy * dy);
            System.out.println("Length is: " + length);
            System.out.println("startX: " + (int)startX);
            System.out.println("startY: " + (int)startY);
            System.out.println("endX: " + (int)endX);
            System.out.println("endY: " + (int)endY);
            System.out.println("deltaX: " + (int)deltaX);
            System.out.println("deltaY: " + (int)deltaY);
            System.out.println("HashColor: " + (double)Color.GREEN.hashCode());
            System.out.println("Lista General: " + listaLineasEnCanvas);
            
            isFirstClick = true;
        }
    }
}*/


