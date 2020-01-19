package fun.diasonti.chessengine.engine;

import fun.diasonti.chessengine.data.ChessBoard;
import fun.diasonti.chessengine.data.Color;
import fun.diasonti.chessengine.data.Piece;

public class EvaluationEngine {

    private static final int[][][] PAWN = {
            {
                { 0,  0,  0,  0,  0,  0,  0,  0  },
                { 5, 10, 10,-20,-20, 10, 10,  5  },
                { 5, -5,-10,  0,  0,-10, -5,  5  },
                { 0,  0,  0, 20, 20,  0,  0,  0  },
                { 5,  5, 10, 25, 25, 10,  5,  5  },
                { 10, 10, 20, 30, 30, 20, 10, 10 },
                { 50, 50, 50, 50, 50, 50, 50, 50 },
                { 0,  0,  0,  0,  0,  0,  0,  0  }
            }, {
                {  0,  0,  0,  0,  0,  0,  0,  0 },
                { 50, 50, 50, 50, 50, 50, 50, 50 },
                { 10, 10, 20, 30, 30, 20, 10, 10 },
                {  5,  5, 10, 25, 25, 10,  5,  5 },
                {  0,  0,  0, 20, 20,  0,  0,  0 },
                {  5, -5,-10,  0,  0,-10, -5,  5 },
                {  5, 10, 10,-20,-20, 10, 10,  5 },
                {  0,  0,  0,  0,  0,  0,  0,  0 }
            }
    };

    private static final int[][][] KNIGHT = {
            {
                { -50,-40,-30,-30,-30,-30,-40,-50 },
                { -40,-20,  0,  5,  5,  0,-20,-40 },
                { -30,  5, 10, 15, 15, 10,  5,-30 },
                { -30,  0, 15, 20, 20, 15,  0,-30 },
                { -30,  5, 15, 20, 20, 15,  5,-30 },
                { -30,  0, 10, 15, 15, 10,  0,-30 },
                { -40,-20,  0,  0,  0,  0,-20,-40 },
                { -50,-40,-30,-30,-30,-30,-40,-50 }
            }, {
                { -50,-40,-30,-30,-30,-30,-40,-50 },
                { -40,-20,  0,  0,  0,  0,-20,-40 },
                { -30,  0, 10, 15, 15, 10,  0,-30 },
                { -30,  5, 15, 20, 20, 15,  5,-30 },
                { -30,  0, 15, 20, 20, 15,  0,-30 },
                { -30,  5, 10, 15, 15, 10,  5,-30 },
                { -40,-20,  0,  5,  5,  0,-20,-40 },
                { -50,-40,-30,-30,-30,-30,-40,-50 }
            }
    };

    private static final int[][][] BISHOP = {
            {
                { -20,-10,-10,-10,-10,-10,-10,-20 },
                { -10,  5,  0,  0,  0,  0,  5,-10 },
                { -10, 10, 10, 10, 10, 10, 10,-10 },
                { -10,  0, 10, 10, 10, 10,  0,-10 },
                { -10,  5,  5, 10, 10,  5,  5,-10 },
                { -10,  0,  5, 10, 10,  5,  0,-10 },
                { -10,  0,  0,  0,  0,  0,  0,-10 },
                { -20,-10,-10,-10,-10,-10,-10,-20 }
            }, {
                { -20,-10,-10,-10,-10,-10,-10,-20 },
                { -10,  0,  0,  0,  0,  0,  0,-10 },
                { -10,  0,  5, 10, 10,  5,  0,-10 },
                { -10,  5,  5, 10, 10,  5,  5,-10 },
                { -10,  0, 10, 10, 10, 10,  0,-10 },
                { -10, 10, 10, 10, 10, 10, 10,-10 },
                { -10,  5,  0,  0,  0,  0,  5,-10 },
                { -20,-10,-10,-10,-10,-10,-10,-20 }
            }
    };

    private static final int[][][] ROOK = {
            {
                {  0,  0,  0,  5,  5,  0,  0,  0 },
                { -5,  0,  0,  0,  0,  0,  0, -5 },
                { -5,  0,  0,  0,  0,  0,  0, -5 },
                { -5,  0,  0,  0,  0,  0,  0, -5 },
                { -5,  0,  0,  0,  0,  0,  0, -5 },
                { -5,  0,  0,  0,  0,  0,  0, -5 },
                {  5, 10, 10, 10, 10, 10, 10,  5 },
                {  0,  0,  0,  5,  5,  0,  0,  0 }
            }, {
                {  0,  0,  0,  0,  0,  0,  0,  0 },
                {  5, 10, 10, 10, 10, 10, 10,  5 },
                { -5,  0,  0,  0,  0,  0,  0, -5 },
                { -5,  0,  0,  0,  0,  0,  0, -5 },
                { -5,  0,  0,  0,  0,  0,  0, -5 },
                { -5,  0,  0,  0,  0,  0,  0, -5 },
                { -5,  0,  0,  0,  0,  0,  0, -5 },
                {  0,  0,  0,  5,  5,  0,  0,  0 }
            }
    };

    private static final int[][][] QUEEN = {
            {
                { -20,-10,-10, -5, -5,-10,-10,-20 },
                { -10,  0,  5,  0,  0,  0,  0,-10 },
                { -10,  5,  5,  5,  5,  5,  0,-10 },
                {   0,  0,  5,  5,  5,  5,  0, -5 },
                {  -5,  0,  5,  5,  5,  5,  0, -5 },
                { -10,  0,  5,  5,  5,  5,  0,-10 },
                { -10,  0,  0,  0,  0,  0,  0,-10 },
                { -20,-10,-10, -5, -5,-10,-10,-20 }
            }, {
                { -20,-10,-10, -5, -5,-10,-10,-20 },
                { -10,  0,  0,  0,  0,  0,  0,-10 },
                { -10,  0,  5,  5,  5,  5,  0,-10 },
                {  -5,  0,  5,  5,  5,  5,  0, -5 },
                {   0,  0,  5,  5,  5,  5,  0, -5 },
                { -10,  5,  5,  5,  5,  5,  0,-10 },
                { -10,  0,  5,  0,  0,  0,  0,-10 },
                { -20,-10,-10, -5, -5,-10,-10,-20 }
            }
    };

    private static final int[][][] KING = {
            {
                {  20, 30, 10,  0,  0, 10, 30, 20 },
                {  20, 20,  0,  0,  0,  0, 20, 20 },
                { -10,-20,-20,-20,-20,-20,-20,-10 },
                { -20,-30,-30,-40,-40,-30,-30,-20 },
                { -30,-40,-40,-50,-50,-40,-40,-30 },
                { -30,-40,-40,-50,-50,-40,-40,-30 },
                { -30,-40,-40,-50,-50,-40,-40,-30 },
                { -30,-40,-40,-50,-50,-40,-40,-30 }
            }, {
                { -30,-40,-40,-50,-50,-40,-40,-30 },
                { -30,-40,-40,-50,-50,-40,-40,-30 },
                { -30,-40,-40,-50,-50,-40,-40,-30 },
                { -30,-40,-40,-50,-50,-40,-40,-30 },
                { -20,-30,-30,-40,-40,-30,-30,-20 },
                { -10,-20,-20,-20,-20,-20,-20,-10 },
                {  20, 20,  0,  0,  0,  0, 20, 20 },
                {  20, 30, 10,  0,  0, 10, 30, 20 }
            }
    };

    public int getScore(ChessBoard board) {
        int score = 0;
        score += getMaterialScore(board);
        score += getPositionalScore(board);
        score += getMobilityScore(board);
        return score;
    }

    public int getMaterialScore(ChessBoard board) {
        int score = 0;

        long whitePawns = board.whitePawns;
        while (whitePawns != 0) {
            whitePawns &= ~(1L << Long.numberOfTrailingZeros(whitePawns));
            score += getMaterialScore(Piece.PAWN);
        }

        long whiteKnights = board.whiteKnights;
        while (whiteKnights != 0) {
            whiteKnights &= ~(1L << Long.numberOfTrailingZeros(whiteKnights));
            score += getMaterialScore(Piece.KNIGHT);
        }

        long whiteBishops = board.whiteBishops;
        while (whiteBishops != 0) {
            whiteBishops &= ~(1L << Long.numberOfTrailingZeros(whiteBishops));
            score += getMaterialScore(Piece.BISHOP);
        }

        long whiteRooks = board.whiteRooks;
        while (whiteRooks != 0) {
            whiteRooks &= ~(1L << Long.numberOfTrailingZeros(whiteRooks));
            score += getMaterialScore(Piece.ROOK);
        }

        long whiteQueens = board.whiteQueens;
        while (whiteQueens != 0) {
            whiteQueens &= ~(1L << Long.numberOfTrailingZeros(whiteQueens));
            score += getMaterialScore(Piece.QUEEN);
        }

        long whiteKings = board.whiteKings;
        while (whiteKings != 0) {
            whiteKings &= ~(1L << Long.numberOfTrailingZeros(whiteKings));
            score += getMaterialScore(Piece.KING);
        }

        long blackPawns = board.blackPawns;
        while (blackPawns != 0) {
            blackPawns &= ~(1L << Long.numberOfTrailingZeros(blackPawns));
            score -= getMaterialScore(Piece.PAWN);
        }

        long blackKnights = board.blackKnights;
        while (blackKnights != 0) {
            blackKnights &= ~(1L << Long.numberOfTrailingZeros(blackKnights));
            score -= getMaterialScore(Piece.KNIGHT);
        }

        long blackBishops = board.blackBishops;
        while (blackBishops != 0) {
            blackBishops &= ~(1L << Long.numberOfTrailingZeros(blackBishops));
            score -= getMaterialScore(Piece.BISHOP);
        }

        long blackRooks = board.blackRooks;
        while (blackRooks != 0) {
            blackRooks &= ~(1L << Long.numberOfTrailingZeros(blackRooks));
            score -= getMaterialScore(Piece.ROOK);
        }

        long blackQueens = board.blackQueens;
        while (blackQueens != 0) {
            blackQueens &= ~(1L << Long.numberOfTrailingZeros(blackQueens));
            score -= getMaterialScore(Piece.QUEEN);
        }

        long blackKings = board.blackKings;
        while (blackKings != 0) {
            blackKings &= ~(1L << Long.numberOfTrailingZeros(blackKings));
            score -= getMaterialScore(Piece.KING);
        }

        return score;
    }

    private int getMaterialScore(Piece piece) {
        switch (piece) {
            case PAWN:
                return 100;
            case KNIGHT:
                return 320;
            case BISHOP:
                return 333;
            case ROOK:
                return 510;
            case QUEEN:
                return 880;
            case KING:
                return 5000;
            default:
                throw new IllegalStateException("Unexpected value: " + piece);
        }
    }

    public int getPositionalScore(ChessBoard board) {
        int score = 0;
        score += getPositionalScore(board.whiteKings,   Piece.KING,   Color.WHITE);
        score += getPositionalScore(board.whiteQueens,  Piece.QUEEN,  Color.WHITE);
        score += getPositionalScore(board.whiteRooks,   Piece.ROOK,   Color.WHITE);
        score += getPositionalScore(board.whiteBishops, Piece.BISHOP, Color.WHITE);
        score += getPositionalScore(board.whiteKnights, Piece.KNIGHT, Color.WHITE);
        score += getPositionalScore(board.whitePawns,   Piece.PAWN,   Color.BLACK);
        score -= getPositionalScore(board.blackKings,   Piece.KING,   Color.BLACK);
        score -= getPositionalScore(board.blackQueens,  Piece.QUEEN,  Color.BLACK);
        score -= getPositionalScore(board.blackRooks,   Piece.ROOK,   Color.BLACK);
        score -= getPositionalScore(board.blackBishops, Piece.BISHOP, Color.BLACK);
        score -= getPositionalScore(board.blackKnights, Piece.KNIGHT, Color.BLACK);
        score -= getPositionalScore(board.blackPawns,   Piece.PAWN,   Color.BLACK);
        return score;
    }

    private int getPositionalScore(long bitboard, Piece piece, Color color) {
        int colour = 0;
        if (color == Color.WHITE) {
            colour = 1;
        }
        int score = 0;
        long position = bitboard & -bitboard;
        while (position != 0) {
            int cell = Long.numberOfTrailingZeros(position);
            switch (piece) {
                case PAWN:
                    score += PAWN[colour][cell / 8][cell % 8];
                    break;
                case KNIGHT:
                    score += KNIGHT[colour][cell / 8][cell % 8];
                    break;
                case BISHOP:
                    score += BISHOP[colour][cell / 8][cell % 8];
                    break;
                case ROOK:
                    score += ROOK[colour][cell / 8][cell % 8];
                    break;
                case QUEEN:
                    score += QUEEN[colour][cell / 8][cell % 8];
                    break;
                case KING:
                    score += KING[colour][cell / 8][cell % 8];
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + piece);
            }
            bitboard &= ~position;
            position = bitboard & -bitboard;
        }
        return score;
    }

    public int getMobilityScore(ChessBoard board) {
        return 0;
    }

}
