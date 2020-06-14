package sample;

import java.util.Stack;

public class SlitherlinkSolver {
    Point startingPoint;

    //starting point
    public boolean solution(GameField gameField) {
        System.out.println("starting Backtracking");
        GameField finalSolution;
        //get 2 starting points
        Point[] availableStartingPoints = getStartingPoint(gameField);
        for (Point startingPoint : availableStartingPoints) {
            this.startingPoint = startingPoint;
            finalSolution = nextStep(gameField, startingPoint);
            if (finalSolution != null) {
                finalSolution.printToConsole();
                return true;
            }
        }
        return false;
    }

    //backtracking --> calling every time nextStep() with the actual gameField and the new Point where to go maybe
    public GameField nextStep(GameField gameField, Point p) {
        //For Debugging the steps
        //gameField.printToConsole();

        //check if puzzle is solved
        if (checkFinished(gameField, p)) {
            System.out.println("solution found");
            return gameField;
        }

        //don't connect to another line until its finally finished
        if (!checkConnectionsPoint(gameField, p)) return null;

        //now checking possible directions

        //Down
        if (checkICanGoTHere(gameField, new Point(p.getX(), p.getY() + 1))) {
            gameField.setCharToField(new Point(p.getX(), p.getY() + 1), GameField.verticalChar);
            GameField solution = nextStep(gameField, new Point(p.getX(), p.getY() + 2));
            if (solution != null) {
                gameField.addSolutionStep(new Point(p.getX(), p.getY() + 1));
                return solution;
            }
            gameField.resetLine(new Point(p.getX(), p.getY() + 1));
        }
        //Up
        if (checkICanGoTHere(gameField, new Point(p.getX(), p.getY() - 1))) {
            gameField.setCharToField(new Point(p.getX(), p.getY() - 1), GameField.verticalChar);
            GameField solution = nextStep(gameField, new Point(p.getX(), p.getY() - 2));
            if (solution != null) {
                gameField.addSolutionStep(new Point(p.getX(), p.getY() - 1));
                return solution;
            }
            gameField.resetLine(new Point(p.getX(), p.getY() - 1));
        }
        //right
        if (checkICanGoTHere(gameField, new Point(p.getX() + 1, p.getY()))) {
            gameField.setCharToField(new Point(p.getX() + 1, p.getY()), GameField.horizontalChar);
            GameField solution = nextStep(gameField, new Point(p.getX() + 2, p.getY()));
            if (solution != null) {
                gameField.addSolutionStep(new Point(p.getX() + 1, p.getY()));
                return solution;
            }
            gameField.resetLine(new Point(p.getX() + 1, p.getY()));
        }
        //left
        if (checkICanGoTHere(gameField, new Point(p.getX() - 1, p.getY()))) {
            gameField.setCharToField(new Point(p.getX() - 1, p.getY()), GameField.horizontalChar);
            GameField solution = nextStep(gameField, new Point(p.getX() - 2, p.getY()));
            if (solution != null) {
                gameField.addSolutionStep(new Point(p.getX() - 1, p.getY()));
                return solution;
            }
            gameField.resetLine(new Point(p.getX() - 1, p.getY()));
        }
        //no solution --> return null
        return null;

    }

    //Check if there is connection between Points, so don't cross a connection between two points --> because you have to go back directly --> at the end you close the circle
    boolean checkConnectionsPoint(GameField gameField, Point p) {
        int currentLinesOnPoint = 0;
        if (p.getY() + 1 < gameField.getYSize() && gameField.getCharFromPoint(p.getX(), p.getY() + 1) != ' ') {
            currentLinesOnPoint++;
        }
        if (p.getY() - 1 > 0 && gameField.getCharFromPoint(p.getX(), p.getY() - 1) != ' ') {
            currentLinesOnPoint++;
        }
        if (p.getX() + 1 < gameField.getXSize() && gameField.getCharFromPoint(p.getX() + 1, p.getY()) != ' ') {
            currentLinesOnPoint++;
        }
        if (p.getX() - 1 > 0 && gameField.getCharFromPoint(p.getX() - 1, p.getY()) != ' ') {
            currentLinesOnPoint++;
        }
        return currentLinesOnPoint < 2; //then there aren't two lines connected --> valid
    }

    //checks if the Point can be set in the actual state of the field
    boolean checkICanGoTHere(GameField gameField, Point p) {
        if (p.getY() > gameField.getYSize() - 1 || p.getY() < 0 ||
                p.getX() > gameField.getXSize() - 1 || p.getX() < 0) return false;
        if (gameField.getCharFromPoint(p) != ' ') return false;
        //get all Numbers associated with this place
        Stack<Point> numberFields = new Stack<>();
        if (p.getY() + 1 < gameField.getYSize() && Character.isDigit(gameField.getCharFromPoint(p.getX(), p.getY() + 1))) {
            numberFields.push(new Point(p.getX(), p.getY() + 1));
        }
        if (p.getY() - 1 > 0 && Character.isDigit(gameField.getCharFromPoint(p.getX(), p.getY() - 1))) {
            numberFields.push(new Point(p.getX(), p.getY() - 1));
        }
        if (p.getX() + 1 < gameField.getXSize() && Character.isDigit(gameField.getCharFromPoint(p.getX() + 1, p.getY()))) {
            numberFields.push(new Point(p.getX() + 1, p.getY()));
        }
        if (p.getX() - 1 > 0 && Character.isDigit(gameField.getCharFromPoint(p.getX() - 1, p.getY()))) {
            numberFields.push(new Point(p.getX() - 1, p.getY()));
        }
        //check if nodes  are already full
        while (!numberFields.empty()) {
            int allowedRoutes = Character.getNumericValue(gameField.getCharFromPoint(numberFields.lastElement()));
            int actualCounter = countConnectionsOnPoint(gameField, numberFields.lastElement());
            if (allowedRoutes == actualCounter) return false;
            numberFields.pop();
        }

        return true;
    }

    //count actual connections at the actual gamefield at a single number point
    int countConnectionsOnPoint(GameField gameField, Point p) {
        int counter = 0;
        if (gameField.getCharFromPoint(p.getX(), p.getY() + 1) != ' ') counter++;
        if (gameField.getCharFromPoint(p.getX(), p.getY() - 1) != ' ') counter++;
        if (gameField.getCharFromPoint(p.getX() + 1, p.getY()) != ' ') counter++;
        if (gameField.getCharFromPoint(p.getX() - 1, p.getY()) != ' ') counter++;
        return counter;
    }

    //check if puzzle is solved
    boolean checkFinished(GameField gameField, Point p) {
        if (!(p.getX() == startingPoint.getX() && p.getY() == startingPoint.getY())) return false;
        for (int y = 1; y < gameField.getYSize(); y++) {
            for (int x = 1; x < gameField.getXSize(); x += 2) {
                if (Character.isDigit(gameField.getCharFromPoint(x, y))) {
                    if (Character.getNumericValue(gameField.getCharFromPoint(x, y)) != countConnectionsOnPoint(gameField, new Point(x, y)))
                        return false;
                }
            }
        }
        return true;
    }

    //get two starting point: --> first find a number point > 0 -> get two connection points --> at one of them a connection must be for sure -->logical
    Point[] getStartingPoint(GameField gameField) {
        for (int y = 1; y < gameField.getYSize(); y++) {
            for (int x = 1; x < gameField.getXSize(); x += 2) {
                char characterAtPoint = gameField.getCharFromPoint(x, y);
                //game makes no sense without numbers --> that's why i suppose there is a number
                if (Character.isDigit(characterAtPoint) && Character.getNumericValue(characterAtPoint) != 0) {
                    return new Point[]{new Point(x + 1, y + 1), new Point(x - 1, y - 1)};
                }
            }
        }
        return null;
    }
}

