package fun.diasonti.chessengine.engine;

import fun.diasonti.chessengine.data.ChessBoard;
import fun.diasonti.chessengine.data.Color;
import fun.diasonti.chessengine.data.Direction;
import fun.diasonti.chessengine.data.Move;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MoveEngine {

    /**
     * Masks of rows (ranks). Bottom -> Top
     */
    private static final long[] RANK_MASKS = {
            0xFF00000000000000L, 0xFF000000000000L, 0xFF0000000000L,
            0xFF00000000L, 0xFF000000L, 0xFF0000L, 0xFF00L, 0xFFL
    };

    /**
     * Masks of columns (files). Left -> Right
     */
    private static final long[] FILE_MASKS = {
            0x0101010101010101L, 0x0202020202020202L, 0x0404040404040404L, 0x0808080808080808L,
            0x1010101010101010L, 0x2020202020202020L, 0x4040404040404040L, 0x8080808080808080L
    };

    /**
     * Masks of ╲ diagonals. Top-Right -> Bottom-Left
     */
    private static final long[] MAIN_DIAGONAL_MASK = {
            0x80L, 0x8040L, 0x804020L, 0x80402010L, 0x8040201008L,
            0x804020100804L, 0x80402010080402L, 0x8040201008040201L,
            0x4020100804020100L, 0x2010080402010000L, 0x1008040201000000L,
            0x804020100000000L, 0x402010000000000L, 0x201000000000000L,
            0x100000000000000L
    };

    /**
     * Masks of ╱ diagonals. Top-Left -> Bottom-Right
     */
    private static final long[] ANTI_DIAGONAL_MASK = {
            0x1L, 0x102L, 0x10204L, 0x1020408L, 0x102040810L, 0x10204081020L, 0x1020408102040L,
            0x102040810204080L, 0x204081020408000L, 0x408102040800000L, 0x810204080000000L,
            0x1020408000000000L, 0x2040800000000000L, 0x4080000000000000L, 0x8000000000000000L
    };

    public Set<Move> getAvailableMoves(ChessBoard board, Color color) {
        final Set<Move> moves = new HashSet<>();
        if (color == Color.WHITE) {
            moves.addAll(getWhitePawnMoves(board.whitePawns, board.getEmptyCells(), board.getBlackPieces()));
            moves.addAll(getKnightMoves(board.whiteKnights, board.getEmptyCells(), board.getBlackPieces()));
            moves.addAll(getBishopMoves(board.whiteBishops, board.getEmptyCells(), board.getBlackPieces()));
            moves.addAll(getRookMoves(board.whiteRooks, board.getEmptyCells(), board.getBlackPieces()));
            moves.addAll(getQueenMoves(board.whiteQueens, board.getEmptyCells(), board.getBlackPieces()));
        } else if (color == Color.BLACK) {
            moves.addAll(getBlackPawnMoves(board.blackPawns, board.getEmptyCells(), board.getWhitePieces()));
            moves.addAll(getKnightMoves(board.blackKnights, board.getEmptyCells(), board.getWhitePieces()));
            moves.addAll(getBishopMoves(board.blackBishops, board.getEmptyCells(), board.getWhitePieces()));
            moves.addAll(getRookMoves(board.blackRooks, board.getEmptyCells(), board.getWhitePieces()));
            moves.addAll(getQueenMoves(board.blackQueens, board.getEmptyCells(), board.getWhitePieces()));
        }
        return moves;
    }

    public Set<Move> getWhitePawnMoves(long whitePawns, long emptyCells, long enemyPieces) {
        if (whitePawns == 0) {
            return Collections.emptySet();
        }
        final Set<Move> moves = new HashSet<>();

        long ableToPush = (emptyCells << 8) & whitePawns;
        while (ableToPush != 0) {
            final long position = 1L << Long.numberOfTrailingZeros(ableToPush);
            ableToPush &= ~position;
            moves.add(Move.of(position, position >>> 8));
        }

        long ableToAttackLeft = (enemyPieces << 9) & whitePawns & ~FILE_MASKS[7];
        while (ableToAttackLeft != 0) {
            final long position = 1L << Long.numberOfTrailingZeros(ableToAttackLeft);
            ableToAttackLeft &= ~position;
            moves.add(Move.of(position, position >>> 9));
        }

        long ableToAttackRight = (enemyPieces << 7) & whitePawns & ~FILE_MASKS[0];
        while (ableToAttackRight != 0) {
            final long position = 1L << Long.numberOfTrailingZeros(ableToAttackRight);
            ableToAttackRight &= ~position;
            moves.add(Move.of(position, position >>> 7));
        }

        return moves;
    }

    public Set<Move> getBlackPawnMoves(long blackPawns, long emptyCells, long enemyPieces) {
        if (blackPawns == 0) {
            return Collections.emptySet();
        }
        final Set<Move> moves = new HashSet<>();

        long ableToPush = (emptyCells >>> 8) & blackPawns;
        while (ableToPush != 0) {
            final long position = 1L << Long.numberOfTrailingZeros(ableToPush);
            ableToPush &= ~position;
            moves.add(Move.of(position, position << 8));
        }

        long ableToAttackLeft = (enemyPieces >>> 9) & blackPawns & ~FILE_MASKS[0];
        while (ableToAttackLeft != 0) {
            final long position = 1L << Long.numberOfTrailingZeros(ableToAttackLeft);
            ableToAttackLeft &= ~position;
            moves.add(Move.of(position, position << 9));
        }

        long ableToAttackRight = (enemyPieces >>> 7) & blackPawns & ~FILE_MASKS[7];
        while (ableToAttackRight != 0) {
            final long position = 1L << Long.numberOfTrailingZeros(ableToAttackRight);
            ableToAttackRight &= ~position;
            moves.add(Move.of(position, position << 7));
        }

        return moves;
    }

    public Set<Move> getRookMoves(long rooks, long emptyCells, long enemyPieces) {
        final Set<Move> moves = new HashSet<>();
        while (rooks != 0) {
            final long position = (1L << Long.numberOfTrailingZeros(rooks));
            rooks &= ~position; // Remove the 'position' rook from the 'rooks' bitboard
            final List<Move> movesUp = getRayMoves(position, Direction.UP);
            moves.addAll(filterRayMoves(movesUp, emptyCells, enemyPieces));
            final List<Move> movesDown = getRayMoves(position, Direction.DOWN);
            moves.addAll(filterRayMoves(movesDown, emptyCells, enemyPieces));
            final List<Move> movesLeft = getRayMoves(position, Direction.LEFT);
            moves.addAll(filterRayMoves(movesLeft, emptyCells, enemyPieces));
            final List<Move> movesRight = getRayMoves(position, Direction.RIGHT);
            moves.addAll(filterRayMoves(movesRight, emptyCells, enemyPieces));
        }
        return moves;
    }

    public Set<Move> getBishopMoves(long bishops, long emptyCells, long enemyPieces) {
        final Set<Move> moves = new HashSet<>();
        while (bishops != 0) {
            final long position = (1L << Long.numberOfTrailingZeros(bishops));
            bishops &= ~position; // Remove the 'position' bishop from the 'bishops' bitboard
            final List<Move> movesUpRight = getRayMoves(position, Direction.UP_RIGHT);
            moves.addAll(filterRayMoves(movesUpRight, emptyCells, enemyPieces));
            final List<Move> movesUpLeft = getRayMoves(position, Direction.UP_LEFT);
            moves.addAll(filterRayMoves(movesUpLeft, emptyCells, enemyPieces));
            final List<Move> movesDownRight = getRayMoves(position, Direction.DOWN_RIGHT);
            moves.addAll(filterRayMoves(movesDownRight, emptyCells, enemyPieces));
            final List<Move> movesDownLeft = getRayMoves(position, Direction.DOWN_LEFT);
            moves.addAll(filterRayMoves(movesDownLeft, emptyCells, enemyPieces));
        }
        return moves;
    }

    public Set<Move> getQueenMoves(long queens, long emptyCells, long enemyPieces) {
        final Set<Move> moves = new HashSet<>();
        moves.addAll(getRookMoves(queens, emptyCells, enemyPieces));
        moves.addAll(getBishopMoves(queens, emptyCells, enemyPieces));
        return moves;
    }

    public Set<Move> getKnightMoves(long knights, long emptyCells, long enemyPieces) {
        final Set<Move> moves = new HashSet<>();
        while (knights != 0) {
            final long position = (1L << Long.numberOfTrailingZeros(knights));
            knights &= ~position; // Remove the 'position' knight from the 'knights' bitboard
            long attack = 0L;
            attack |= (position & ~(FILE_MASKS[0] | FILE_MASKS[1] | RANK_MASKS[7])) >>> 10; // Left-top
            attack |= (position & ~(FILE_MASKS[0] | FILE_MASKS[1] | RANK_MASKS[0])) <<  6;  // Left-bottom

            attack |= (position & ~(FILE_MASKS[6] | FILE_MASKS[7] | RANK_MASKS[7])) >>> 6;  // Right-top
            attack |= (position & ~(FILE_MASKS[6] | FILE_MASKS[7] | RANK_MASKS[0])) <<  10; // Right-bottom

            attack |= (position & ~(RANK_MASKS[6] | RANK_MASKS[7] | FILE_MASKS[0])) >>> 17; // Top-left
            attack |= (position & ~(RANK_MASKS[6] | RANK_MASKS[7] | FILE_MASKS[7])) >>> 15; // Top-right

            attack |= (position & ~(RANK_MASKS[0] | RANK_MASKS[1] | FILE_MASKS[0])) <<  15; // Bottom-left
            attack |= (position & ~(RANK_MASKS[0] | RANK_MASKS[1] | FILE_MASKS[7])) <<  17; // Bottom-right

            attack &= (emptyCells | enemyPieces);
            while (attack != 0) {
                final long attackPosition = 1L << Long.numberOfTrailingZeros(attack);
                attack &= ~attackPosition;
                moves.add(Move.of(position, attackPosition));
            }
        }
        return moves;
    }

    public List<Move> getRayMoves(long position, Direction direction) {
        final List<Move> moves = new LinkedList<>();
        long tempPosition = position;
        int tempCell = Long.numberOfTrailingZeros(tempPosition);
        switch (direction) {
            case UP:
                while (tempCell >= 8) {
                    tempPosition = tempPosition >>> 8;
                    tempCell = Long.numberOfTrailingZeros(tempPosition);
                    moves.add(Move.of(position, tempPosition));
                }
                break;
            case DOWN:
                while (tempCell <= 55) {
                    tempPosition = tempPosition << 8;
                    tempCell = Long.numberOfTrailingZeros(tempPosition);
                    moves.add(Move.of(position, tempPosition));
                }
                break;
            case LEFT:
                while (tempCell % 8 > 0) {
                    tempPosition = tempPosition >>> 1;
                    tempCell = Long.numberOfTrailingZeros(tempPosition);
                    moves.add(Move.of(position, tempPosition));
                }
                break;
            case RIGHT:
                while ((tempCell + 1) % 8 > 0) {
                    tempPosition = tempPosition << 1;
                    tempCell = Long.numberOfTrailingZeros(tempPosition);
                    moves.add(Move.of(position, tempPosition));
                }
                break;
            case UP_RIGHT:
                while (tempCell >= 8 && (tempCell + 1) % 8 > 0) {
                    tempPosition = tempPosition >>> 7;
                    tempCell = Long.numberOfTrailingZeros(tempPosition);
                    moves.add(Move.of(position, tempPosition));
                }
                break;
            case UP_LEFT:
                while (tempCell >= 8 && tempCell % 8 > 0) {
                    tempPosition = tempPosition >>> 9;
                    tempCell = Long.numberOfTrailingZeros(tempPosition);
                    moves.add(Move.of(position, tempPosition));
                }
                break;
            case DOWN_RIGHT:
                while (tempCell <= 55 && (tempCell + 1) % 8 > 0) {
                    tempPosition = tempPosition << 9;
                    tempCell = Long.numberOfTrailingZeros(tempPosition);
                    moves.add(Move.of(position, tempPosition));
                }
                break;
            case DOWN_LEFT:
                while (tempCell <= 55 && tempCell % 8 > 0) {
                    tempPosition = tempPosition << 7;
                    tempCell = Long.numberOfTrailingZeros(tempPosition);
                    moves.add(Move.of(position, tempPosition));
                }
                break;
        }
        return moves;
    }

    public Set<Move> filterRayMoves(List<Move> allMoves, long emptyCells, long enemyPieces) {
        final Set<Move> filteredMoves = new HashSet<>();
        for (Move move : allMoves) {
            if ((move.to & emptyCells) != 0) {
                filteredMoves.add(move);
            } else if ((move.to & enemyPieces) != 0) {
                filteredMoves.add(move);
                break;
            } else {
                break;
            }
        }
        return filteredMoves;
    }
}
