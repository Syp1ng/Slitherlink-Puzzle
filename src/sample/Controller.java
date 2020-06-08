package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Controller extends Application {

    SlitherlinkSolver pu = new SlitherlinkSolver();
    @FXML
    private GridPane guiGameField;
    @FXML
    private Button stepButton;
    @FXML
    private Button completeButton;
    @FXML
    private VBox vbox;

    private GameField gamefield = new GameField();

    String jsonFile;
    boolean started = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("GUIElements.fxml"));
        primaryStage.setTitle("Slitherlink Solver");

        Scene mainScene = new Scene(root, 500,500);


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
    private void setUpDrop() {
        Label label = new Label("Drag the JSON file here");
        Label dropped = new Label("");
        vbox.getChildren().addAll(label,dropped);

        vbox.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != vbox && event.getDragboard().hasFiles()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });
        vbox.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    jsonFile = db.getFiles().get(0).toString();
                    dropped.setText(jsonFile);
                    success = true;
                    //let user press the buttons
                    activateButtons();
                }
                else {
                    //Disable Buttons
                    reset();
                }
                /* let the source know whether the string was successfully
                 * transferred and used */
                event.setDropCompleted(success);

                event.consume();
            }
        });
        reset();

    }

    @FXML
    void reset(){
        started = false;
        guiGameField.getChildren().clear();
        disableButtons();

    }


    void disableButtons(){
        stepButton.setDisable(true);
        completeButton.setDisable(true);
    }
    void activateButtons(){
        stepButton.setDisable(false);
        completeButton.setDisable(false);
    }
    @FXML
    void completeAction(){
        if(started) GUIEverything();
        else if(prepareGameFieldWithoutErrors()){
            pu.solution(gamefield);
            GUIEverything();
        }
        disableButtons();
    }

    @FXML
    void stepByStepAction(){

        if(started){
            if(!gamefield.stepCollectionEmpty()){
                GUISingle(gamefield.getNextStep());
            }
            else{
                disableButtons();
            }
        }else {
            if(prepareGameFieldWithoutErrors()){
                GUIEverything();
                started = true;
                pu.solution(gamefield);
                stepByStepAction();
            }
        }
    }

    @FXML
    void closeApplication(){
        System.exit(0);
    }

    // Printing Field
    void GUISingle(Point p){
        char characterAtPoint = gamefield.getCharFromPoint(p);
        setStyle(characterAtPoint, p.X, p.Y);

    }
    void GUIEverything(){
        for (int y = 0; y< gamefield.getYSize();y++){
            for(int x=0; x< gamefield.getXSize(); x++){
                char characterAtPoint = gamefield.getCharFromPoint(new Point(x,y));
                setStyle(characterAtPoint,x,y);
            }
        }
    }
    void setStyle(char characterAtPoint, int x, int y){
        if (Character.isDigit(characterAtPoint)||characterAtPoint==GameField.connectionPoint){
            Label label = new Label(Character.toString(characterAtPoint));
            label.setAlignment(Pos.CENTER);
            label.setPrefSize(20,20);
            guiGameField.add(label, x, y);
        }
        if (characterAtPoint==GameField.verticalChar){
            Separator separator1 = new Separator();
            separator1.setPrefSize(20,20);
            separator1.setOrientation(Orientation.VERTICAL);
            separator1.setHalignment(HPos.CENTER);
            separator1.setValignment(VPos.CENTER);
            guiGameField.add (separator1,x,y);
        }
        if(characterAtPoint==GameField.horizontalChar){
            Separator separator1 = new Separator();
            separator1.setPrefSize(20,20);
            separator1.setHalignment(HPos.CENTER);
            separator1.setValignment(VPos.CENTER);
            guiGameField.add (separator1,x,y);
        }
    }

    boolean prepareGameFieldWithoutErrors() {
        gamefield = new GameField();
        try {
            return gamefield.parseJSON(readJSONFile(jsonFile));
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    static JSONObject readJSONFile(String filename) throws JSONException, IOException {
        String content = new String(Files.readAllBytes(Paths.get(filename)));
        return new JSONObject(content);
    }
}
