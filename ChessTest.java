package org.cis1200.chess;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChessTest {
    private Chess game;

    @BeforeEach
    public void setUp() {
        game = new Chess();
        game.resetBoard();
        ChessGameHolder.setGame(game);
    }

    @Test
    public void testRookStraightMove() {
        // clear pawn at a2
        game.getBoard()[6][0] = null;
        assertTrue(game.movePiece(7, 0, 5, 0)); // a1 -> a3
    }

    @Test
    public void testBishopDiagonalMove() {
        // clear pawn at d2 to open diagonal c1->e3
        game.getBoard()[6][3] = null;
        assertTrue(game.movePiece(7, 2, 5, 4)); // c1 -> e3
    }

    @Test
    public void testKnightJump() {
        assertTrue(game.movePiece(7, 1, 5, 2)); // b1 -> c3
    }

    @Test
    public void testPawnDoubleStep() {
        assertTrue(game.movePiece(6, 4, 4, 4)); // e2 -> e4
    }

    @Test
    public void testPawnSingleStepBlocked() {
        // place blocking pawn at d3
        game.getBoard()[5][3] = new Pawn(true);
        assertFalse(game.movePiece(6, 3, 5, 3)); // d2 -> d3 blocked
    }

    @Test
    public void testIllegalQueenMove() {
        assertFalse(game.movePiece(7, 3, 5, 4)); // queen blocked
    }

    @Test
    public void testEnPassant() {
        // setup
        assertTrue(game.movePiece(6, 4, 4, 4)); // e2->e4
        assertTrue(game.movePiece(1, 3, 3, 3)); // d7->d5
        assertTrue(game.movePiece(4, 4, 3, 3)); // en passant
        assertNull(game.getPiece(4, 3));      // captured pawn removed
    }

    @Test
    public void testCastlingKingside() {
        game.getBoard()[7][5] = null;
        game.getBoard()[7][6] = null;
        assertTrue(game.movePiece(7, 4, 7, 6)); // O-O
        assertTrue(game.getPiece(7, 5) instanceof Rook);
    }

    @Test
    public void testInCheckDetection() {
        game.movePiece(6, 5, 5, 5); // f2-f3
        game.movePiece(1, 4, 3, 4); // e7-e5
        game.movePiece(6, 6, 4, 6); // g2-g4
        game.movePiece(0, 3, 4, 7); // Qd8-h4
        assertTrue(game.isInCheck(true));
    }

    @Test
    public void testCheckmateFoolsMate() {
        game.movePiece(6, 5, 5, 5);
        game.movePiece(1, 4, 3, 4);
        game.movePiece(6, 6, 4, 6);
        game.movePiece(0, 3, 4, 7);
        assertTrue(game.isCheckmate(true));
    }
}
