package org.cis1200.chess;

public class Knight extends ChessPiece {
    public Knight(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(ChessPiece[][] board, int fromRow, int fromCol, int toRow, int toCol) {
        if (!isValidSquare(fromRow, fromCol) || !isValidSquare(toRow, toCol)) {
            return false;
        }

        int dr = Math.abs(fromRow - toRow);
        int dc = Math.abs(fromCol - toCol);
        if ((dr == 2 && dc == 1) || (dr == 1 && dc == 2)) {
            return board[toRow][toCol] == null || board[toRow][toCol].isWhite() != isWhite();
        }
        return false;
    }

    private boolean isValidSquare(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    @Override
    public String getType() {
        return "Knight";
    }
}
