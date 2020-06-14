package sample;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


public class Controller extends Application {
    final int sizeGuiElements = 30;
    final int fontSize = 20;
    //FXML Elements
    @FXML
    GridPane guiGameField;
    @FXML
    Button stepButton;
    @FXML
    Button completeButton;
    private boolean started = false;
    private SlitherlinkSolver pu = new SlitherlinkSolver();
    private GameField gamefield = new GameField();
    private boolean keysAreActive = false;

    //-->main
    public static void main(String[] args) {
        launch(args);
    }


    //returns the json output of a file (path)
    private static JSONObject readJSONFile(File file) throws JSONException, IOException {
        String content = new String(Files.readAllBytes(file.toPath()));
        return new JSONObject(content);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("GUIElements.fxml"));
        primaryStage.setTitle("Slitherlink Solver");

        Scene mainScene = new Scene(root, 500, 500);

        //css for design of the lines
        mainScene.getStylesheets().add("sample/controlStyle.css");

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    //reset everything in GUI Field and disable the buttons
    @FXML
    private void reset() {
        started = false;
        guiGameField.getChildren().clear();
        disableButtons();

    }

    private void disableButtons() {
        stepButton.setDisable(true);
        completeButton.setDisable(true);
        keysAreActive = false;
    }

    private void activateButtons() {
        stepButton.setDisable(false);
        completeButton.setDisable(false);
        keysAreActive = true;
    }

    //show the complete Solution at once
    @FXML
    private void completeAction() {
        if (!started) {
            //calculate the solution if not already happened
            pu.solution(gamefield);
        }
        GUIEverything();
        disableButtons();
    }

    //show every final solution step
    @FXML
    private void stepByStepAction() {
        if (started) {
            //show step by step until stack of steps is empty
            if (!gamefield.stepCollectionEmpty()) {
                GUISingle(gamefield.getNextStep());
            } else {
                disableButtons();
            }
        } else {
            //calculate solution if not already happened --> show a step (call this function again)
            started = true;
            pu.solution(gamefield);
            stepByStepAction();
        }
    }

    @FXML
    private void closeApplication() {
        System.exit(0);
    }

    // call GUI designer with only one single point
    private void GUISingle(Point p) {
        char characterAtPoint = gamefield.getCharFromPoint(p);
        setStyle(characterAtPoint, p.getX(), p.getY());

    }

    //get every point of the field --> call GUI designer with every point
    private void GUIEverything() {
        for (int y = 0; y < gamefield.getYSize(); y++) {
            for (int x = 0; x < gamefield.getXSize(); x++) {
                char characterAtPoint = gamefield.getCharFromPoint(new Point(x, y));
                setStyle(characterAtPoint, x, y);
            }
        }
    }

    //here the point will be set to the GUI grid
    private void setStyle(char characterAtPoint, int x, int y) {
        //design for connection point, numbers and empty cells
        if (Character.isDigit(characterAtPoint) || characterAtPoint == GameField.connectionPoint | characterAtPoint == ' ') {
            Label label = new Label(Character.toString(characterAtPoint));
            label.setAlignment(Pos.CENTER);
            label.setFont(Font.font(null, FontWeight.BOLD, fontSize));
            label.setPrefSize(sizeGuiElements, sizeGuiElements);
            guiGameField.add(label, x, y);
        }
        //design for vertical Line
        if (characterAtPoint == GameField.verticalChar) {
            Separator separator1 = new Separator();
            separator1.setPrefSize(sizeGuiElements, sizeGuiElements);
            separator1.setOrientation(Orientation.VERTICAL);
            separator1.setHalignment(HPos.CENTER);
            separator1.setValignment(VPos.CENTER);
            guiGameField.add(separator1, x, y);
        }
        //design for horizontal line
        if (characterAtPoint == GameField.horizontalChar) {
            Separator separator1 = new Separator();
            separator1.setPrefSize(sizeGuiElements, sizeGuiElements);
            separator1.setHalignment(HPos.CENTER);
            separator1.setValignment(VPos.CENTER);
            guiGameField.add(separator1, x, y);
        }
    }

    //prepare Gamefield with no error--> create gamefield --> parse
    private boolean prepareGameFieldWithoutErrors(File file) {
        gamefield = new GameField();
        try {
            return gamefield.parseJSON(readJSONFile(file));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    //Drag and drop event --> handle file --> GUI the starting points
    @FXML
    private void handleDragDrop(DragEvent event) {
        Dragboard db = event.getDragboard();
        File file = db.getFiles().get(0);
        if (prepareGameFieldWithoutErrors(file)) {
            GUIEverything();
            activateButtons();
        } else {
            disableButtons();
        }
    }

    @FXML
    private void handleDragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }
    }

    //handler for keyPressed --> do certain actions when certain key is pressed & active
    @FXML
    private void keyPressedHandler(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case F5:
                if(keysAreActive){
                    stepByStepAction();
                }
                break;
            case F6:
                if(keysAreActive){
                    completeAction();
                }
                break;
            case F8:
                closeApplication();
                break;
        }
    }
}
