package pck3;

import javax.swing.JToggleButton;
// This version is completely restructured version of the original project.
// this class is the main class.
public class MineSweeperMainRefactored {
    static int grid_size = 20; // I could not automatically increase the grid size for this version like code 1 and 2 in pck1 and pck2. So to run the experiment for different grid size it should be changed manually and for each grid size it should be done 10 times to reduce the chance of an outlier.
    public static void main(String[] args) {
        
        long startTime = System.currentTimeMillis();

        Runtime runtime = Runtime.getRuntime();

        // We perform gc (garbage collection) to reduce the affect of previously created objects
        runtime.gc();
        // this code is a lot similar to pck2 
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();

        JToggleButton[][] buttons = new JToggleButton[grid_size][grid_size];
        MinesweeperGUI gui = new MinesweeperGUI(buttons);
        MinesweeperLogic logic = new MinesweeperLogic(gui, buttons);
        MinesweeperHandler handler = new MinesweeperHandler(logic, gui);
        gui.setHandler(handler); // We set the handler in the GUI

        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = memoryAfter - memoryBefore;

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("Memory used: " + memoryUsed + " bytes");
        System.out.println("Startup Time: " + duration + " milliseconds");
    }
}

//Memory used: 33630456 bytes
//Startup Time: 1553 milliseconds