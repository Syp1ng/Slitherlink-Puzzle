package sample;

import javafx.application.Application;
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
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
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
    private boolean started = false;
    private SlitherlinkSolver pu = new SlitherlinkSolver();
    private GameField gamefield = new GameField();
    final int sizeGuiElements = 30;
    final int fontSize = 20;

    //FXML Elements
    @FXML
    private GridPane guiGameField;
    @FXML
    private Button stepButton;
    @FXML
    private Button completeButton;


    public static void main(String[] args) {
        launch(args);
    }

    static JSONObject readJSONFile(File file) throws JSONException, IOException {
        String content = new String(Files.readAllBytes(file.toPath()));
        return new JSONObject(content);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("GUIElements.fxml"));
        primaryStage.setTitle("Slitherlink Solver");

        Scene mainScene = new Scene(root, 500, 500);

        mainScene.getStylesheets().add("sample/controlStyle.css");


        //Key Binding
        mainScene.setOnKeyPressed(e -> {
            KeyCode l = e.getCode();
            switch (l) {
                case F5:
                    stepByStepAction();
                    break;
                case F6:
                    completeAction();
                    break;
                case F8:
                    closeApplication();
                    break;
            }
        });


        primaryStage.setScene(mainScene);
        primaryStage.show();
        }

    @FXML
    void reset() {
        started = false;
        guiGameField.getChildren().clear();
        disableButtons();

    }

    void disableButtons() {
        stepButton.setDisable(true);
        completeButton.setDisable(true);
    }

    void activateButtons() {
        stepButton.setDisable(false);
        completeButton.setDisable(false);
    }

    @FXML
    void completeAction() {
        if (started) GUIEverything();
        else {
            pu.solution(gamefield);
            GUIEverything();
            disableButtons();
        }
    }

    @FXML
    void stepByStepAction() {
        if (started) {
            if (!gamefield.stepCollectionEmpty()) {
                GUISingle(gamefield.getNextStep());
            } else {
                disableButtons();
            }
        } else {
            started = true;
            pu.solution(gamefield);
            stepByStepAction();

        }
    }

    @FXML
    void closeApplication() {
        System.exit(0);
    }

    // Printing Field
    void GUISingle(Point p) {
        char characterAtPoint = gamefield.getCharFromPoint(p);
        setStyle(characterAtPoint, p.X, p.Y);

    }

    void GUIEverything() {
        for (int y = 0; y < gamefield.getYSize(); y++) {
            for (int x = 0; x < gamefield.getXSize(); x++) {
                char characterAtPoint = gamefield.getCharFromPoint(new Point(x, y));
                setStyle(characterAtPoint, x, y);
            }
        }
    }

    void setStyle(char characterAtPoint, int x, int y) {
        if (Character.isDigit(characterAtPoint) || characterAtPoint == GameField.connectionPoint | characterAtPoint ==' ') {
            Label label = new Label(Character.toString(characterAtPoint));
            label.setAlignment(Pos.CENTER);
            label.setFont(Font.font(null, FontWeight.BOLD, fontSize));
            label.setPrefSize(sizeGuiElements, sizeGuiElements);
            guiGameField.add(label, x, y);
        }
        if (characterAtPoint == GameField.verticalChar) {
            Separator separator1 = new Separator();
            separator1.setPrefSize(sizeGuiElements, sizeGuiElements);
            separator1.setOrientation(Orientation.VERTICAL);
            separator1.setHalignment(HPos.CENTER);
            separator1.setValignment(VPos.CENTER);
            guiGameField.add(separator1, x, y);
        }
        if (characterAtPoint == GameField.horizontalChar) {
            Separator separator1 = new Separator();
            separator1.setPrefSize(sizeGuiElements, sizeGuiElements);
            separator1.setHalignment(HPos.CENTER);
            separator1.setValignment(VPos.CENTER);
            guiGameField.add(separator1, x, y);
        }
    }

    boolean prepareGameFieldWithoutErrors(File file) {
        gamefield = new GameField();
        try {
            return gamefield.parseJSON(readJSONFile(file));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    @FXML
    void handleDragDrop(DragEvent event) {
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
    void handleDragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }
    }
}
