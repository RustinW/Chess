package org.cis1200.chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChessBoard extends JPanel {
    private Chess game;
    private int selectedRow;
    private int selectedCol;

    public ChessBoard() {
        setLayout(new BorderLayout());

        game = new Chess();
        ChessGameHolder.setGame(game);
        selectedRow = -1;
        selectedCol = -1;

        BoardPanel boardPanel = new BoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        JButton restartButton = new JButton("Restart Game");
        restartButton.addActionListener(e -> {
            game.resetBoard();
            selectedRow = -1;
            selectedCol = -1;
            repaint();
        });
        add(restartButton, BorderLayout.SOUTH);

        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int squareSize = Math.min(boardPanel.getWidth(), boardPanel.getHeight()) / 8;
                int row = e.getY() / squareSize;
                int col = e.getX() / squareSize;

                if (selectedRow == -1 && selectedCol == -1) {
                    if (game.getPiece(row, col) != null &&
                            game.getPiece(row, col).isWhite() == game.isWhiteTurn()) {
                        selectedRow = row;
                        selectedCol = col;
                    }
                } else {
                    if (game.movePiece(selectedRow, selectedCol, row, col)) {
                        selectedRow = -1;
                        selectedCol = -1;

                        // Check for game over
                        if (game.isGameOver()) {
                            String winner = game.isWhiteTurn() ? "Black" : "White";
                            JOptionPane.showMessageDialog(
                                    ChessBoard.this,
                                    winner + " wins!",
                                    "Game Over",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    } else {
                        selectedRow = -1;
                        selectedCol = -1;
                    }
                }
                repaint();
            }
        });
    }

    private class BoardPanel extends JPanel {
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(800, 800); // Bigger board
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int squareSize = Math.min(getWidth(), getHeight()) / 8;

            // Draw board squares
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if ((row + col) % 2 == 0) {
                        g.setColor(Color.WHITE);
                    } else {
                        g.setColor(Color.GRAY);
                    }
                    g.fillRect(col * squareSize, row * squareSize, squareSize, squareSize);

                    ChessPiece piece = game.getPiece(row, col);
                    if (piece != null) {
                        g.setColor(piece.isWhite() ? Color.BLUE : Color.RED);
                        g.setFont(new Font("Arial", Font.BOLD, Math.min(20, squareSize / 4)));
                        FontMetrics fm = g.getFontMetrics();
                        String text = piece.getType();
                        int textWidth = fm.stringWidth(text);
                        int textHeight = fm.getAscent();
                        g.drawString(text,
                                col * squareSize + (squareSize - textWidth) / 2,
                                row * squareSize + (squareSize + textHeight) / 2 - 5);
                    }
                }
            }

            // Highlight selected square
            if (selectedRow != -1 && selectedCol != -1) {
                g.setColor(Color.YELLOW);
                g.drawRect(selectedCol * squareSize, selectedRow * squareSize, squareSize, squareSize);
                g.drawRect(selectedCol * squareSize + 1, selectedRow * squareSize + 1, squareSize - 2, squareSize - 2);

                // If selected piece is a King, highlight possible castling squares
                ChessPiece selectedPiece = game.getPiece(selectedRow, selectedCol);
                if (selectedPiece instanceof King) {
                    // Kingside castling highlight
                    if (selectedPiece.isWhite() && selectedRow == 7 && selectedCol == 4) {
                        if (game.getPiece(7, 5) == null && game.getPiece(7, 6) == null &&
                                game.getPiece(7, 7) instanceof Rook && !game.getPiece(7, 7).hasMoved() &&
                                !selectedPiece.hasMoved()) {
                            g.setColor(Color.GREEN);
                            g.drawRect(6 * squareSize, 7 * squareSize, squareSize, squareSize);
                            g.drawRect(6 * squareSize + 1, 7 * squareSize + 1, squareSize - 2, squareSize - 2);
                        }
                    }
                    if (!selectedPiece.isWhite() && selectedRow == 0 && selectedCol == 4) {
                        if (game.getPiece(0, 5) == null && game.getPiece(0, 6) == null &&
                                game.getPiece(0, 7) instanceof Rook && !game.getPiece(0, 7).hasMoved() &&
                                !selectedPiece.hasMoved()) {
                            g.setColor(Color.GREEN);
                            g.drawRect(6 * squareSize, 0, squareSize, squareSize);
                            g.drawRect(6 * squareSize + 1, 0 + 1, squareSize - 2, squareSize - 2);
                        }
                    }

                    // Queenside castling highlight
                    if (selectedPiece.isWhite() && selectedRow == 7 && selectedCol == 4) {
                        if (game.getPiece(7, 1) == null && game.getPiece(7, 2) == null && game.getPiece(7, 3) == null &&
                                game.getPiece(7, 0) instanceof Rook && !game.getPiece(7, 0).hasMoved() &&
                                !selectedPiece.hasMoved()) {
                            g.setColor(Color.GREEN);
                            g.drawRect(2 * squareSize, 7 * squareSize, squareSize, squareSize);
                            g.drawRect(2 * squareSize + 1, 7 * squareSize + 1, squareSize - 2, squareSize - 2);
                        }
                    }
                    if (!selectedPiece.isWhite() && selectedRow == 0 && selectedCol == 4) {
                        if (game.getPiece(0, 1) == null && game.getPiece(0, 2) == null && game.getPiece(0, 3) == null &&
                                game.getPiece(0, 0) instanceof Rook && !game.getPiece(0, 0).hasMoved() &&
                                !selectedPiece.hasMoved()) {
                            g.setColor(Color.GREEN);
                            g.drawRect(2 * squareSize, 0, squareSize, squareSize);
                            g.drawRect(2 * squareSize + 1, 0 + 1, squareSize - 2, squareSize - 2);
                        }
                    }
                }
            }

            // Draw turn information
            g.setColor(Color.BLACK);
            String status;
            if (game.isGameOver()) {
                status = (game.isWhiteTurn() ? "Black" : "White") + " Wins!";
            } else {
                status = (game.isWhiteTurn() ? "White" : "Black") + "'s Turn";
                if (game.isInCheck(game.isWhiteTurn())) {
                    status += " (Check!)";
                }
            }
            g.drawString(status, 10, getHeight() - 10);
        }
    }
}
