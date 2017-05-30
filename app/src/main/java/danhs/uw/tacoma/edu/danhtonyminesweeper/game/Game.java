package danhs.uw.tacoma.edu.danhtonyminesweeper.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Game {

    private int length;
    private int width;
    private double probability;

    private boolean[][] mine_map;
    private int[][] solution;

    private String[][] playing_board;
    public final String HIDDEN = "X";
    public final String MINE = "*";
    public final String EMPTY = ".";
    public final String FLAG = "O";

    private Set<CoordPair> mineList = new HashSet<>();//list of mine coordinates
    private Set<CoordPair> flagList = new HashSet<>();//list of flag coordinates
    private Set<CoordPair> hiddenList = new HashSet<>();//list of flag coordinates

    private boolean Flag_Mode = false;
    private boolean GAME_OVER = false;
    boolean WIN = false;

    /**
     * Public Constructor to create a new Minesweeper Game
     * @param length, the length of the board
     * @param width, the width of the board
     * @param probability, the probability that any 'cell' in a board is a mine.
     */
    public Game(int length, int width, double probability){
        setLength(length);
        setWidth(width);
        setProbability(probability);
        NewGame();

    }

    /**
     * Constructor to create a new game from a pre-created unplayed game.
     * @param mine_map
     * @param solution
     */
    public Game(boolean[][] mine_map, int[][] solution,int length, int width , double probability){

        setLength(length);
        setWidth(width);
        setProbability(probability);

        this.mine_map = mine_map;
        makeMineList();

        this.solution = solution;

        playing_board = new String[length][width];
        for (int i = 0; i < length; i++){
            for (int j = 0; j < width; j++) playing_board[i][j] = HIDDEN;
        }

    }

    /**
     * Creates a new game.
     */
    public void NewGame(){
        Flag_Mode = false;
        //lists reset to be empty
        mineList = new HashSet<>();
        flagList = new HashSet<>();

        generateBoard();
        makeMineList();
        GAME_OVER = false;
        WIN = false;
    }

    /**
     * initializes and creates the mine_map, the solution map, and the playing board.
     */
    private void generateBoard(){
        mine_map = new boolean[length][width];
        playing_board = new String[length][width];
        for (int i = 0; i < length; i++){
            for (int j = 0; j < width; j++){
                playing_board[i][j] = HIDDEN;
                mine_map[i][j] = (Math.random() < probability);
            }
        }
        makeMineList();
        solution = new int[length][width];
        for (int i = 0; i < length; i++){
            for (int j = 0; j < width; j++) checkMines(i, j);
        }

    }

    /**
     * Private helper method to populate Solution Board.
     * Cell with given X,Y coordinates is given a number indicating how many adjacent mines there are.
     *
     * @param x coordinate of cell
     * @param y coordinate of cell
     */
    private void checkMines(int x, int y){
        solution[x][y] = 0;
        //System.out.println("(" + x + ", "+ y +  ")");
        for(int xx = x-1; xx <= x+1; xx++){
            for(int yy = y-1; yy <= y+1; yy++){
                if(inbounds(xx, yy)){
                    if (mine_map[xx][yy]) solution[x][y]++;
                }
            }
        }
        if(solution[x][y] == 0) solution[x][y] = -1;
    }

    /**
     * Private helper method to populate MineList with Mine Coordinates.
     */
    private void makeMineList(){
        mineList = new HashSet<>();
        for (int i = 0; i < length; i++){
            for (int j = 0; j < width; j++){
                if(mine_map[i][j]) mineList.add(new CoordPair(j, i));
            }
        }
    }

    /**
     * Checks if X and Y coordinates are inbounds
     * @param x coordinate of cell
     * @param y coordinate of cell
     * @return true if in bounds, else false if not
     */
    private boolean inbounds(int x, int y){
        return !(x < 0 || x >= length || y < 0 || y >= width);

    }

    /**
     * Method to tap and reveal cell given x, y coordinates
     *
     * First Tap of the Game will ALWAYS be Empty
     *
     * IF cell is mine then the player has lost and it is game over,
     * ELSE recursively reveal cells
     *
     * @param x coordinate of cell
     * @param y coordinate of cell
     */
    public void tapCell(int x, int y){
        int myX = x-1;
        int myY = y-1;

        if (mine_map[myY][myX]){
            playing_board[myY][myX] = MINE;
            WIN = true;
            GAME_OVER = true;
            //System.out.println("YOU HIT A MINE");
        }
        else if(playing_board[myY][myX] != FLAG){
            tapCell2(x, y);
        }
        updateGame();

    }

    /**
     * Flood Fill Recursion Algorithm to reveal all cells recursively:
     *
     * CASE 1: if the coordinates of the cell are not in bounds end recursion,
     * CASE 2: if the cell is a mine end the recursion,
     * CASE 3: if the cell is already revealed end the recursion,
     * CASE 4: if the cell is empty but has mines adjacent to it, mark it on the playing board and end recursion
     *
     * Recursive Case: mark cell on playing board as revealed and do recursion on adjacent cells
     *
     * @param x coordinate of cell
     * @param y coordinate of cell
     */
    private void tapCell2(int x, int y){
        int myX = x-1;
        int myY = y-1;
        //System.out.println(myX + "," + myY);
        //Base Cases
        if (!inbounds(myX,myY)){
            //System.out.println("inbounds");
            return;
        }
        if (mine_map[myY][myX]){
            //System.out.println("mine");
            return;
        }
        if (playing_board[myY][myX] != HIDDEN || playing_board[myY][myX] == FLAG){
            //System.out.println("not hidden");
            return;
        }

        if (solution[myY][myX] > 0){
            //System.out.println("number");
            playing_board[myY][myX]= Integer.toString(solution[myX][myY]);
            return;
        }
        //Recursive Case - populate playingboard
        else{
            playing_board[myY][myX] = EMPTY;
            //System.out.println("RECURSE");
            tapCell2(myX+1, myY);
            tapCell2(myX-1, myY);
            tapCell2(myX, myY+1);
            tapCell2(myX, myY-1);

            tapCell2(myX+1, myY+1);
            tapCell2(myX-1, myY-1);
            tapCell2(myX-1, myY+1);
            tapCell2(myX+1, myY-1);
        }
    }

    public void flagCell(int x, int y){
        int myX = x-1;
        int myY = y-1;
        if (inbounds(myX,myY)){
            //System.out.println(myX + " " + myY);
            playing_board[myY][myX] = FLAG;
            flagList.add(new CoordPair(myX, myY));
        }
        updateGame();
    }
    public void removeFlag(int x, int y){
        int myX = x-1;
        int myY = y-1;
        if(inbounds(myX, myY)){
            CoordPair temp = new CoordPair(myX,myY);
            for(CoordPair c: flagList){
                if(temp.equals(c)){
                    flagList.remove(c);
                    playing_board[myY][myX] = HIDDEN;
                }
            }
        }
        updateGame();
    }



    /**
     * Private Method checks if the game has won the game or not.
     * 1) check if all mines are flagged.
     * 2) check if all cells except the mines are revealed.
     */
    private void updateGame(){
        //1) confirmation count, if X out of X Mine CoordPairs are flagged then the game is won
        int counter = 0;
        for(CoordPair m: mineList){
            for(CoordPair f: flagList){
                if(f.equals(m)) counter++;
            }
        }


        if (counter == mineList.size() && flagList.size() == mineList.size()||
                (mineList.containsAll(hiddenList) && mineList.size() == hiddenList.size())
                || GAME_OVER) System.out.println("GAME OVER! You win!");

    }


    /**
     * prints Mine Map and Solution to Console
     */
    public void printBoard(){
        // print game
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++){
                if (mine_map[i][j]) System.out.print(MINE+ " ");
                else System.out.print(EMPTY + " ");
            }
            System.out.println();
        }

        // print solution
        System.out.println();
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                if (mine_map[i][j]) System.out.print(MINE+ " ");
                else             System.out.print(solution[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("=====================");
    }
    /**
     * Prints Playing Board to Console
     */
    public void printPlayingBoard(){
        System.out.println();
        for (int i = 0; i < length; i++){
            for (int j = 0; j < width; j++){
                System.out.print(playing_board[i][j]+ " ");
            }
            System.out.println();
        }
    }
    public void printLists(){
        System.out.print("Mines:");
        for(CoordPair c: mineList) System.out.print("(" + c.toString() + "), ");
        System.out.print("Flags:");
        for(CoordPair c: flagList) System.out.print("(" + c.toString() + "), ");
    }

    public void setToggle(boolean toggle){
        Flag_Mode = toggle;
    }

    public boolean getToggle(){
        return Flag_Mode;
    }

    /**
     * getter for Length
     * @returns Length
     */
    public int getLength() {
        return length;
    }
    /**
     * setter for Length
     */
    public void setLength(int length) {
        this.length = length;
    }
    /**
     * getter for Width
     * @returns Width
     */
    public int getWidth() {
        return this.width;
    }
    /**
     * setter for Width
     */
    public void setWidth(int width) {
        this.width = width;
    }
    /**
     * getter for Probability
     * @returns Probability
     */
    public double getProbability() {
        return this.probability;
    }
    /**
     * setter for Probability
     */
    public void setProbability(double probability) {
        this.probability = probability;
    }

    public String[][] getPlaying_board(){
        return this.playing_board;
    }
    public int[][] getSolution(){
        return this.solution;
    }


    public class CoordPair {
        private final int x, y;
        public CoordPair(int x, int y) {
            this.x=x;this.y=y;
        }
        public int getX(){
            return x;
        }
        public int getY(){
            return y;
        }

        @Override
        public boolean equals(Object cp){
            CoordPair cp2 = (CoordPair) cp;
            return (this.getX() == cp2.getX() && this.getY() == cp2.getY());
        }
        public String toString(){
            return getX() + ", " + getY();
        }
    }

    /**
     * Main function to test Minesweeper
     * @param args
     */
    public static void main(String[] args) {
        boolean[][] m = {
                {true, false, false},
                {false, false, false},
                {true, false, false},
        };

        int[][] s = {
                {0, 1, 0},
                {1, 1, 0},
                {0, 1, 0},
        };

        Game game = new Game(m, s, 3, 3, 0.5);
        game.printBoard();
        //game.printPlayingBoard();
        game.flagCell(1, 1);
        //game.flagCell(2,2);
        //game.removeFlag(1, 1);
        game.flagCell(1, 3);
        game.printLists();

        game.printPlayingBoard();
        game.tapCell(3,2);
        game.printPlayingBoard();
        //game.tapCell(2, 3);
        //game.printPlayingBoard();


    }

}
