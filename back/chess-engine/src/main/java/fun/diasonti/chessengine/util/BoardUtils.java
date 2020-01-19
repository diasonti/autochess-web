package fun.diasonti.chessengine.util;

import fun.diasonti.chessengine.data.ChessBoard;

public final class BoardUtils {
    private BoardUtils() {
    }

    public static ChessBoard fenToBitboard(String fen) {
        final ChessBoard board = new ChessBoard();
        int pos = 0;
        for (int i = 0; pos < ChessBoard.SIZE; i++) {
            char symbol = fen.charAt(i);
            if (Character.isDigit(symbol)) {
                int skip = Integer.parseInt(String.valueOf(symbol));
                pos += skip;
            } else if (Character.isAlphabetic(symbol)) {
                switch (symbol) {
                    case 'R':
                        board.whiteRooks |= (1L << pos);
                        break;
                    case 'N':
                        board.whiteKnights |= (1L << pos);
                        break;
                    case 'B':
                        board.whiteBishops |= (1L << pos);
                        break;
                    case 'Q':
                        board.whiteQueens |= (1L << pos);
                        break;
                    case 'K':
                        board.whiteKings |= (1L << pos);
                        break;
                    case 'P':
                        board.whitePawns |= (1L << pos);
                        break;
                    case 'r':
                        board.blackRooks |= (1L << pos);
                        break;
                    case 'n':
                        board.blackKnights |= (1L << pos);
                        break;
                    case 'b':
                        board.blackBishops |= (1L << pos);
                        break;
                    case 'q':
                        board.blackQueens |= (1L << pos);
                        break;
                    case 'k':
                        board.blackKings |= (1L << pos);
                        break;
                    case 'p':
                        board.blackPawns |= (1L << pos);
                        break;
                    default:
                        pos--;
                }
                pos++;
            }
        }
        return board;
    }

    public static String bitboardToFen(ChessBoard board) {
        String fen = "";
        final long pieces = board.getAllPieces();
        int col = 0;
        int empty = 0;
        for (int i = 0; i < ChessBoard.SIZE; i++) {
            if (col == 8) {
                if (empty > 0) {
                    fen += empty;
                    empty = 0;
                }
                fen += "/";
                col = 0;
            }
            col++;
            final int offset = i;
            final boolean hasPiece = ((pieces >>> offset) & 1) == 1;
            if (hasPiece) {
                if (empty > 0) {
                    fen += empty;
                    empty = 0;
                }
                boolean hasWhitePawn = ((board.whitePawns >>> offset) & 1) == 1;
                if (hasWhitePawn) {
                    fen += "P";
                    continue;
                }
                boolean hasBlackPawn = ((board.blackPawns >>> offset) & 1) == 1;
                if (hasBlackPawn) {
                    fen += "p";
                    continue;
                }
                boolean hasWhiteRook = ((board.whiteRooks >>> offset) & 1) == 1;
                if (hasWhiteRook) {
                    fen += "R";
                    continue;
                }
                boolean hasWhiteKnight = ((board.whiteKnights >>> offset) & 1) == 1;
                if (hasWhiteKnight) {
                    fen += "N";
                    continue;
                }
                boolean hasWhiteBishop = ((board.whiteBishops >>> offset) & 1) == 1;
                if (hasWhiteBishop) {
                    fen += "B";
                    continue;
                }
                boolean hasWhiteQueen = ((board.whiteQueens >>> offset) & 1) == 1;
                if (hasWhiteQueen) {
                    fen += "Q";
                    continue;
                }
                boolean hasWhiteKing = ((board.whiteKings >>> offset) & 1) == 1;
                if (hasWhiteKing) {
                    fen += "K";
                    continue;
                }
                boolean hasBlackRook = ((board.blackRooks >>> offset) & 1) == 1;
                if (hasBlackRook) {
                    fen += "r";
                    continue;
                }
                boolean hasBlackKnight = ((board.blackKnights >>> offset) & 1) == 1;
                if (hasBlackKnight) {
                    fen += "n";
                    continue;
                }
                boolean hasBlackBishop = ((board.blackBishops >>> offset) & 1) == 1;
                if (hasBlackBishop) {
                    fen += "b";
                    continue;
                }
                boolean hasBlackQueen = ((board.blackQueens >>> offset) & 1) == 1;
                if (hasBlackQueen) {
                    fen += "q";
                    continue;
                }
                boolean hasBlackKing = ((board.blackKings >>> offset) & 1) == 1;
                if (hasBlackKing) {
                    fen += "k";
                    continue;
                }
            } else {
                empty++;
            }
        }
        return fen;
    }

    public static String boardToString(ChessBoard board) {
        String str = "";
        final String fen = bitboardToFen(board);
        for (int i = 0; i < fen.length(); i++) {
            char symbol = fen.charAt(i);
            if (Character.isDigit(symbol)) {
                int skip = Integer.parseInt(String.valueOf(symbol));
                str += new String(new char[skip]).replace("\0", ". ");
            } else if (Character.isAlphabetic(symbol)) {
                str += symbol + " ";
            } else if (symbol == '/') {
                str += System.getProperty("line.separator");

            }
        }
        return str;
    }

}
