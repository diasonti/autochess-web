package fun.diasonti.chessengine.engine;

import fun.diasonti.chessengine.data.ChessBoard;
import fun.diasonti.chessengine.data.Move;
import fun.diasonti.chessengine.util.BoardUtils;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveEngineTest {

    private final MoveEngine moveEngine = new MoveEngine();

    @Test
    void getWhitePawnMovesPush() {
        final String fen = "8/P7/8/8/8/8/8/8";
        final ChessBoard board = BoardUtils.fenToBitboard(fen);
        final Set<Move> moves = moveEngine.getWhitePawnMoves(board.whitePawns, board.getEmptyCells(), board.getBlackPieces());
        assertEquals(1, moves.size());
        assertEquals(moves.stream().findFirst().orElse(null), new Move(1L << 8, 1));
    }

    @Test
    void getWhitePawnMovesBlocked() {
        final String fen = "8/8/8/3p4/3P4/8/8/8";
        final ChessBoard board = BoardUtils.fenToBitboard(fen);
        final Set<Move> moves = moveEngine.getWhitePawnMoves(board.whitePawns, board.getEmptyCells(), board.getBlackPieces());
        assertTrue(moves.isEmpty());
    }

    @Test
    void getWhitePawnMovesDoubleAttack() {
        final String fen = "8/p1p5/1P6/8/8/8/8/8";
        final ChessBoard board = BoardUtils.fenToBitboard(fen);
        final Set<Move> moves = moveEngine.getWhitePawnMoves(board.whitePawns, board.getEmptyCells(), board.getBlackPieces());
        assertEquals(3, moves.size());
        assertTrue(moves.stream().anyMatch(m -> m.equals(new Move(1L << 17, 1L << 8))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(new Move(1L << 17, 1L << 9))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(new Move(1L << 17, 1L << 10))));
    }

    @Test
    void getBlackPawnMovesPush() {
        final String fen = "8/1p6/8/8/8/8/8/8";
        final ChessBoard board = BoardUtils.fenToBitboard(fen);
        final Set<Move> moves = moveEngine.getBlackPawnMoves(board.blackPawns, board.getEmptyCells(), board.getWhitePieces());
        assertEquals(1, moves.size());
        assertEquals(moves.stream().findFirst().orElse(null), new Move(1L << 9, 1L << 17));
    }

    @Test
    void getBlackPawnMovesBlocked() {
        final String fen = "8/8/8/4p3/4P3/8/8/8";
        final ChessBoard board = BoardUtils.fenToBitboard(fen);
        final Set<Move> moves = moveEngine.getBlackPawnMoves(board.blackPawns, board.getEmptyCells(), board.getWhitePieces());
        assertTrue(moves.isEmpty());
    }

    @Test
    void getBlackPawnMovesDoubleAttack() {
        final String fen = "8/1p6/P1P5/8/8/8/8/8";
        final ChessBoard board = BoardUtils.fenToBitboard(fen);
        final Set<Move> moves = moveEngine.getBlackPawnMoves(board.blackPawns, board.getEmptyCells(), board.getWhitePieces());
        assertEquals(3, moves.size());
        assertTrue(moves.stream().anyMatch(m -> m.equals(new Move(1L << 9, 1L << 16))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(new Move(1L << 9, 1L << 17))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(new Move(1L << 9, 1L << 18))));
    }
}
