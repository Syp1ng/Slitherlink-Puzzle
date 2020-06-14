package sample;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Stack;

public class GameField {
    //symbols used internal for the fieldData Array
    static final char connectionPoint = '+';
    static final char horizontalChar = '-';
    static final char verticalChar = '|';

    //later used for every step in the solution
    private final Stack<Point> solutionSteps;
    private char[][] fieldData;

    public GameField() {
        solutionSteps = new Stack<>();
    }

    //this constructor only used for testing
    public GameField(char[][] testingField) {
        solutionSteps = new Stack<>();
        fieldData = testingField;
    }

    /* //example Data
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
    */
    //push all right steps from the solution in a stack
    public void addSolutionStep(Point p) {
        System.out.println(p.getX() + "/" + p.getY());
        solutionSteps.push(p);
    }

    //a acheck if it is empty
    public boolean stepCollectionEmpty() {
        return solutionSteps.empty();
    }

    //for the step Solution --> return top of stack--> pop it --> until it is empty
    public Point getNextStep() {
        if (solutionSteps.empty()) return null;
        return solutionSteps.pop();
    }

    //get the Char from the field with a Point p
    public char getCharFromPoint(Point p) {
        return fieldData[p.getY()][p.getX()];
    }

    //get the Char from the field with two ints, x and y
    public char getCharFromPoint(int x, int y) {
        return fieldData[y][x];
    }

    //set a specific char to the field /gameData[][] --> could be line (in solver) or a number (in parsing)
    public void setCharToField(Point p, char c) {
        fieldData[p.getY()][p.getX()] = c;
    }

    //reset the Line
    public void resetLine(Point p) {
        fieldData[p.getY()][p.getX()] = ' ';
    }

    public int getYSize() {
        return fieldData.length;
    }

    public int getXSize() {
        return fieldData[0].length;
    }

    //print actual gameField / fielData[][] to console
    public void printToConsole() {
        System.out.println("**************************");
        for (int y = 0; y < fieldData.length; y++) {
            for (int x = 0; x < fieldData[y].length; x++) {
                System.out.print(getCharFromPoint(x, y));
            }
            System.out.println();
        }
        System.out.println("**************************");
    }


    //parse the jsonObject and save it in the fielData[][]
    public boolean parseJSON(JSONObject jsonObject) {
        try {
            //Different options to parse the fields
            boolean spacesForLines = jsonObject.getBoolean("spaceForLines");
            int gameFieldWidth = jsonObject.getInt("gameFieldWidth");
            int gameFieldHeight = jsonObject.getInt("gameFieldHeight");
            JSONArray jsonArray = jsonObject.getJSONArray("Points");

            if (!spacesForLines) {
                gameFieldHeight = gameFieldHeight * 2 + 1;
                gameFieldWidth = gameFieldWidth * 2 + 1;
            }
            fieldData = new char[gameFieldHeight][gameFieldWidth];
            formatField();

            for (int i = 0; i < jsonArray.length(); i++) {
                int x = jsonArray.getJSONObject(i).getInt("x");
                int y = jsonArray.getJSONObject(i).getInt("y");
                char number = Character.forDigit(jsonArray.getJSONObject(i).getInt("n"), 10);
                if (!spacesForLines) {
                    x = x * 2 + 1;
                    y = y * 2 + 1;
                }
                setCharToField(new Point(x, y), number);
            }
            printToConsole();
            return true;
        } catch (Exception e) {
            System.out.println("Error in parsing: " + e.getMessage());
            return false;
        }
    }


    //prepare the right spaces for the fielData[][] that lines also have space
    public void formatField() {
        for (int y = 0; y < fieldData.length; y++) {
            for (int x = 0; x < fieldData[y].length; x++) {
                if (y % 2 == 0 && x % 2 == 0) fieldData[y][x] = connectionPoint;
                else fieldData[y][x] = ' ';
            }
        }
    }
}
