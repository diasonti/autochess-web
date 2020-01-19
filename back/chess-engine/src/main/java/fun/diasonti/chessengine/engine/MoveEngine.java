package fun.diasonti.chessengine.engine;

import fun.diasonti.chessengine.data.ChessBoard;
import fun.diasonti.chessengine.data.Move;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

    public Set<Move> getWhitePawnMoves(long whitePawns, long emptyCells, long enemyPieces) {
        if (whitePawns == 0) {
            return Collections.emptySet();
        }
        final Set<Move> moves = new HashSet<>();

        final long ableToPush = (emptyCells << 8) & whitePawns;
        for (int i = 0; i < ChessBoard.SIZE; i++) {
            boolean isAbleToPush = ((ableToPush >>> i) & 1L) == 1L;
            if (isAbleToPush) {
                long from = 1 << i;
                long to = 1 << (i - 8);
                moves.add(new Move(from, to));
            }
        }

        final long ableToAttackLeft = (enemyPieces << 9) & whitePawns & ~FILE_MASKS[7];
        for (int i = 0; i < ChessBoard.SIZE; i++) {
            boolean isAbleAttackLeft = ((ableToAttackLeft >>> i) & 1L) == 1L;
            if (isAbleAttackLeft) {
                long from = 1 << i;
                long to = 1 << (i - 9);
                moves.add(new Move(from, to));
            }
        }

        final long ableToAttackRight = (enemyPieces << 7) & whitePawns & ~FILE_MASKS[0];
        for (int i = 0; i < ChessBoard.SIZE; i++) {
            boolean isAbleAttackRight = ((ableToAttackRight >>> i) & 1L) == 1L;
            if (isAbleAttackRight) {
                long from = 1 << i;
                long to = 1 << (i - 7);
                moves.add(new Move(from, to));
            }
        }

        return moves;
    }
}
