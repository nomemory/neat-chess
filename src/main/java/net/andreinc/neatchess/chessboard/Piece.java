package net.andreinc.neatchess.chessboard;

import java.util.Map;

import static net.andreinc.neatchess.chessboard.PieceType.*;

public class Piece {

    private static Map<PieceType, Short> VALUES_MID_GAME = Map.of(
            PAWN, (short) 126,
            KNIGHT, (short) 781,
            BISHOP, (short) 825,
            ROOK, (short) 1276,
            QUEEN, (short) 2538
    );

    public static Map<PieceType, Short> VALUES_END_GAME = Map.of(
            PAWN, (short) 208,
            KNIGHT, (short) 854,
            BISHOP, (short) 915,
            ROOK, (short) 1380,
            QUEEN, (short) 2682
    );

    private PieceColor color;
    private PieceType type;
    private short valueMg;
    private short valueEg;

    public Piece(PieceType type, PieceColor color) {
        this.type = type;
        this.color = color;
        this.valueEg = VALUES_END_GAME.get(type);
        this.valueMg = VALUES_MID_GAME.get(type);
    }


}
