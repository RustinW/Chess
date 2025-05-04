package org.cis1200.chess;

public class Rook extends ChessPiece {
    public Rook(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(ChessPiece[][] board, int fromRow, int fromCol, int toRow, int toCol) {
        if (!isValidSquare(fromRow, fromCol) || !isValidSquare(toRow, toCol)) {
            return false;
        }

        if (fromRow != toRow && fromCol != toCol) {
            return false;
        }

        int rowStep = Integer.compare(toRow, fromRow);
        int colStep = Integer.compare(toCol, fromCol);
        int r = fromRow + rowStep;
        int c = fromCol + colStep;

        while (r != toRow || c != toCol) {
            if (board[r][c] != null) {
                return false;
            }
            r += rowStep;
            c += colStep;
        }
        return board[toRow][toCol] == null || board[toRow][toCol].isWhite() != this.isWhite();
    }

    private boolean isValidSquare(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    @Override
    public String getType() {
        return "Rook";
    }
}
