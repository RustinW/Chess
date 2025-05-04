public abstract class ChessPiece {
    private boolean isWhite;
    private boolean hasMoved;

    public ChessPiece(boolean isWhite) {
        this.isWhite = isWhite;
        this.hasMoved = false;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setMoved(boolean moved) {
        this.hasMoved = moved;
    }

    public abstract boolean isValidMove(ChessPiece[][] board, int fromRow, int fromCol, int toRow, int toCol);

    public abstract String getType();
}
