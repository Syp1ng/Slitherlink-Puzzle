package sample;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;

public class Controller {
    SlitherlinkSolver pu = new SlitherlinkSolver();

    @FXML
    private GridPane guiGameField ;

    private GameField gamefield = new GameField();


    char tab[][] = {
            {'+', '_', '+', '-', '+'},
            {'|', ' ', ' ', '2', '|'},
            {'+', '-', '+', ' ', '|'},
            {'|', '2', '|', ' ', '|',}};


    public void completeAction(){
        GUIGUI();
        pu.stepByStep =false;
        //pu.run();
        gamefield.formatField();
        pu.solution(gamefield);
        GUIGUI();
    }
    boolean started = false;
    public void stepByStepAction(){
        System.out.println("clicked");
         if(started){
                pu.notify();
             //pu.haveAGo= true;//notify();
        }else {
            started = true;
            pu.haveAGo = true;
            pu.stepByStep = true;
             gamefield.formatField();
             pu.run();
            //pu.solution(gamefield);
        }

        GUIGUI();
    }
    public void closeApplication(){
        System.exit(0);
    }


    private void GUIGUI(){
        for (int y = 0; y< gamefield.getYSize();y++){
            for(int x=0; x< gamefield.getXSize(); x++){
                char characterAtPoint = gamefield.getCharFromPoint(new Point(x,y));
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
        }


    }

    public String text ;
    public void dropAction(DragEvent dragEvent) {
        Dragboard db = dragEvent.getDragboard();
        boolean success =false;
        if (db.hasString()) {
            text = db.getFiles().toString();
            success = true;
        }
        else{
        }

        System.out.println(success);
        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }

    public void dragAction(DragEvent dragEvent) {
            if (dragEvent.getDragboard().hasFiles()) {
                /* allow for both copying and moving, whatever user chooses */
                dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
        dragEvent.consume();
        }
}
