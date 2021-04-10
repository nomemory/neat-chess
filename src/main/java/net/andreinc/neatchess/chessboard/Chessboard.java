package net.andreinc.neatchess.chessboard;

public class Chessboard {

    public Square[] squares = new Square[64];

    private boolean blackCheckmate;
    private boolean whiteCheckmate;
    private boolean stalemate;

    private short fiftyMove;
    private short repeatedMove;
    private short moveCount;

    private boolean whiteCastled;
    private boolean blackCastled;

    private PieceColor whoMoves;

    public Chessboard() {
        for(int i = 0; i < 64; ++i) {
            squares[i] = new Square();
        }
    }

    
}
