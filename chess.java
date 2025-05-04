package org.cis1200.chess;

import javax.swing.JOptionPane;

public class Chess {
    private ChessPiece[][] board;
    private boolean whiteTurn;
    private int lastFromRow, lastFromCol, lastToRow, lastToCol;

    public Chess() {
        resetBoard();
    }

    public void resetBoard() {
        board = new ChessPiece[8][8];
        whiteTurn = true;
        board[7] = new ChessPiece[]{
                new Rook(true), new Knight(true), new Bishop(true), new Queen(true),
                new King(true), new Bishop(true), new Knight(true), new Rook(true)
        };
        for (int i = 0; i < 8; i++) board[6][i] = new Pawn(true);
        board[0] = new ChessPiece[]{
                new Rook(false), new Knight(false), new Bishop(false), new Queen(false),
                new King(false), new Bishop(false), new Knight(false), new Rook(false)
        };
        for (int i = 0; i < 8; i++) board[1][i] = new Pawn(false);
    }

    public ChessPiece getPiece(int r, int c) {
        return board[r][c];
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public boolean movePiece(int fr, int fc, int tr, int tc) {
        ChessPiece p = board[fr][fc];
        if (p == null || p.isWhite() != whiteTurn || !p.isValidMove(board, fr, fc, tr, tc))
            return false;

        // Prevent self-check
        ChessPiece[][] tb = copy(board);
        // castling
        if (p instanceof King && Math.abs(fc - tc) == 2) {
            if (tc == 6) {
                tb[fr][5] = tb[fr][7];
                tb[fr][7] = null;
            } else {
                tb[fr][3] = tb[fr][0];
                tb[fr][0] = null;
            }
        }
        // en passant
        if (p instanceof Pawn && Math.abs(fc - tc) == 1 && board[tr][tc] == null) {
            tb[fr][tc] = null;
        }
        tb[tr][tc] = tb[fr][fc];
        tb[fr][fc] = null;
        if (inCheck(tb, whiteTurn)) {
            return false;
        }

        // actual move
        if (p instanceof King && Math.abs(fc - tc) == 2) {
            if (tc == 6) {
                board[fr][5] = board[fr][7];
                board[fr][7] = null;
                board[fr][5].setMoved(true);
            } else {
                board[fr][3] = board[fr][0];
                board[fr][0] = null;
                board[fr][3].setMoved(true);
            }
        }

        if (p instanceof Pawn && Math.abs(fc - tc) == 1 && board[tr][tc] == null) {
            board[fr][tc] = null;
        }

        board[tr][tc] = p;
        board[fr][fc] = null;
        p.setMoved(true);

        lastFromRow = fr;
        lastFromCol = fc;
        lastToRow   = tr;
        lastToCol   = tc;

        if (p instanceof Pawn && (tr == 0 || tr == 7)) {
            String[] opts = {"Queen","Rook","Bishop","Knight"};
            String choice = (String) JOptionPane.showInputDialog(
                    null, "Promote pawn to:", "", JOptionPane.PLAIN_MESSAGE, null, opts, "Queen"
            );
            board[tr][tc] =
                    choice.equals("Rook")   ? new Rook(p.isWhite()) :
                            choice.equals("Bishop") ? new Bishop(p.isWhite()) :
                                    choice.equals("Knight") ? new Knight(p.isWhite()) :
                                            new Queen(p.isWhite());
        }

        whiteTurn = !whiteTurn;
        return true;
    }

    public boolean isInCheck(boolean w) {
        return inCheck(board, w);
    }

    public boolean isInCheck() {
        return isInCheck(whiteTurn);
    }

    public boolean isGameOver() {
        return isCheckmate(true) || isCheckmate(false);
    }

    public boolean isCheckmate(boolean w) {
        if (!inCheck(board, w)) return false;
        for (int r1 = 0; r1 < 8; r1++) {
            for (int c1 = 0; c1 < 8; c1++) {
                ChessPiece p = board[r1][c1];
                if (p == null || p.isWhite() != w) continue;
                for (int r2 = 0; r2 < 8; r2++) {
                    for (int c2 = 0; c2 < 8; c2++) {
                        if (p.isValidMove(board, r1, c1, r2, c2)) {
                            ChessPiece[][] tmp = copy(board);
                            ChessPiece cap = tmp[r2][c2];
                            tmp[r2][c2] = tmp[r1][c1];
                            tmp[r1][c1] = null;
                            if (!inCheck(tmp, w)) return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public ChessPiece[][] getBoard() {
        return board;
    }

    private boolean inCheck(ChessPiece[][] b, boolean w) {
        int kr = -1, kc = -1;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                ChessPiece p = b[r][c];
                if (p instanceof King && p.isWhite() == w) {
                    kr = r; kc = c;
                    break;
                }
            }
        }
        if (kr < 0) return false;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                ChessPiece p = b[r][c];
                if (p != null && p.isWhite() != w && p.isValidMove(b, r, c, kr, kc))
                    return true;
            }
        }
        return false;
    }

    private ChessPiece[][] copy(ChessPiece[][] src) {
        ChessPiece[][] dst = new ChessPiece[8][8];
        for (int r = 0; r < 8; r++) {
            System.arraycopy(src[r], 0, dst[r], 0, 8);
        }
        return dst;
    }

    public int getLastMoveFromRow() { return lastFromRow; }
    public int getLastMoveFromCol() { return lastFromCol; }
    public int getLastMoveToRow()   { return lastToRow; }
    public int getLastMoveToCol()   { return lastToCol; }
}
