package org.cis1200.chess;

import javax.swing.*;

public class RunChess implements Runnable {
    public void run() {
        final JFrame frame = new JFrame("Chess");
        frame.setLocation(300, 300);

        final ChessBoard chessBoard = new ChessBoard();
        frame.add(chessBoard);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
