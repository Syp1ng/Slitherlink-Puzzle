package sample;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlitherlinkSolverTest {

    char[][] exampleField = {
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
    char[][] exampleFieldWithConnections = {
            {'.', ' ', '.', ' ', '.', ' ', '.', ' ', '.',},
            {' ', '1', ' ', '2', ' ', '2', ' ', '3', ' ',},
            {'.', ' ', '.', ' ', '.', ' ', '.', ' ', '.',},
            {' ', ' ', ' ', '1', ' ', '1', ' ', '3', ' ',},
            {'.', ' ', '.', ' ', '.', ' ', '.', ' ', '.',},
            {'|', '3', '|', '1', ' ', '1', ' ', '3', ' ',},
            {'.', ' ', '.', ' ', '.', ' ', '.', ' ', '.',},
            {'|', '3', '|', '2', ' ', '2', ' ', ' ', ' ',},
            {'.', '-', '.', ' ', '.', ' ', '.', ' ', '.',}
    };
    char[][] exampleNoSolution = {
            {'.', ' ', '.', ' ', '.', ' ', '.',},
            {' ', '1', ' ', '0', ' ', '0', ' ',},
            {'.', ' ', '.', ' ', '.', ' ', '.',},
            {' ', ' ', ' ', '0', ' ', '0', ' ',},
            {'.', ' ', '.', ' ', '.', ' ', '.',},
            {' ', '3', ' ', '0', ' ', '1', ' ',},
            {'.', ' ', '.', ' ', '.', ' ', '.',}
    };

    @Test
    void solution() {
        SlitherlinkSolver solver = new SlitherlinkSolver();
        GameField gameField = new GameField(exampleField);
        boolean hasSolution = solver.solution(gameField);
        assertTrue(hasSolution, "Should find a solution");


        gameField = new GameField(exampleNoSolution);
        hasSolution = solver.solution(gameField);
        assertFalse(hasSolution, "Should find a solution");
    }

    @Test
    void nextStep() {
        SlitherlinkSolver solver = new SlitherlinkSolver();
        GameField gameField = new GameField(exampleField);
        solver.startingPoint = new Point(2, 2);

        solver.nextStep(gameField, solver.startingPoint);
        assertFalse(gameField.stepCollectionEmpty(), "this function should safe the steps in a collection when it has a solution");
        assertEquals(3, gameField.getNextStep().getX(), "The first point should be 3/1 and saved in the collection");
        assertEquals(1, gameField.getNextStep().getY(), "The first point should be 3/1 and saved in the collection");

        gameField = new GameField(exampleNoSolution);
        assertTrue(gameField.stepCollectionEmpty(), "this function should safe the steps in a collection, but there's no solution -->empty");
    }

    @Test
    void checkConnectionsPoint() {
        SlitherlinkSolver solver = new SlitherlinkSolver();
        GameField gameField = new GameField(exampleFieldWithConnections);
        assertFalse(solver.checkConnectionsPoint(gameField, new Point(0, 6)), "2 lines already connected in that point");
        assertFalse(solver.checkConnectionsPoint(gameField, new Point(0, 8)), "2 lines already connected in that point");
        assertTrue(solver.checkConnectionsPoint(gameField, new Point(0, 4)), "All ok, there are less than 2 connections to that point");
        assertTrue(solver.checkConnectionsPoint(gameField, new Point(0, 2)), "All ok, there are less than 2 connections to that point");
    }

    @Test
    void checkICanGoTHere() {
        SlitherlinkSolver solver = new SlitherlinkSolver();
        GameField gameField = new GameField(exampleFieldWithConnections);
        //on point 2/4 --> check different options
        assertTrue(solver.checkICanGoTHere(gameField, new Point(2, 3)), "theoretically i can go up");
        assertTrue(solver.checkICanGoTHere(gameField, new Point(1, 4)), "theoretically i can go left");
        assertFalse(solver.checkICanGoTHere(gameField, new Point(2, 5)), "i can't go down");
        assertFalse(solver.checkICanGoTHere(gameField, new Point(3, 4)), "i can't go right");
    }

    @Test
    void countConnectionsOnPoint() {
        SlitherlinkSolver solver = new SlitherlinkSolver();
        GameField gameField = new GameField(exampleFieldWithConnections);
        assertEquals(0, solver.countConnectionsOnPoint(gameField, new Point(1, 1)), "In this example the chosen Point has 0 connections");
        assertEquals(2, solver.countConnectionsOnPoint(gameField, new Point(1, 5)), "In this example the chosen Point has 2 connections");
        assertEquals(3, solver.countConnectionsOnPoint(gameField, new Point(1, 7)), "In this example the chosen Point has 3 connections");
    }

    @Test
    void checkFinished() {
        SlitherlinkSolver solver = new SlitherlinkSolver();
        solver.startingPoint = new Point(2, 2);
        GameField gameField = new GameField(exampleField);
        assertFalse(solver.checkFinished(gameField, new Point(2, 2)), "Should return false because puzzle isn't solved");

        //now solve it with logic
        solver.solution(gameField);
        assertTrue(solver.checkFinished(gameField, new Point(2, 2)), "Should return true, because puzzle is solved");


        //manual typing the solution, then check it
        char[][] finishedField = {
                {'.', ' ', '.', ' ', '.', '-', '.', '-', '.',},
                {' ', '1', ' ', '2', '|', '2', ' ', '3', '|',},
                {'.', '-', '.', '-', '.', ' ', '.', '-', '.',},
                {'|', ' ', ' ', '1', ' ', '1', '|', '3', ' ',},
                {'.', '-', '.', ' ', '.', ' ', '.', '-', '.',},
                {' ', '3', '|', '1', ' ', '1', ' ', '3', '|',},
                {'.', '-', '.', ' ', '.', '-', '.', '-', '.',},
                {'|', '3', ' ', '2', '|', '2', ' ', ' ', ' ',},
                {'.', '-', '.', '-', '.', ' ', '.', ' ', '.',}
        };
        gameField = new GameField(finishedField);
        assertTrue(solver.checkFinished(gameField, new Point(2, 2)), "Should return true, because puzzle is solved");
    }

    @Test
    void getStartingPoint() {
        SlitherlinkSolver solver = new SlitherlinkSolver();
        GameField gamefield = new GameField(exampleField);

        Point[] calculatedStartingPoints = solver.getStartingPoint(gamefield);
        assertEquals(2, calculatedStartingPoints.length, "Should find 2 starting Points in the example field");
        assertEquals(2, calculatedStartingPoints[0].getX(), "First starting point in the example should be 2/2");
        assertEquals(2, calculatedStartingPoints[0].getY(), "First starting point in the example should be 2/2");
        assertEquals(0, calculatedStartingPoints[1].getX(), "First starting point in the example should be 0/0");
        assertEquals(0, calculatedStartingPoints[1].getY(), "First starting point in the example should be 0/0");


        //can't find starting point

        char[][] noStartingPoint = {
                {'.', ' ', '.', ' ', '.',},
                {' ', '0', ' ', '0', ' ',},
                {'.', ' ', '.', ' ', '.',},
                {' ', '0', ' ', '0', ' ',},
                {'.', ' ', '.', ' ', '.',},};

        gamefield = new GameField(noStartingPoint);
        assertNull(solver.getStartingPoint(gamefield), "in this example no starting point");
    }

}