package sample;

public class GameField {
    //symbols used internal for the fieldData Array
    static char horizontalChar = '-';
    static char verticalChar = '|';
    static char connectionPoint = '+';

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
    public char getCharFromPoint(Point p){
        return fieldData[p.Y][p.X];
    }
    public char getCharFromPoint(int x, int y){
        return fieldData[y][x];
    }
    public void setLine(Point p, char c){
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
        System.out.println("OOOOOOOOOOOOOOOOOOOOOOO");
        for (int y = 0; y< fieldData.length; y++){
            for (int x = 0; x < fieldData[y].length; x++){
                System.out.print(getCharFromPoint(x,y));
            }
            System.out.println("");
        }
        System.out.println("OOOOOOOOOOOOOOOOOOOOOOO");
    }

    public void transferField(char[][] fieldData){
        ///123 56 7
        //mit Leerzeile
        //Mit Punkten
        // als string mit Punkten / Leerzeilen


        this.fieldData = fieldData;
        formatField();
    }
    public void formatField(){
        printToConsole();
        for (int y = 0; y< fieldData.length; y+=2){
            for (int x = 0; x < fieldData[y].length; x+=2){
                fieldData[y][x]= connectionPoint;
            }
        }
        printToConsole();
    }
}
