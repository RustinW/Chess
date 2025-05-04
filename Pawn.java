package org.cis1200.chess;

public class Pawn extends ChessPiece {
    public Pawn(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(ChessPiece[][] board, int fromRow, int fromCol, int toRow, int toCol) {
        Chess game = ChessGameHolder.getGame();
        int dir = isWhite() ? -1 : 1;
        int startRow = isWhite() ? 6 : 1;

        if (fromCol == toCol) {
            if (toRow - fromRow == dir && board[toRow][toCol] == null) {
                return true;
            }
            if (fromRow == startRow && toRow - fromRow == 2 * dir && board[fromRow + dir][toCol] == null && board[toRow][toCol] == null) {
                return true;
            }
        } else if (Math.abs(toCol - fromCol) == 1 && toRow - fromRow == dir) {
            if (board[toRow][toCol] != null && board[toRow][toCol].isWhite() != isWhite()) {
                return true;
            }
            if (board[toRow][toCol] == null) {
                int lastMoveFromRow = game.getLastMoveFromRow();
                int lastMoveToRow = game.getLastMoveToRow();
                int lastMoveToCol = game.getLastMoveToCol();
                if (lastMoveFromRow == (isWhite() ? 1 : 6) &&
                        lastMoveToRow == fromRow &&
                        lastMoveToCol == toCol) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getType() {
        return "Pawn";
    }
}
