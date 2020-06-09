package sample;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameFieldTest {

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

    GameField gamefield = new GameField();
    JSONObject jasonObject = new JSONObject("{\n" +
            "\t\"spaceForLines\": false,\n" +
            "\t\"gameFieldWidth\": 4,\n" +
            "\t\"gameFieldHeight\": 4,\n" +
            "\t\"Points\":[\n" +
            "\t{\n" +
            "\t\t\"x\":0,\n" +
            "\t\t\"y\":0,\n" +
            "\t\t\"n\":1\n" +
            "\t}, \n" +
            "\t{\n" +
            "\t\t\"x\":0,\n" +
            "\t\t\"y\":2,\n" +
            "\t\t\"n\":3\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"x\":0,\n" +
            "\t\t\"y\":3,\n" +
            "\t\t\"n\":3\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"x\":1,\n" +
            "\t\t\"y\":0,\n" +
            "\t\t\"n\": 2\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"x\":1,\n" +
            "\t\t\"y\":1,\n" +
            "\t\t\"n\":1\n" +
            "\t}, \n" +
            "\t{\n" +
            "\t\t\"x\":1,\n" +
            "\t\t\"y\":2,\n" +
            "\t\t\"n\":1\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"x\":1,\n" +
            "\t\t\"y\":3,\n" +
            "\t\t\"n\":2\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"x\":2,\n" +
            "\t\t\"y\":0,\n" +
            "\t\t\"n\": 2\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"x\":2,\n" +
            "\t\t\"y\":1,\n" +
            "\t\t\"n\":1\n" +
            "\t}, \n" +
            "\t{\n" +
            "\t\t\"x\":2,\n" +
            "\t\t\"y\":2,\n" +
            "\t\t\"n\":1\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"x\":2,\n" +
            "\t\t\"y\":3,\n" +
            "\t\t\"n\":2\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"x\":3,\n" +
            "\t\t\"y\":0,\n" +
            "\t\t\"n\":3\n" +
            "\t}, \n" +
            "\t{\n" +
            "\t\t\"x\":3,\n" +
            "\t\t\"y\":1,\n" +
            "\t\t\"n\":3\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"x\":3,\n" +
            "\t\t\"y\":2,\n" +
            "\t\t\"n\":3\n" +
            "\t}\t\t\n" +
            "\t]\t\n" +
            "}");
    JSONObject jasonObjectError = new JSONObject("{\n" +
            "\t\"spaceForLines\": false,\n" +
            "\t\"gameFieldWidth\": 4,\n" +
            "\t\"gameFieldheighththhtht\": 4,\n" +
            "\t\"Points\":[\n" +
            "\t{\n" +
            "\t\t\"x\":0,\n" +
            "\t\t\"y\":0,\n" +
            "\t\t\"n\":1\n" +
            "\t}]}");

    @Test
        //addSolutionStep, stepCollectionEmpty, getNextStep
    void SolutionSteps() {
        //Testing with one elements
        assertTrue(gamefield.stepCollectionEmpty(), "Collection should be empty");
        gamefield.addSolutionStep(new Point(0, 1));
        assertFalse(gamefield.stepCollectionEmpty(), "Collection should now be filled");
        Point p = gamefield.getNextStep();
        assertEquals(0, p.X, "Testing for x coordinate");
        assertEquals(1, p.Y, "Testing for y coordinate");
        assertTrue(gamefield.stepCollectionEmpty(), "Collection should now be empty");

        //Testing with many elements
        assertTrue(gamefield.stepCollectionEmpty(), "Collection should be empty");
        gamefield.addSolutionStep(new Point(0, 1));
        gamefield.addSolutionStep(new Point(2, 3));
        gamefield.addSolutionStep(new Point(4, 5));
        assertFalse(gamefield.stepCollectionEmpty(), "Collection should now be filled");
        p = gamefield.getNextStep();
        assertEquals(4, p.X, "Testing for x coordinate");
        assertEquals(5, p.Y, "Testing for y coordinate");
        assertFalse(gamefield.stepCollectionEmpty(), "Collection should not be empty");
        p = gamefield.getNextStep();
        assertEquals(2, p.X, "Testing for x coordinate");
        assertEquals(3, p.Y, "Testing for y coordinate");
        assertFalse(gamefield.stepCollectionEmpty(), "Collection should not be empty");
        p = gamefield.getNextStep();
        assertEquals(0, p.X, "Testing for x coordinate");
        assertEquals(1, p.Y, "Testing for y coordinate");
        assertTrue(gamefield.stepCollectionEmpty(), "Collection should now be empty");

    }

    @Test
    void getCharFromPoint() {
        gamefield = new GameField(exampleField);

        assertEquals('1', gamefield.getCharFromPoint(1, 1), "Should return the char at the correct position");
        assertEquals('1', gamefield.getCharFromPoint(new Point(1, 1)), "Should return the char at the correct position with Point Object");

        assertEquals(' ', gamefield.getCharFromPoint(1, 2), "Should return the char at the correct position");
        assertEquals(' ', gamefield.getCharFromPoint(new Point(1, 2)), "Should return the char at the correct position with Point Object");

        assertEquals('3', gamefield.getCharFromPoint(1, 5), "Should return the char at the correct position");
        assertEquals('3', gamefield.getCharFromPoint(new Point(1, 5)), "Should return the char at the correct position with Point Object");

        assertEquals('2', gamefield.getCharFromPoint(3, 1), "Should return the char at the correct position");
        assertEquals('2', gamefield.getCharFromPoint(new Point(3, 1)), "Should return the char at the correct position with Point Object");
    }

    @Test
    void setCharToField() {
        gamefield = new GameField(exampleField);

        Point p = new Point(1, 2);
        assertNotEquals(GameField.horizontalChar, gamefield.getCharFromPoint(p), "A horizontal line ist not there");
        gamefield.setCharToField(p, GameField.horizontalChar);
        assertEquals(GameField.horizontalChar, gamefield.getCharFromPoint(p), "A horizontal line should now be there");

        p = new Point(2, 1);
        assertNotEquals(GameField.verticalChar, gamefield.getCharFromPoint(p), "A vertical line ist not there");
        gamefield.setCharToField(p, GameField.verticalChar);
        assertEquals(GameField.verticalChar, gamefield.getCharFromPoint(p), "A vertical line should now be there");
    }

    @Test
    void resetLine() {
        gamefield = new GameField(exampleField);

        Point p = new Point(1, 2);
        gamefield.setCharToField(p, GameField.horizontalChar);
        assertEquals(GameField.horizontalChar, gamefield.getCharFromPoint(p), "A horizontal line should now be there");

        //actual test for this method
        gamefield.resetLine(p);
        assertEquals(' ', gamefield.getCharFromPoint(p), "Connection should now be empty (reset)");
    }

    @Test
    void getYSize() {
        gamefield = new GameField(exampleField);

        assertEquals(9, gamefield.getYSize(), "This example data has a length of 9 in Y");


        //Second test
        char[][] exampleField2 = {
                {'.', ' ', '.', ' ', '.',},
                {' ', '1', ' ', '2', ' ',},
                {'.', ' ', '.', ' ', '.',},
                {' ', ' ', ' ', '1', ' ',},
                {'.', ' ', '.', ' ', '.',}};
        gamefield = new GameField(exampleField2);

        assertEquals(5, gamefield.getYSize(), "This example data has a length of 5 in Y");
    }

    @Test
    void getXSize() {
        gamefield = new GameField(exampleField);

        assertEquals(9, gamefield.getXSize(), "This example data has a length of 9 in X");


        //Second Test
        char[][] exampleField2 = {
                {'.', ' ', '.', ' ', '.',},
                {' ', '1', ' ', '2', ' ',},
                {'.', ' ', '.', ' ', '.',},
                {' ', ' ', ' ', '1', ' ',},
                {'.', ' ', '.', ' ', '.',}};
        gamefield = new GameField(exampleField2);

        assertEquals(5, gamefield.getXSize(), "This example data has a length of 5 in X");
    }

    @Test
    void parseJSON() {
        gamefield = new GameField();
        //gameField is empty now

        assertTrue(gamefield.parseJSON(jasonObject), "Should return true, because valid json input");
        assertEquals('1', gamefield.getCharFromPoint(new Point(1, 1)), "Should now have the value from the json");
        assertEquals(9, gamefield.getXSize(), "Size should now be adjusted to the json");
        assertEquals(9, gamefield.getYSize(), "Size should now be adjusted to the json");


        assertFalse(gamefield.parseJSON(jasonObjectError), "Should return false, because invalid json index");
    }

    @Test
    void formatField() {
        gamefield = new GameField(exampleField);
        //gameField has now '.' ant not '+'. After formatting it has '+' as connection Point
        Point p = new Point(0, 0);
        assertNotEquals(GameField.connectionPoint, gamefield.getCharFromPoint(p), "actual gameField should before not contain the connectionPoint char");
        gamefield.formatField();
        assertEquals(GameField.connectionPoint, gamefield.getCharFromPoint(p), "actual gameField should now contain the connectionPoint char");
    }
}