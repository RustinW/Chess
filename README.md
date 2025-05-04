===========
=: Core Concepts :=======
  1. 2D Arrays: I will use a 2D array of Piece objects to represent the 8x8 game board. Each index in the array
  corresponds to a square on the board and will either contain a chess piece object or be null if empty. It will be
  used for rendering the board, tracking valid moves, and determining the game state.

  2. Inheritance and subtyping: Each chess piece will be a subclass of an abstract Piece class. This base class will
  define shared attributes such as position and color and an abstract method called getValidMoves().
  Each subclass will implement its own move logic. This allows for polymorphic behavior when interacting with pieces.

  3. Collections: I will use collections such as ArrayList to manage active pieces for each player, store valid moves,
  and maintain move history for features such as undo or looking for repeated positions. Collections will also help
  track captured pieces for visual display or detecting when it is endgame. .

  4. Complex Game Logic: I hope to use complex game logic by coding chess mechanics such as check, checkmate, castling,
  pawn promotion and en passant. These features require evaluating the board state, move history, and potential threats.
  For example, I will look for when a player's move leaves their own king in check or if castling move is legal based on
  the rook and king movement history. These rules will be enforced during gameplay using helper methods within the game
  model and piece classes.


=========================
=: Your Implementation :=
=========================
Chess.java:
This is the main class which maintains the board array, makes sure that turn order occurs, makes sure the desired move
is valid and then if it is valid it will execute the move which includes niche rules such as castling, en passant, and
promotions. Also, it detects check and checkmate through simulation of all legal moves on a copy of the board.

ChessPiece.java:
This is the superclass to the piece classes - king, queen, etc... - which creates the common behavior (isWhite(),
hasMoved()) and defines isValidMove(...) which is different piece by piece.

ChessBoard.java:
This is a java swing JPanel that paints the board and the pieces as well as highlights legal moves and handles mouse
clicks in order to invoke Chess.movePiece(...).

ChessGameHolder.java:
This is a static holder so Pawn can use the last move for en passant logic without creating circular dependencies.

RunChess.java:
Starts the Swing JFrame and embeds the ChessBoard and then starts the UI thread.

King.java:
Overrides the isValidMove() to allow one squared moves in any direction with the special case that if it has not moved
and wants to move exactly two files left or right of where it started the game and detects the rook and empty path it
castles.

Pawn.java:
Single step forward if empty, two steps from its home position if both squares are clear. Diagonal capture when an enemy
piece is in that square. En Passant is enabled if the opponent's last move was a two square pawn push ending in the
square adjacent to the current pawn. The diagonal move into the empty square is permitted and the captured pawn is
removed behind it. This uses on ChessGameHolder.getGame() to check the last move. For promotions, when reaching the
furthest row it triggers a GUI prompt and the user can choose Queen/Rook/Bishop/Knight and replaces the pawn with
respect to what the user chose.

Queen.java:
Combines rook and bishop moves by going to fresh Rook and Bishop instances. The Queen's isValidMove() returns
rook.isValidMove() || bishop.isValidMove().

Knight.java:
Implements L-Shaped jump: two squares in one direction and another square perpendicular. Ignores the obstruction
squares (the ones it "jumped" over). isValidMove() checks (dr == 2 && dc == 1) || (dr == 1 && dc == 2).

Bishop.java:
Moves any number of squares along a diagonal. Does so by verifies |delta(row)| == |delta(col)| and that every square
between from and to is empty. isValidMove() walks the diagonal and returns false on obstructions. Rejects moves that
are to the same square.

Rook.java:
Moves any number of vacant squares along a rank or file. Also, checks that all squares between from and to are empty.
This rejects diagonal moves or jumps over pieces. isValidMove() implements straight line path checking via
rowStep / colStep.

There were a ton of stumbling blocks when I was trying to implement my game. First, implementing en passant
correctly was very difficult since it required recording the opponent's last pawn double step and then simulating
the capture on an empty square. Then, castling was difficult since the logic needed both move validation which included
making sure there was an empty path and the king and rook were both unmoved as well as simulating the move for side
effects of castling such as checkmate. Finally, checkmate was the most difficult and it kept crashing for awhile. I
made a checkmate detection with a board copy helper in order to avoid mutable rollback and ensured no move puts your
own king in check.

I believe that I achieved good separation of functionality with Chess + the piece classes which are completely
independent of the UI (ChessBoard). The private state of board and whiteTurn is very well encapsulated and the public
APIs only allow specific interactions. If I refactor this, I would possibly create an object for undo/redo the move.
