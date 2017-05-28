package danhs.uw.tacoma.edu.danhtonyminesweeper.game;

import java.util.ArrayList;

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
    final String FLAG = "O";

    private ArrayList<CoordPair> mineList = new ArrayList<>();//list of mine coordinates
    private ArrayList<CoordPair> flagList = new ArrayList<>();//list of flag coordinates
    private ArrayList<CoordPair> exploreList = new ArrayList<>();//list of explored cell coordinates

    private boolean First_Tap_of_Game;
    private boolean GAME_OVER;

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
     * Constructor to create a new game from a pre-created played game.
     * @param mine_map
     * @param solution
     * @param playing_board
     */
    public Game(boolean[][] mine_map, int[][] solution, String[][] playing_board, int length, int width, double probability){

        setLength(length);
        setWidth(width);
        setProbability(probability);

        First_Tap_of_Game = true;
        GAME_OVER = false;

        this.mine_map = mine_map;
        makeMineList();

        this.solution = solution;
        this.playing_board = playing_board;

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

        First_Tap_of_Game = true;
        GAME_OVER = false;

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
        First_Tap_of_Game = true;
        GAME_OVER = false;

        //lists reset to be empty
        mineList = new ArrayList<>();
        flagList = new ArrayList<>();
        exploreList = new ArrayList<>();
        generateBoard();

    }

    /**
     * initializes and creates the mine_map, the solution map, and the playing board.
     */
    private void generateBoard(){
        mine_map = new boolean[length][width];
        for (int i = 0; i < length; i++){
            for (int j = 0; j < width; j++) mine_map[i][j] = (Math.random() < probability);
        }
        makeMineList();
        solution = new int[length][width];
        for (int i = 0; i < length; i++){
            for (int j = 0; j < width; j++) checkMines(i, j);
        }

        // the playing board displayed to the player, initialized as an entirely hidden board
        playing_board = new String[length][width];
        for (int i = 0; i < length; i++){
            for (int j = 0; j < width; j++) playing_board[i][j] = HIDDEN;
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
    }

    /**
     * Private helper method to populate MineList with Mine Coordinates.
     */
    private void makeMineList(){
        mineList = new ArrayList<>();
        for (int i = 0; i < length; i++){
            for (int j = 0; j < width; j++){
                if(mine_map[i][j]) mineList.add(new CoordPair(i, j));
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

        //Special Case, the First Cell to be tapped is ALWAYS NEVER a mine
        if(First_Tap_of_Game && mine_map[myX][myY]){
            while(mine_map[myX][myY]) generateBoard();//keeps generating a new board until one exists where first cell is empty
            exploreList.add(new CoordPair(myX, myY));
            playing_board[myX][myY] = EMPTY;
        }
        First_Tap_of_Game = false;

        if (mine_map[myX][myY]){
            GAME_OVER = true;
        }
        else tapCell2(x, y);
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

        //Base Cases
        if (!inbounds(myX,myY)){
            //System.out.println("inbounds");
            return;
        }

        if (mine_map[myX][myY]){
            //System.out.println("mine");
            return;
        }
        if (playing_board[myX][myY] != HIDDEN){
            //System.out.println("hidden");
            return;
        }
        if (solution[myX][myY] != 0 && !First_Tap_of_Game){
            playing_board[myX][myY] = Integer.toString(solution[myX][myY]);
            exploreList.add(new CoordPair(myX, myY));
            return;
        }

        //Recursive Case - populate playingboard
        else{
            playing_board[myX][myY] = EMPTY;
            exploreList.add(new CoordPair(myX, myY));
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
            flagList.add(new CoordPair(myX, myY));
        }
        updateGame();
    }
    public void removeFlag(int x, int y){
        int myX = x-1;
        int myY = y-1;
        if(inbounds(myX, myY)){
            CoordPair temp = new CoordPair(myX,myY);
            for(CoordPair f : flagList){
                if(temp.equals(f)){
                    System.out.println("FOUND");
                    flagList.remove(f);
                    break;
                }
            }
        }

    }


    /**
     * Private Method checks if the game has won the game or not.
     * 1) check if all mines are flagged.
     * 2) check if all cells except the mines are revealed.
     */
    private void updateGame(){
        //1) confirmation count, if X out of X Mine CoordPairs are flagged then the game is won
        int confirm = 0;
        for(CoordPair m : mineList){
            for(CoordPair f: flagList){
                if(m.equals(f)){
                    confirm++;
                }

            }
        }
        if (confirm == mineList.size() || GAME_OVER) System.out.println("GAME OVER! You win!");

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
        public boolean equals(CoordPair cp){
            return (this.getX() == cp.getX() && this.getY() == cp.getY());
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
        game.printPlayingBoard();
        game.flagCell(1, 1);
        game.removeFlag(1, 1);
        game.flagCell(1, 3);
        game.tapCell(2, 3);
        game.printPlayingBoard();
        //game.tapCell(1, 1);
        game.printPlayingBoard();

        //nuGame.tapCell(9, 9);
        //nuGame.printPlayingBoard();

    }

}
