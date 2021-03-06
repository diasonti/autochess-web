package fun.diasonti.chessengine.engine;

import fun.diasonti.chessengine.data.ChessBoard;
import fun.diasonti.chessengine.data.Color;
import fun.diasonti.chessengine.data.Direction;
import fun.diasonti.chessengine.data.Move;
import fun.diasonti.chessengine.util.BoardUtils;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BitwiseOperationsMoveEngineTest {

    private final BitwiseOperationsMoveEngine moveEngine = new BitwiseOperationsMoveEngine();

    @Test
    public void testMakeMoveWhite() {
        final String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
        final ChessBoard board = BoardUtils.fenToBitboard(fen);
        final ChessBoard boardAfterMove = moveEngine.makeMove(board, Move.of(1L << 57, 1L << 42));
        final String fenAfterMove = BoardUtils.bitboardToFen(boardAfterMove);
        assertEquals("rnbqkbnr/pppppppp/8/8/8/2N5/PPPPPPPP/R1BQKBNR", fenAfterMove);
    }

    @Test
    public void testMakeMoveBlack() {
        final String fen = "rnbqkbnr/pppp1ppp/8/4p3/8/2N5/PPPPPPPP/R1BQKBNR";
        final ChessBoard board = BoardUtils.fenToBitboard(fen);
        final ChessBoard boardAfterMove = moveEngine.makeMove(board, Move.of(1L << 5, 1L << 33));
        final String fenAfterMove = BoardUtils.bitboardToFen(boardAfterMove);
        assertEquals("rnbqk1nr/pppp1ppp/8/4p3/1b6/2N5/PPPPPPPP/R1BQKBNR", fenAfterMove);
    }

    @Test
    public void testGetAvailableMovesWhite() {
        final String fen = "rnbq1bnr/pppppppp/8/5N2/P3P3/R7/1PPP1PPP/1NBQ1B1R";
        final ChessBoard board = BoardUtils.fenToBitboard(fen);
        final Set<Move> moves = moveEngine.getAvailableMoves(board, Color.WHITE);
        assertEquals(37, moves.size());
    }

    @Test
    public void testGetAvailableMovesBlack() {
        final String fen = "2bq1bnr/1ppp1pp1/r3p3/pn5p/8/8/PPPPPPPP/RNBQ1BNR";
        final ChessBoard board = BoardUtils.fenToBitboard(fen);
        final Set<Move> moves = moveEngine.getAvailableMoves(board, Color.BLACK);
        assertEquals(33, moves.size());
    }

    @Test
    public void testGetWhitePawnMovesPush() {
        final String fen = "8/P7/8/8/8/8/8/8";
        final ChessBoard board = BoardUtils.fenToBitboard(fen);
        final Set<Move> moves = moveEngine.getWhitePawnMoves(board.whitePawns, board.getEmptyCells(), board.getBlackPieces());
        assertEquals(1, moves.size());
        assertEquals(Move.of(1L << 8, 1), moves.stream().findFirst().orElse(null));
    }

    @Test
    public void testGetWhitePawnMovesBlocked() {
        final String fen = "8/8/8/3p4/3P4/8/8/8";
        final ChessBoard board = BoardUtils.fenToBitboard(fen);
        final Set<Move> moves = moveEngine.getWhitePawnMoves(board.whitePawns, board.getEmptyCells(), board.getBlackPieces());
        assertTrue(moves.isEmpty());
    }

    @Test
    public void testGetWhitePawnMovesDoubleAttack() {
        final String fen = "8/p1p5/1P6/8/8/8/8/8";
        final ChessBoard board = BoardUtils.fenToBitboard(fen);
        final Set<Move> moves = moveEngine.getWhitePawnMoves(board.whitePawns, board.getEmptyCells(), board.getBlackPieces());
        assertEquals(3, moves.size());
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 17, 1L << 8))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 17, 1L << 9))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 17, 1L << 10))));
    }

    @Test
    public void testGetBlackPawnMovesPush() {
        final String fen = "8/1p6/8/8/8/8/8/8";
        final ChessBoard board = BoardUtils.fenToBitboard(fen);
        final Set<Move> moves = moveEngine.getBlackPawnMoves(board.blackPawns, board.getEmptyCells(), board.getWhitePieces());
        assertEquals(1, moves.size());
        assertEquals(Move.of(1L << 9, 1L << 17), moves.stream().findFirst().orElse(null));
    }

    @Test
    public void testGetBlackPawnMovesBlocked() {
        final String fen = "8/8/8/4p3/4P3/8/8/8";
        final ChessBoard board = BoardUtils.fenToBitboard(fen);
        final Set<Move> moves = moveEngine.getBlackPawnMoves(board.blackPawns, board.getEmptyCells(), board.getWhitePieces());
        assertTrue(moves.isEmpty());
    }

    @Test
    public void testGetBlackPawnMovesDoubleAttack() {
        final String fen = "8/1p6/P1P5/8/8/8/8/8";
        final ChessBoard board = BoardUtils.fenToBitboard(fen);
        final Set<Move> moves = moveEngine.getBlackPawnMoves(board.blackPawns, board.getEmptyCells(), board.getWhitePieces());
        assertEquals(3, moves.size());
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 9, 1L << 16))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 9, 1L << 17))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 9, 1L << 18))));
    }

    @Test
    public void testGetRayMoves() {
        final List<Move> movesUp = moveEngine.getRayMoves(1L << 25, Direction.UP);
        assertEquals(3, movesUp.size());
        assertEquals(Move.of(1L << 25, 1L << 17), movesUp.get(0));
        assertEquals(Move.of(1L << 25, 1L << 9), movesUp.get(1));
        assertEquals(Move.of(1L << 25, 1L << 1), movesUp.get(2));

        final List<Move> movesDown = moveEngine.getRayMoves(1L << 37, Direction.DOWN);
        assertEquals(3, movesDown.size());
        assertEquals(Move.of(1L << 37, 1L << 45), movesDown.get(0));
        assertEquals(Move.of(1L << 37, 1L << 53), movesDown.get(1));
        assertEquals(Move.of(1L << 37, 1L << 61), movesDown.get(2));

        final List<Move> movesLeft = moveEngine.getRayMoves(1L << 35, Direction.LEFT);
        assertEquals(3, movesLeft.size());
        assertEquals(Move.of(1L << 35, 1L << 34), movesLeft.get(0));
        assertEquals(Move.of(1L << 35, 1L << 33), movesLeft.get(1));
        assertEquals(Move.of(1L << 35, 1L << 32), movesLeft.get(2));

        final List<Move> movesRight = moveEngine.getRayMoves(1L << 52, Direction.RIGHT);
        assertEquals(3, movesRight.size());
        assertEquals(Move.of(1L << 52, 1L << 53), movesRight.get(0));
        assertEquals(Move.of(1L << 52, 1L << 54), movesRight.get(1));
        assertEquals(Move.of(1L << 52, 1L << 55), movesRight.get(2));

        final List<Move> movesUpRight = moveEngine.getRayMoves(1L << 52, Direction.UP_RIGHT);
        assertEquals(3, movesUpRight.size());
        assertEquals(Move.of(1L << 52, 1L << 45), movesUpRight.get(0));
        assertEquals(Move.of(1L << 52, 1L << 38), movesUpRight.get(1));
        assertEquals(Move.of(1L << 52, 1L << 31), movesUpRight.get(2));

        final List<Move> movesUpLeft = moveEngine.getRayMoves(1L << 29, Direction.UP_LEFT);
        assertEquals(3, movesUpLeft.size());
        assertEquals(Move.of(1L << 29, 1L << 20), movesUpLeft.get(0));
        assertEquals(Move.of(1L << 29, 1L << 11), movesUpLeft.get(1));
        assertEquals(Move.of(1L << 29, 1L << 2), movesUpLeft.get(2));

        final List<Move> movesDownRight = moveEngine.getRayMoves(1L << 12, Direction.DOWN_RIGHT);
        assertEquals(3, movesDownRight.size());
        assertEquals(Move.of(1L << 12, 1L << 21), movesDownRight.get(0));
        assertEquals(Move.of(1L << 12, 1L << 30), movesDownRight.get(1));
        assertEquals(Move.of(1L << 12, 1L << 39), movesDownRight.get(2));

        final List<Move> movesDownLeft = moveEngine.getRayMoves(1L << 39, Direction.DOWN_LEFT);
        assertEquals(3, movesDownLeft.size());
        assertEquals(Move.of(1L << 39, 1L << 46), movesDownLeft.get(0));
        assertEquals(Move.of(1L << 39, 1L << 53), movesDownLeft.get(1));
        assertEquals(Move.of(1L << 39, 1L << 60), movesDownLeft.get(2));
    }

    @Test
    public void testGetRookMoves() {
        final String fen = "8/8/2P4p/1PRP4/2P3NR/2R1N2B/8/1NrN4";
        final ChessBoard board = BoardUtils.fenToBitboard(fen);
        final Set<Move> moves = moveEngine.getRookMoves(board.whiteRooks, board.getEmptyCells(), board.getBlackPieces());
        assertEquals(7, moves.size());
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 39, 1L << 31))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 39, 1L << 23))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 42, 1L << 43))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 42, 1L << 50))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 42, 1L << 58))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 42, 1L << 41))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 42, 1L << 40))));
    }

    @Test
    public void testGetBishopMoves() {
        final String fen = "8/7p/6B1/6NQ/1N2p3/2P1N3/P2B4/BPp1N3";
        final ChessBoard board = BoardUtils.fenToBitboard(fen);
        final Set<Move> moves = moveEngine.getBishopMoves(board.whiteBishops, board.getEmptyCells(), board.getBlackPieces());
        assertEquals(7, moves.size());
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 22, 1L << 15))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 22, 1L << 13))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 22, 1L << 4))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 22, 1L << 29))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 22, 1L << 36))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 56, 1L << 49))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 51, 1L << 58))));
    }

    @Test
    public void testGetQueenMoves() {
        final String fen = "6qQ/6P1/7P/4p3/2b5/1PQ1n3/1BP5/8";
        final ChessBoard board = BoardUtils.fenToBitboard(fen);
        final Set<Move> moves = moveEngine.getQueenMoves(board.whiteQueens, board.getEmptyCells(), board.getBlackPieces());
        assertEquals(11, moves.size());
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 7, 1L << 6))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 7, 1L << 15))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 42, 1L << 34))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 42, 1L << 43))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 42, 1L << 44))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 42, 1L << 35))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 42, 1L << 28))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 42, 1L << 33))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 42, 1L << 24))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 42, 1L << 51))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 42, 1L << 60))));
    }

    @Test
    public void testGetKnightMoves() {
        final String fen = "1p2p3/2p4p/N4N2/2p4p/1p2p1P1/1p6/2P5/N7";
        final ChessBoard board = BoardUtils.fenToBitboard(fen);
        final Set<Move> moves = moveEngine.getKnightMoves(board.whiteKnights, board.getEmptyCells(), board.getBlackPieces());
        assertEquals(12, moves.size());
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 21, 1L << 4))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 21, 1L << 6))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 21, 1L << 11))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 21, 1L << 15))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 21, 1L << 31))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 21, 1L << 27))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 21, 1L << 36))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 16, 1L << 1))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 16, 1L << 10))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 16, 1L << 26))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 16, 1L << 33))));
        assertTrue(moves.stream().anyMatch(m -> m.equals(Move.of(1L << 56, 1L << 41))));
    }
}
