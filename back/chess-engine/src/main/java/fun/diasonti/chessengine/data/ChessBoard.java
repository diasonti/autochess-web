package fun.diasonti.chessengine.data;

public class ChessBoard {

    public static final int SIZE = 64;

    public long whiteKings;
    public long whiteQueens;
    public long whiteRooks;
    public long whiteBishops;
    public long whiteKnights;
    public long whitePawns;

    public long blackKings;
    public long blackQueens;
    public long blackRooks;
    public long blackBishops;
    public long blackKnights;
    public long blackPawns;

    public long getWhitePieces() {
        return whiteKings | whiteQueens | whiteRooks | whiteBishops | whiteKnights | whitePawns;
    }

    public long getBlackPieces() {
        return blackKings | blackQueens | blackRooks | blackBishops | blackKnights | blackPawns;
    }

    public long getAllPieces() {
        return getWhitePieces() | getBlackPieces();
    }

    public long getEmptyCells() {
        return ~getAllPieces();
    }

}
