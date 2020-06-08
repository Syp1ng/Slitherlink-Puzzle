package sample;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Stack;
import org.json.*;

public class GameField {
    public GameField(){
        solutionSteps = new Stack<>();
    }
    //only used for testing
    public GameField (char testingField[][]){
        solutionSteps = new Stack<>();
        fieldData = testingField;

    }
    //symbols used internal for the fieldData Array
    static char horizontalChar = '-';
    static char verticalChar = '|';
    static char connectionPoint = '+';

    private Stack<Point> solutionSteps;
    private char fieldData[][];
/*
    char fieldData[][] ={
            {'.', ' ', '.', ' ', '.', ' ', '.', ' ', '.',},
            {' ', '1', ' ', '2', ' ', '2', ' ', '3', ' ',},
            {'.', ' ', '.', ' ', '.', ' ', '.', ' ', '.',},
            {' ', ' ', ' ', '1', ' ', '1', ' ', '3', ' ',},
            {'.', ' ', '.', ' ', '.', ' ', '.', ' ', '.',},
            {' ', '3', ' ', '1', ' ', '1', ' ', '3', ' ',},
            {'.', ' ', '.', ' ', '.', ' ', '.', ' ', '.',},
            {' ', '3', ' ', '2', ' ', '2', ' ', ' ', ' ',},
            {'.', ' ', '.', ' ', '.', ' ', '.', ' ', '.',}
    };


    /*
    char fieldData[][] ={
            {'.', ' ', '.', ' ', '.', ' ', '.', ' ', '.', ' ', '.', ' ', '.', ' ','.', ' ', '.'},
            {' ', ' ', ' ', '3', ' ', ' ', ' ', ' ', ' ', '3', ' ', ' ', ' ', ' ',' ', '1', ' '},
            {'.', ' ', '.', ' ', '.', ' ', '.', ' ', '.', ' ', '.', ' ', '.', ' ','.', ' ', '.'},
            {' ', '3', ' ', ' ', ' ', '2', ' ', '0', ' ', ' ', ' ', '3', ' ', ' ',' ', ' ', ' '},
            {'.', ' ', '.', ' ', '.', ' ', '.', ' ', '.', ' ', '.', ' ', '.', ' ','.', ' ', '.'},
            {' ', ' ', ' ', ' ', ' ', '3', ' ', '2', ' ', ' ', ' ', ' ', ' ', '0',' ', ' ', ' '},
            {'.', ' ', '.', ' ', '.', ' ', '.', ' ', '.', ' ', '.', ' ', '.', ' ','.', ' ', '.'},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '1', ' ', '2',' ', ' ', ' '},
            {'.', ' ', '.', ' ', '.', ' ', '.', ' ', '.', ' ', '.', ' ', '.', ' ','.', ' ', '.'},
            {' ', ' ', ' ', '2', ' ', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',' ', ' ', ' '},
            {'.', ' ', '.', ' ', '.', ' ', '.', ' ', '.', ' ', '.', ' ', '.', ' ','.', ' ', '.'},
            {' ', ' ', ' ', '2', ' ', ' ', ' ', ' ', ' ', '0', ' ', '1', ' ', ' ',' ', ' ', ' '},
            {'.', ' ', '.', ' ', '.', ' ', '.', ' ', '.', ' ', '.', ' ', '.', ' ','.', ' ', '.'},
            {' ', ' ', ' ', ' ', ' ', '1', ' ', ' ', ' ', '1', ' ', '2', ' ', ' ',' ', '2', ' '},
            {'.', ' ', '.', ' ', '.', ' ', '.', ' ', '.', ' ', '.', ' ', '.', ' ','.', ' ', '.'},
            {' ', '3', ' ', ' ', ' ', ' ', ' ', '1', ' ', ' ', ' ', ' ', ' ', '3',' ', '2', ' '},
            {'.', ' ', '.', ' ', '.', ' ', '.', ' ', '.', ' ', '.', ' ', '.', ' ','.', ' ', '.'},
    };
    /*
    public char field[][] ={
            {'.', ' ', '.', ' ', '.', '-', '.', '-', '.',},
            {' ', '1', ' ', '2', '-', '2', ' ', '3', '-',},
            {'.', '-', '.', '-', '.', ' ', '.', '-', '.',},
            {' ', ' ', ' ', '1', ' ', '1', '-', '3', ' ',},
            {'.', '-', '.', ' ', '.', ' ', '.', '-', '.',},
            {' ', '3', '-', '1', ' ', '1', ' ', '3', '-',},
            {'.', '-', '.', ' ', '.', '-', '.', '-', '.',},
            {'-', '3', ' ', '2', '-', '2', ' ', ' ', ' ',},
            {'.', '-', '.', '-', '.', ' ', '.', ' ', '.',}
    };
*/
    public void addSolutionStep(Point p){
        System.out.println(p.X+"/"+p.Y);
        solutionSteps.push(p);
    }
    public boolean stepCollectionEmpty(){
        if (solutionSteps.empty())return true;
        return false;
    }
    public Point getNextStep(){
        if (solutionSteps.empty()) return null;
        return  solutionSteps.pop();
    }
    public char getCharFromPoint(Point p){
        return fieldData[p.Y][p.X];
    }
    public char getCharFromPoint(int x, int y){
        return fieldData[y][x];
    }
    public void setCharToField(Point p, char c){
        fieldData[p.Y][p.X] = c;
    }
    public void resetLine(Point p){
        fieldData[p.Y][p.X] =' ';
    }
    public int getYSize(){
        return fieldData.length;
    }
    public int getXSize(){
        return fieldData[0].length;
    }

    public void printToConsole(){
        System.out.println("**************************");
        for (int y = 0; y< fieldData.length; y++){
            for (int x = 0; x < fieldData[y].length; x++){
                System.out.print(getCharFromPoint(x,y));
            }
            System.out.println("");
        }
        System.out.println("**************************");
    }


    public boolean parseJSON(JSONObject jsonObject) {
        try{
            //Different options to parse the fields
            Boolean spacesForLines = jsonObject.getBoolean("spaceForLines");
            int gameFieldWidth = jsonObject.getInt("gameFieldWidth");
            int gameFieldHeight = jsonObject.getInt("gameFieldHeight");
            JSONArray jsonArray = jsonObject.getJSONArray("Points");

            if(spacesForLines==false){
                gameFieldHeight = gameFieldHeight * 2 +1;
                gameFieldWidth = gameFieldWidth * 2 +1;
            }
            fieldData = new char[gameFieldHeight][gameFieldWidth];
            formatField();

            for(int i = 0; i < jsonArray.length(); i++){
                int x = jsonArray.getJSONObject(i).getInt("x");
                int y = jsonArray.getJSONObject(i).getInt("y");
                char number = Character.forDigit(jsonArray.getJSONObject(i).getInt("n"),10);
                if (spacesForLines==false){
                    x=x*2 + 1;
                    y=y*2 + 1;
                }
                setCharToField(new Point(x,y), number);
            }
            return true;
        }
        catch (Exception e){
            System.out.println("Error in parsing: " + e.getMessage());
            return false;
        }
    }




    public void formatField(){
        printToConsole();
        for (int y = 0; y< fieldData.length; y++){
            for (int x = 0; x < fieldData[y].length; x++){
                if(y % 2 ==0 && x % 2==0) fieldData[y][x]=connectionPoint;
                else fieldData[y][x]=' ';
            }
        }
        printToConsole();
    }
}
