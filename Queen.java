package org.cis1200.chess;

public class Queen extends ChessPiece {
    public Queen(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(ChessPiece[][] board, int fromRow, int fromCol, int toRow, int toCol) {
        Rook rook = new Rook(isWhite());
        Bishop bishop = new Bishop(isWhite());
        return rook.isValidMove(board, fromRow, fromCol, toRow, toCol) || bishop.isValidMove(board, fromRow, fromCol, toRow, toCol);
    }

    @Override
    public String getType() {
        return "Queen";
    }
}
