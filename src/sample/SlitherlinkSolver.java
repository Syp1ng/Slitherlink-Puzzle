package sample;

import javafx.application.Application;
import javafx.fxml.JavaFXBuilderFactory;

import java.util.Stack;

public class SlitherlinkSolver extends Thread {
    public boolean stepByStep = true; //for going Step By Step
    private Point startingPoint;

    public void run(){
        var x = solution(new GameField());
    }
    public GameField solution(GameField gameField) {
        System.out.println("starting Backtracking");
        GameField finalSolution = new GameField();
        Point[] availableStartingPoints = getStartingPoint(gameField);
        for (Point startingPoint : availableStartingPoints){
            this.startingPoint = startingPoint;
            finalSolution = nextStep(gameField, startingPoint);
            if(finalSolution != null) {
                finalSolution.printToConsole();
                break;
            }
        }
        return finalSolution;
    }
    public boolean haveAGo;
    private GameField nextStep(GameField gameField, Point p){
        //triggerGuiUpdate();

        //gameField.outConsole();


        if(stepByStep) {
            /*
            while (haveAGo == false) {

            }
            */
                try {
                    System.out.println("wait");
                    currentThread().suspend();

                } catch (Exception e) {

            }
        }


        if(checkFinished(gameField, p)) {
            System.out.println("solution found");
            return gameField;
        }

        //don't connect to another line until its finally finished
        if(!checkConnectionsPoint(gameField, p)) return null;

        if(checkICanGoTHere(gameField, new Point (p.X, p.Y+1))){
            gameField.setLine(new Point(p.X, p.Y+1), GameField.verticalChar);
            GameField solution =  nextStep(gameField, new Point(p.X, p.Y+2));
            if(solution!=null) return solution;
            gameField.resetLine(new Point(p.X, p.Y+1));
        }
        if(checkICanGoTHere(gameField, new Point (p.X, p.Y-1))){
            gameField.setLine(new Point(p.X, p.Y-1), GameField.verticalChar);
            GameField solution =  nextStep(gameField, new Point(p.X, p.Y-2));
            if(solution!=null) return solution;
            gameField.resetLine(new Point(p.X, p.Y-1));
        }
        if(checkICanGoTHere(gameField, new Point (p.X+1, p.Y))){
            gameField.setLine(new Point(p.X+1, p.Y), GameField.horizontalChar);
            GameField solution =  nextStep(gameField, new Point(p.X+2, p.Y));
            if(solution!=null) return solution;
            gameField.resetLine(new Point(p.X+1, p.Y));
        }
        if(checkICanGoTHere(gameField, new Point (p.X-1, p.Y))){
            gameField.setLine(new Point(p.X-1, p.Y), GameField.horizontalChar);
            GameField solution =  nextStep(gameField, new Point(p.X-2, p.Y));
            if(solution!=null) return solution;
            gameField.resetLine(new Point(p.X-1, p.Y));
        }
        return null;

    }

    private boolean checkConnectionsPoint(GameField gameField, Point p){
        int currentLinesOnPoint =0;
        if (p.Y+1 <gameField.getYSize() && gameField.getCharFromPoint(p.X, p.Y+1)!=' '){
            currentLinesOnPoint++;
        }
        if (p.Y-1 > 0 && gameField.getCharFromPoint(p.X, p.Y-1)!=' '){
            currentLinesOnPoint++;
        }
        if (p.X+1 <gameField.getXSize()&& gameField.getCharFromPoint(p.X+1, p.Y)!=' '){
            currentLinesOnPoint++;
        }
        if (p.X-1 > 0 && gameField.getCharFromPoint(p.X-1, p.Y)!=' '){
            currentLinesOnPoint++;
        }
        if (currentLinesOnPoint<2) return true;
        return  false;
    }
    private boolean checkICanGoTHere(GameField gameField, Point p) {
        //empty Field?
        //System.out.println(p.X+"/"+p.Y);
        if (p.Y > gameField.getYSize() -1 || p.Y< 0 ||
                p.X> gameField.getXSize()-1 || p.X <0) return false;
        if (gameField.getCharFromPoint(p)!=' ') return false;
        //get all Numbers associated with this place
        Stack<Point> numberFields = new Stack<>();
        if (p.Y+1 <gameField.getYSize() &&Character.isDigit(gameField.getCharFromPoint(p.X, p.Y+1))){
            numberFields.push(new Point(p.X, p.Y+1));
        }
        if (p.Y-1 > 0 && Character.isDigit(gameField.getCharFromPoint(p.X, p.Y-1))){
            numberFields.push(new Point(p.X, p.Y-1));
        }
        if (p.X+1 <gameField.getXSize() && Character.isDigit(gameField.getCharFromPoint(p.X+1, p.Y))){
            numberFields.push(new Point(p.X+1, p.Y));
        }
        if (p.X-1 > 0 && Character.isDigit(gameField.getCharFromPoint(p.X-1, p.Y))){
            numberFields.push(new Point(p.X-1, p.Y));
        }
        //check if nodes  are already full
        while(!numberFields.empty()){
            int aquiredRoutes = Character.getNumericValue(gameField.getCharFromPoint(numberFields.lastElement()));
            int actualCounter = countConnectionsOnPoint(gameField, numberFields.lastElement());
            if (aquiredRoutes == actualCounter) return false;
            numberFields.pop();
        }

        return true;
    }

    private int countConnectionsOnPoint(GameField gameField, Point p) {
        int counter = 0;
        if (gameField.getCharFromPoint(p.X, p.Y+1)!=' ') counter++;
        if (gameField.getCharFromPoint(p.X, p.Y-1)!=' ') counter++;
        if (gameField.getCharFromPoint(p.X+1, p.Y)!=' ') counter++;
        if (gameField.getCharFromPoint(p.X-1, p.Y)!=' ') counter++;
        return counter;
    }

    private  boolean checkFinished(GameField gameField, Point p) {
        if(!(p.X == startingPoint.X && p.Y == startingPoint.Y)) return false;
        for (int y = 1; y < gameField.getYSize(); y++) {
            for (int x = 1; x < gameField.getXSize(); x += 2) {
                if (Character.isDigit(gameField.getCharFromPoint(x,y))){
                    if (Character.getNumericValue(gameField.getCharFromPoint(x,y))!=countConnectionsOnPoint(gameField, new Point(x,y))) return false;
                }
            }
        }
        return true;
    }

    public Point[] getStartingPoint(GameField gameField) {
        for (int y = 1; y < gameField.getYSize(); y++) {
            for (int x = 1; x < gameField.getXSize(); x += 2) {
                char characterAtPoint = gameField.getCharFromPoint(x,y);
                if (Character.isDigit(characterAtPoint)&&Character.getNumericValue(characterAtPoint)!=0){
                    return new Point[]{new Point(x+1, y+1), new Point(x-1,y-1)};
                }
            }
        }
        return null;
    }

}

