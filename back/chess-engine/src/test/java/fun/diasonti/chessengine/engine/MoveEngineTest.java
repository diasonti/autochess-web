package fun.diasonti.chessengine.engine;

import fun.diasonti.chessengine.data.ChessBoard;
import fun.diasonti.chessengine.data.Direction;
import fun.diasonti.chessengine.data.Move;
import fun.diasonti.chessengine.util.BoardUtils;
import org.junit.jupiter.api.Test;

import java.util.List;
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
        assertEquals(new Move(1L << 8, 1), moves.stream().findFirst().orElse(null));
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
        assertEquals(new Move(1L << 9, 1L << 17), moves.stream().findFirst().orElse(null));
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

    @Test
    void getRayMoves() {
        final List<Move> movesUp = moveEngine.getRayMoves(1L << 25, Direction.UP);
        assertEquals(3, movesUp.size());
        assertEquals(new Move(1L << 25, 1L << 17), movesUp.get(0));
        assertEquals(new Move(1L << 25, 1L << 9), movesUp.get(1));
        assertEquals(new Move(1L << 25, 1L << 1), movesUp.get(2));

        final List<Move> movesDown = moveEngine.getRayMoves(1L << 37, Direction.DOWN);
        assertEquals(3, movesDown.size());
        assertEquals(new Move(1L << 37, 1L << 45), movesDown.get(0));
        assertEquals(new Move(1L << 37, 1L << 53), movesDown.get(1));
        assertEquals(new Move(1L << 37, 1L << 61), movesDown.get(2));

        final List<Move> movesLeft = moveEngine.getRayMoves(1L << 35, Direction.LEFT);
        assertEquals(3, movesLeft.size());
        assertEquals(new Move(1L << 35, 1L << 34), movesLeft.get(0));
        assertEquals(new Move(1L << 35, 1L << 33), movesLeft.get(1));
        assertEquals(new Move(1L << 35, 1L << 32), movesLeft.get(2));

        final List<Move> movesRight = moveEngine.getRayMoves(1L << 52, Direction.RIGHT);
        assertEquals(3, movesRight.size());
        assertEquals(new Move(1L << 52, 1L << 53), movesRight.get(0));
        assertEquals(new Move(1L << 52, 1L << 54), movesRight.get(1));
        assertEquals(new Move(1L << 52, 1L << 55), movesRight.get(2));
    }

    @Test
    void getRookMoves() {
        final String fen = "8/8/2P4p/1PRP4/2P3NR/2R1N2B/8/1NrN4";
        final ChessBoard board = BoardUtils.fenToBitboard(fen);
        final Set<Move> moves = moveEngine.getRookMoves(board.whiteRooks, board.getEmptyCells(), board.getBlackPieces());
        assertEquals(7, moves.size());
        assertTrue(moves.stream().anyMatch(m -> m.equals(new Move(1L << 39, 1L << 31))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(new Move(1L << 39, 1L << 23))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(new Move(1L << 42, 1L << 43))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(new Move(1L << 42, 1L << 50))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(new Move(1L << 42, 1L << 58))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(new Move(1L << 42, 1L << 41))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(new Move(1L << 42, 1L << 40))));
    }
}
