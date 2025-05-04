package org.cis1200.chess;

public class King extends ChessPiece {
    public King(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(ChessPiece[][] board, int fromRow, int fromCol, int toRow, int toCol) {
        if (!isValidSquare(fromRow, fromCol) || !isValidSquare(toRow, toCol)) {
            return false;
        }

        // Normal king move
        if (Math.abs(fromRow - toRow) <= 1 && Math.abs(fromCol - toCol) <= 1) {
            return board[toRow][toCol] == null || board[toRow][toCol].isWhite() != this.isWhite();
        }

        // Castling move
        if (!this.hasMoved() && fromRow == toRow && Math.abs(fromCol - toCol) == 2) {
            // Kingside castling
            if (toCol == 6) {
                if (board[fromRow][5] == null && board[fromRow][6] == null &&
                        board[fromRow][7] instanceof Rook && !board[fromRow][7].hasMoved()) {
                    return true;
                }
            }
            // Queenside castling
            else if (toCol == 2) {
                if (board[fromRow][1] == null && board[fromRow][2] == null && board[fromRow][3] == null &&
                        board[fromRow][0] instanceof Rook && !board[fromRow][0].hasMoved()) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isValidSquare(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    @Override
    public String getType() {
        return "King";
    }
}
