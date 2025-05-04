package org.cis1200.chess;

public class Bishop extends ChessPiece {
    public Bishop(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(ChessPiece[][] board, int fromRow, int fromCol, int toRow, int toCol) {
        if (!isValidSquare(fromRow, fromCol) || !isValidSquare(toRow, toCol)) {
            return false;
        }

        if (fromRow == toRow && fromCol == toCol) {
            return false;
        }

        if (Math.abs(fromRow - toRow) != Math.abs(fromCol - toCol)) {
            return false;
        }

        int rowStep = (toRow > fromRow) ? 1 : -1;
        int colStep = (toCol > fromCol) ? 1 : -1;
        int r = fromRow + rowStep;
        int c = fromCol + colStep;

        while (r != toRow && c != toCol) {
            if (board[r][c] != null) {
                return false;
            }
            r += rowStep;
            c += colStep;
        }

        return board[toRow][toCol] == null || board[toRow][toCol].isWhite() != isWhite();
    }

    private boolean isValidSquare(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    @Override
    public String getType() {
        return "Bishop";
    }
}
