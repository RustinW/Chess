package org.cis1200.chess;

public class ChessGameHolder {
    private static Chess game;

    public static void setGame(Chess g) {
        game = g;
    }

    public static Chess getGame() {
        return game;
    }
}
