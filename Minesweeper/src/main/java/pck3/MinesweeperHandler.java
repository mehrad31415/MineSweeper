package pck3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
// this is the event handler class.
// The methods are very similar to the original project just the methods related to the handler are brought to this class.
public class MinesweeperHandler implements ActionListener {
    // a handler takes the GUI and and logic into account.
    private MinesweeperLogic logic;
    private MinesweeperGUI gui;

    public MinesweeperHandler(MinesweeperLogic logic, MinesweeperGUI gui) {
        this.logic = logic;
        this.gui = gui;
    }

    // again this is broken down just like pck2 but with additional changes to accomodate for the splitting of the classes.
    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource().equals(gui.reset)) {
            resetGame();
        }
        else if (event.getSource().equals(gui.solve)) {
            solveGame();
        }
        else {
            handleTileClick(event);
        }
    }
    // these are exactly the same as pck2
    private void resetGame() {
        for (int r = 0; r < gui.buttons.length; r++) {
            for (int c = 0; c < gui.buttons[0].length; c++) {
                gui.buttons[r][c].setEnabled(true);
                gui.buttons[r][c].setSelected(false);
                gui.buttons[r][c].setText("");
            }
        }
        logic.canSolve = false;
        logic.marked.clear();
        logic.addRandomMines();
    }
    
    private void solveGame() {
        while (logic.solveGame())
            logic.canSolve = false;
        if (logic.canSolve || logic.checkWin()) {
            JOptionPane.showMessageDialog(gui.frame, "The computer wins!");
            disableAllButtons();
        }
        else if (!logic.canSolve) {
            JOptionPane.showMessageDialog(gui.frame, "Uh oh, you are gonna have to guess!");
        }
    }

    private void handleTileClick(ActionEvent event) {
        for (int r = 0; r < gui.buttons.length; r++) {
            for (int c = 0; c < gui.buttons[0].length; c++) {
                if (event.getSource().equals(gui.buttons[r][c])) {
                    processTileClick(r, c);
                    return; // Early return to avoid unnecessary looping
                }
            }
        }
    }

    private void processTileClick(int r, int c) {
        if (logic.counts[r][c] == 0) {
            logic.clearEmpty(r, c);
        }
        logic.showTile(r, c);
        if (logic.checkWin()) {
            JOptionPane.showMessageDialog(gui.frame, "YOU WIN!");
        } else if (logic.checkLose()) {
            JOptionPane.showMessageDialog(gui.frame, "You lose!");
        }
    }

    private void disableAllButtons() {
        for (int x = 0; x < gui.buttons.length; x++) {
            for (int y = 0; y < gui.buttons[0].length; y++) {
                gui.buttons[x][y].setEnabled(false);
            }
        }
    }
}
