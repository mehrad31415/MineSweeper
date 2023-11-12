package pck3;

import javax.swing.*;
import java.awt.*;
// these are GUI parts. The methods are very similar to the original project just the methods related to the GUI are brought to this class.
public class MinesweeperGUI {
    JFrame frame = new JFrame("Minesweeper");
    JButton reset = new JButton("Reset");
    JButton solve = new JButton("Solve");
    JToggleButton[][] buttons;
    Container grid = new Container();
    
    public MinesweeperGUI(JToggleButton[][] buttons) {
        this.buttons = buttons;
        frame.setSize(900, 900);
        frame.setLayout(new BorderLayout());
        frame.add(reset, BorderLayout.NORTH);
        frame.add(solve, BorderLayout.SOUTH);
        grid.setLayout(new GridLayout(MineSweeperMainRefactored.grid_size, MineSweeperMainRefactored.grid_size));
        frame.add(grid, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void setButtonText(int x, int y, String text) {
        buttons[x][y].setText(text);
    }

    public void setHandler(MinesweeperHandler handler) {
        reset.addActionListener(handler);
        solve.addActionListener(handler);
        for (int r = 0; r < buttons.length; r++){
            for (int c = 0; c < buttons[0].length; c++) {
                buttons[r][c] = new JToggleButton();
                buttons[r][c].addActionListener(handler);
                grid.add(buttons[r][c]);
                buttons[r][c].setSize(frame.getWidth() / MineSweeperMainRefactored.grid_size, frame.getHeight() / MineSweeperMainRefactored.grid_size+2);
            }
        }
    }
}
