package pck3;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JToggleButton;
// this is the logic behind the game.The methods are very similar to the original project just the methods related to the logic are brought to this class.

public class MinesweeperLogic {

    public static final int MINE = 10;
    int[][] counts = new int[MineSweeperMainRefactored.grid_size][MineSweeperMainRefactored.grid_size];
    int mineCount = 30;
    Set<Integer> marked = new HashSet<>();
    boolean lost = false;
    boolean firstLost = true;
    boolean canSolve = false;
    // the button is actually related to the GUI but I could not fix it there.
    JToggleButton[][] buttons;

    public MinesweeperLogic(MinesweeperGUI gui, JToggleButton[][] buttons) {
        this.buttons = buttons;
        addRandomMines();
    }

    public static int getMine() {
        return MINE;
    }

    // This is broken down and refactored just like pck2.
    public void addRandomMines() {
        ArrayList<Integer> mineList = initializeMineList();
        placeMines(mineList);
        calculateAdjacentMines();
    }

    // added same as pck2
    private ArrayList<Integer> initializeMineList() {
        ArrayList<Integer> mineList = new ArrayList<>();
        for (int x = 0; x < counts.length; x++) {
            for (int y = 0; y < counts[0].length; y++) {
                mineList.add((x * 100) + y);
            }
        }
        return mineList;
    }

    // added same as pck2
    private void placeMines(ArrayList<Integer> mineList) {
        counts = new int[MineSweeperMainRefactored.grid_size][MineSweeperMainRefactored.grid_size];
        for (int i = 0; i < mineCount; i++) {
            int choice = (int) (Math.random() * mineList.size());
            int x = mineList.get(choice) / 100;
            int y = mineList.get(choice) % 100;
            counts[x][y] = MINE;
            mineList.remove(choice);
        }
    }

    // added same as pck2
    private void calculateAdjacentMines() {
        for (int x = 0; x < counts.length; x++) {
            for (int y = 0; y < counts[0].length; y++) {
                if (counts[x][y] != MINE) {
                    counts[x][y] = getAdjacentMineCount(x, y);
                }
            }
        }
    }

    // added same as pck2
    private int getAdjacentMineCount(int x, int y) {
        int mineCount = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue; // Skip the current cell
                }
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

    // same as pck1
    public boolean checkLose() {
        boolean won = true;
        for (int x = 0; x < buttons.length; x++) {
            for (int y = 0; y < buttons[0].length; y++) {
                if (counts[x][y] == MINE && buttons[x][y].isSelected()) {
                    won = false;
                }
            }
        }
        if (!won) {
            for (int x = 0; x < buttons.length; x++) {
                for (int y = 0; y < buttons[0].length; y++) {
                    buttons[x][y].setEnabled(false);
                    if (counts[x][y] == MINE) {
                        buttons[x][y].setEnabled(true);
                        showTile(x, y);
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    // same as pck1
    public boolean checkWin() {
        boolean won = true;
        for (int x = 0; x < buttons.length; x++) {
            for (int y = 0; y < buttons[0].length; y++) {
                if (counts[x][y] != MINE && !buttons[x][y].isSelected()) {
                    won = false;
                }
            }
        }
        if (won && !lost) {
            for (int x = 0; x < buttons.length; x++) {
                for (int y = 0; y < buttons[0].length; y++) {
                    buttons[x][y].setEnabled(false);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    // same as pck1
    public int surroundingClosed(int x, int y) {
        int count = 0;
        for (int r = x - 1; r <= x + 1; r++) {
            for (int c = y - 1; c <= y + 1; c++) {
                if (r >= 0 && r < counts.length && c >= 0 && c < counts[0].length) {
                    if (!buttons[r][c].isSelected()) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    // same as pck1
    public void markItem(int x, int y, int n) {
        int count = 0;
        for (int r = x - 1; r <= x + 1; r++) {
            for (int c = y - 1; c <= y + 1; c++) {
                if (r >= 0 && r < counts.length && c >= 0 && c < counts[0].length) {
                    if (!buttons[r][c].isSelected()) {
                        if (count > n) {
                            return;
                        } else {
                            if (!marked.contains(r * 100 + c)) {
                                marked.add(r * 100 + c);
                                count++;
                            }
                        }
                    }
                }
            }
        }
    }

    // same as pck1
    public int knownMineCount(int x, int y) {
        int count = 0;
        for (int r = x - 1; r <= x + 1; r++) {
            for (int c = y - 1; c <= y + 1; c++) {
                if (r >= 0 && r < counts.length && c >= 0 && c < counts[0].length) {
                    int arrayVal = r * 100 + c;
                    if (marked.contains(arrayVal)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public int openNonMines(int x, int y) {
        int count = 0;
        for (int r = x - 1; r <= x + 1; r++) {
            for (int c = y - 1; c <= y + 1; c++) {
                if (r >= 0 && r < counts.length && c >= 0 && c < counts[0].length) {
                    int arrayVal = r * 100 + c;
                    if (!marked.contains(arrayVal)) {
                        showTile(r, c);
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public boolean solveGame() {
        int count = 0;
        int dif = 0;
        for (int x = 0; x < counts.length; x++) {
            for (int y = 0; y < counts[0].length; y++) {
                if (buttons[x][y].isSelected()) {
                    int surround = surroundingClosed(x, y);
                    int curCount = counts[x][y];
                    int kmc = knownMineCount(x, y);
                    if (surround == curCount) {
                        markItem(x, y, curCount);
                    }
                    if (surround > curCount && kmc == curCount && !marked.contains(x * 100 + y)) {
                        int cOld = count;
                        count += openNonMines(x, y);
                        dif = count - cOld;
                    }
                }
            }
        }
        if (marked.size() == 30 && checkWin()) {
            canSolve = true;
            return false;
        }
        if (dif > 0) {
            return true;
        } else {
            return false;
        }

    }
    // again this should technically be part of the GUI but I could not get it working so moved it in the logic.
    public void showTile(int r, int c) {
        if (counts[r][c] == 0) {
            buttons[r][c].setText("");
            buttons[r][c].setSelected(true);
        } else if (counts[r][c] == MINE) {
            buttons[r][c].setForeground(Color.red);
            buttons[r][c].setText("X");
            buttons[r][c].setSelected(true);
        } else {
            buttons[r][c].setText(counts[r][c] + "");
            if (counts[r][c] == 1) {
                buttons[r][c].setForeground(Color.blue);
            } else if (counts[r][c] == 2) {
                buttons[r][c].setForeground(Color.magenta);
            } else if (counts[r][c] == 3) {
                buttons[r][c].setForeground(Color.green);
            }
            buttons[r][c].setSelected(true);
        }
    }

    public void clearEmpty(int row, int col) {
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                if (r >= 0 && r < counts.length && c >= 0 && c < counts[0].length) {
                    if (!buttons[r][c].isSelected()) {
                        showTile(r, c);
                        if (counts[r][c] == 0) {
                            clearEmpty(r, c);
                        }
                    }
                }
            }
        }
    }
}
