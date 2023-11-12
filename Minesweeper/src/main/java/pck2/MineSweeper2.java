package pck2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mitchell Sayer on 5/9/15. Refactored bY Mehrad Haghshenas 12 November 2023.
 */
public class MineSweeper2 implements ActionListener {
    
    int grid_size = 20;
    JFrame frame = new JFrame("Minesweeper");
    JButton reset = new JButton("Reset");
    JButton solve = new JButton("Solve");
    JToggleButton[][] buttons = new JToggleButton[grid_size][grid_size];
    int[][] counts = new int [grid_size][grid_size];
    Container grid = new Container();
    boolean lost = false;
    boolean firstLost = true;
    boolean canSolve = false;
    int mineCount = 30;
    static final int MINE = 10;
    Set<Integer> marked = new HashSet<>();

    public static void main(String[] args) {
        // the main method was changed.
        // the experiment is run on the following grid sizes.
        int[] gridSizes = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        int runsPerSize = 10; // for each gridsize the experiment is run 10 times.
        // we run the experiment for each size in the following loop.
        for (int size : gridSizes) {
            // the memory and the time is initialized.
            long totalMemory = 0;
            long totalTime = 0;
            // we run the experiment for each grid size 10 times in the following loop.
            for (int i = 0; i < runsPerSize; i++) {
                // runTest returns an array where the first element is the memory for that experiment and and the second is the time.
                long[] result = runTest(size);
                totalMemory += result[0];
                totalTime += result[1];
            }
            // for each experiment the time and memory is averaged.
            long averageMemory = totalMemory / runsPerSize;
            long averageTime = totalTime / runsPerSize;

            System.out.println("Grid Size: " + size + ", Average Memory: " + averageMemory + " bytes, Average Time: " + averageTime + " ms");
        }
    }
    
    // added by me
    // for each experiment, this method takes the grid size and returns the the time and memory it takes for the game to startup.
    private static long[] runTest(int gridSize) {
        long startTime = System.currentTimeMillis();
        Runtime runtime = Runtime.getRuntime();
        // before doing the runtime we do garbage colection. ALthough this is automatically done in java, we do it in case there are any objects left from the previous experiment.
        runtime.gc();
        // the memory before the experiment is calculated.
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();

        new MineSweeper2(gridSize);
        
        // the memory after the startup is calculated.
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long endTime = System.currentTimeMillis();
        
        // the amount of difference between the memories are the amount that the startup of the game has used.
        long memoryUsed = memoryAfter - memoryBefore;
        // difference between start time and end time is the duration.
        long duration = endTime - startTime;

        return new long[]{memoryUsed, duration};
    }

    // majorly it is kept as it is same as the original version.
    public MineSweeper2(int size)
    {
        this.grid_size = size;
        frame.setSize(900, 900);
        frame.setLayout(new BorderLayout());
        frame.add(reset, BorderLayout.NORTH);
        frame.add(solve, BorderLayout.SOUTH);
        reset.addActionListener(this);
        solve.addActionListener(this);
        grid.setLayout(new GridLayout(grid_size, grid_size));
        for (int r = 0; r < buttons.length; r++)
            for (int c = 0; c < buttons[0].length; c++) {
                buttons[r][c] = new JToggleButton();
                buttons[r][c].addActionListener(this);
                grid.add(buttons[r][c]);
                buttons[r][c].setSize(frame.getWidth() / grid_size, frame.getHeight() / grid_size+2);
            }
        frame.add(grid,BorderLayout.CENTER);
        addRandomMines();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // this method was broken down to decrease its cyclomatic complexity.
    public void addRandomMines() {
        // we could have changed the mineList to be a set as well just as we did for marked.
        // initializeMineList is a method we wrote to break down the code. This is the inialization of the mineList.
        ArrayList<Integer> mineList = initializeMineList();
        // instead of writing all the statements under this method we have separated it into two other methods and called them here.
        placeMines(mineList);
        calculateAdjacentMines();
    }
    
    // method added by me.
    private ArrayList<Integer> initializeMineList() {
        ArrayList<Integer> mineList = new ArrayList<>();
        for (int x = 0; x < counts.length; x++) {
            for (int y = 0; y < counts[0].length; y++) {
                mineList.add((x * 100) + y);
            }
        }
        return mineList;
    }
    // method added by me.
    private void placeMines(ArrayList<Integer> mineList) {
        counts = new int[20][20];
        for (int i = 0; i < mineCount; i++) {
            int choice = (int) (Math.random() * mineList.size());
            int x = mineList.get(choice) / 100;
            int y = mineList.get(choice) % 100;
            counts[x][y] = MINE;
            mineList.remove(choice);
        }
    }
    // method added by me.
    private void calculateAdjacentMines() {
        for (int x = 0; x < counts.length; x++) {
            for (int y = 0; y < counts[0].length; y++) {
                if (counts[x][y] != MINE) {
                    counts[x][y] = getAdjacentMineCount(x, y);
                }
            }
        }
    }
    // method added by me. This is called in the calculateAdjacentMines method. Originally, the statements in this method was in addRandomMines.
    private int getAdjacentMineCount(int x, int y) {
        int mineCount = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue; // Skip the current cell
                int newX = x + dx;
                int newY = y + dy;
                if (newX >= 0 && newX < counts.length && newY >= 0 && newY < counts[0].length) {
                    if (counts[newX][newY] == MINE) {
                        mineCount++;
                    }
                }
            }
        }
        return mineCount;
    }
    // not changed.
    public void showTile(int r, int c)
    {
        if (counts[r][c] == 0) {
            buttons[r][c].setText("");
            buttons[r][c].setSelected(true);
        }
        else if (counts[r][c]==MINE) {
            buttons[r][c].setForeground(Color.red);
            buttons[r][c].setText("X");
            buttons[r][c].setSelected(true);
        }
        else {
            buttons[r][c].setText(counts[r][c] + "");
            if (counts[r][c]==1)
                buttons[r][c].setForeground(Color.blue);
            else if (counts[r][c]==2)
                buttons[r][c].setForeground(Color.magenta);
            else if (counts[r][c]==3)
                buttons[r][c].setForeground(Color.green);
            buttons[r][c].setSelected(true);
        }
    }
    // not changed
    public void clearEmpty(int row, int col) {
        for ( int r = row - 1; r <= row + 1; r++ ) {
            for (int c = col - 1; c <= col + 1; c++) {
                if (r >= 0 && r < counts.length && c >= 0 && c < counts[0].length) {
                    if (!buttons[r][c].isSelected()) {
                        showTile(r, c);
                        if (counts[r][c] == 0)
                            clearEmpty(r, c);
                    }
                }
            }
        }
    }
    // not changed.
    public boolean checkLose() {
        boolean won = true;
        for (int x = 0; x < buttons.length; x++) {
            for (int y = 0; y < buttons[0].length; y++){
                if (counts[x][y]==MINE&&buttons[x][y].isSelected())
                    won = false;
            }
        }
        if (!won) {
            for (int x = 0; x < buttons.length; x++) {
                for (int y = 0; y < buttons[0].length; y++){
                    buttons[x][y].setEnabled(false);
                    if (counts[x][y]==MINE) {
                        buttons[x][y].setEnabled(true);
                        showTile(x,y);
                    }
                }
            }
            return true;
        }
        else
            return false;
    }
    // not changed.
    public boolean checkWin() {
        boolean won = true;
        for (int x = 0; x < buttons.length; x++) {
            for (int y = 0; y < buttons[0].length; y++){
                if (counts[x][y]!=MINE&&!buttons[x][y].isSelected())
                    won = false;
            }
        }
        if (won&&!lost) {
            for (int x = 0; x < buttons.length; x++) {
                for (int y = 0; y < buttons[0].length; y++){
                    buttons[x][y].setEnabled(false);
                }
            }
            return true;
        }
        else
            return false;
    }
    // not changed
    public int surroundingClosed(int x, int y) {
        int count = 0;
        for ( int r = x - 1; r <= x + 1; r++ ) {
            for (int c = y - 1; c <= y + 1; c++) {
                if (r >= 0 && r < counts.length && c >= 0 && c < counts[0].length) {
                    if (!buttons[r][c].isSelected())
                        count++;
                }
            }
        }
        return count;
    }
    // not changed
    public void markItem(int x, int y, int n) {
        int count = 0;
        for ( int r = x - 1; r <= x + 1; r++ ) {
            for (int c = y - 1; c <= y + 1; c++) {
                if (r >= 0 && r < counts.length && c >= 0 && c < counts[0].length) {
                    if (!buttons[r][c].isSelected()) {
                        if (count>n)
                            return;
                        else {
                            if (!marked.contains(r*100+c)) {
                                marked.add(r * 100 + c);
                                count++;
                            }
                        }
                    }
                }
            }
        }
    }
    // not changed
    public int knownMineCount(int x,int y) {
        int count = 0;
        for ( int r = x - 1; r <= x + 1; r++ ) {
            for (int c = y - 1; c <= y + 1; c++) {
                if (r >= 0 && r < counts.length && c >= 0 && c < counts[0].length) {
                    int arrayVal = r*100+c;
                    if (marked.contains(arrayVal))
                        count++;
                }
            }
        }
        return count;
    }
    // not changed
    public int openNonMines(int x, int y) {
        int count = 0;
        for ( int r = x - 1; r <= x + 1; r++ ) {
            for (int c = y - 1; c <= y + 1; c++) {
                if (r >= 0 && r < counts.length && c >= 0 && c < counts[0].length) {
                    int arrayVal = r*100+c;
                    if (!marked.contains(arrayVal)) {
                        showTile(r,c);
                        count++;
                    }
                }
            }
        }
        return count;
    }
    // not changed
    public boolean solveGame() {
        int count = 0;
        int dif=0;
        for (int x = 0; x < counts.length; x++) {
            for (int y = 0; y < counts[0].length; y++) {
                if (buttons[x][y].isSelected()) {
                    int surround = surroundingClosed(x, y);
                    int curCount = counts[x][y];
                    int kmc = knownMineCount(x, y);
                    if (surround == curCount)
                        markItem(x, y, curCount);
                    if (surround > curCount && kmc == curCount&&!marked.contains(x*100+y)) {
                        int cOld = count;
                        count+=openNonMines(x, y);
                        dif=count-cOld;
                    }
                }
            }
        }
        if (marked.size()==30&&checkWin()) {
            canSolve = true;
            return false;
        }
        if (dif>0)
            return true;
        else
            return false;

    }
    // modified and broken down.
    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource().equals(reset)) {
            // instead of writing the reset statements we have created a method and incorporated the statements in this method.
            resetGame();
        }
        else if (event.getSource().equals(solve)) {
            // instead of writing the solve statements we have created a method and incorporated the statements in this method.
            solveGame2();
        }
        else {
            // instead of writing the handleTileClick statements we have created a method and incorporated the statements in this method.
            handleTileClick(event);
        }
    }
    // method I have added which resets the game.
    private void resetGame() {
        for (int r = 0; r < buttons.length; r++) {
            for (int c = 0; c < buttons[0].length; c++) {
                buttons[r][c].setEnabled(true);
                buttons[r][c].setSelected(false);
                buttons[r][c].setText("");
            }
        }
        canSolve = false;
        marked.clear();
        addRandomMines();
    }
    // prompts the winner when the game is solved.
    private void solveGame2() {
        while (solveGame())
            canSolve = false;
        if (canSolve || checkWin()) {
            JOptionPane.showMessageDialog(frame, "The computer wins!");
            disableAllButtons();
        }
        else if (!canSolve) {
            JOptionPane.showMessageDialog(frame, "Uh oh, you are gonna have to guess!");
        }
    }
    // added. if the user clicks a tile this method handles the event.
    private void handleTileClick(ActionEvent event) {
        for (int r = 0; r < buttons.length; r++) {
            for (int c = 0; c < buttons[0].length; c++) {
                if (event.getSource().equals(buttons[r][c])) {
                    processTileClick(r, c);
                    return; // Early return to avoid unnecessary looping
                }
            }
        }
    }
    // added. when the tile has been clicked processing the tile and neighbours are done with this method.
    private void processTileClick(int r, int c) {
        if (counts[r][c] == 0) {
            clearEmpty(r, c);
        }
        showTile(r, c);
        if (checkWin()) {
            JOptionPane.showMessageDialog(frame, "YOU WIN!");
        } else if (checkLose()) {
            JOptionPane.showMessageDialog(frame, "You lose!");
        }
    }
    // added. when the game is solved and the computer wins, we disable all of the buttons.
    private void disableAllButtons() {
        for (int x = 0; x < buttons.length; x++) {
            for (int y = 0; y < buttons[0].length; y++) {
                buttons[x][y].setEnabled(false);
            }
        }
    }
}