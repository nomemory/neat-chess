package net.andreinc.neatchess.chessboard;

public class Square {

    private Piece piece;

    public Square(Piece piece) {
        this.piece = piece;
    }

    public Square() {
    }

    public Piece getPiece() {
        return piece;
    }

    public boolean isEmpty() {
        return piece == null;
    }
}
