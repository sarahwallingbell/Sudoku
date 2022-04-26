package graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Sudoku{


	/**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI(String filename) {
        //Create and set up the window.
        JFrame frame = new JFrame("Sudoku");
        frame.setLocation(new Point(700, 10));
        frame.setPreferredSize(new Dimension(500, 500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        JPanel panel = new SudokuPanel(filename);
        panel.setBackground(Color.WHITE);
        frame.add(panel);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
    	if(args.length != 1) {
    		System.err.println("Usage: java Sudoku <puzzle>");
    		System.exit(-1);
    	}
    	//Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args[0]);
            }
        });
    }
}
