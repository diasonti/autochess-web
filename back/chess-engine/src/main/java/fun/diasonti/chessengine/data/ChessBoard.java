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

    public Color currentPlayer = Color.WHITE;
    public int moveCount = 0;

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

    public ChessBoard getCopy() {
        final ChessBoard copy = new ChessBoard();
        copy.whiteKings = this.whiteKings;
        copy.whiteQueens = this.whiteQueens;
        copy.whiteRooks = this.whiteRooks;
        copy.whiteBishops = this.whiteBishops;
        copy.whiteKnights = this.whiteKnights;
        copy.whitePawns = this.whitePawns;
        copy.blackKings = this.blackKings;
        copy.blackQueens = this.blackQueens;
        copy.blackRooks = this.blackRooks;
        copy.blackBishops = this.blackBishops;
        copy.blackKnights = this.blackKnights;
        copy.blackPawns = this.blackPawns;
        copy.currentPlayer = this.currentPlayer;
        copy.moveCount = this.moveCount;
        return copy;
    }

}
