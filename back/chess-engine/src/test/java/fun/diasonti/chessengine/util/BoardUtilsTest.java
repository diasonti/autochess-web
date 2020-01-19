package fun.diasonti.chessengine.util;

import fun.diasonti.chessengine.data.ChessBoard;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardUtilsTest {

    private static final Logger log = LoggerFactory.getLogger(BoardUtilsTest.class);
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    @Test
    void fenToBitboardAndBack() {
        final String fenBefore = "rnbqkbnr/p1p1p2p/8/8/1p2ppp1/1P3PPP/P1PP4/RNBQKBNK";
        final ChessBoard board = BoardUtils.fenToBitboard(fenBefore);
        final String fenAfter = BoardUtils.bitboardToFen(board);
        assertEquals(fenBefore, fenAfter);
    }

    @Test
    void print() {
        final String fenBefore = "rnbqkbnr/p1p1p2p/8/8/1p2ppp1/1P3PPP/P1PP4/RNBQKBNK";
        final ChessBoard board = BoardUtils.fenToBitboard(fenBefore);
        final String boardString = BoardUtils.boardToString(board);
        final String expected =
                "r n b q k b n r " + LINE_SEPARATOR +
                "p . p . p . . p " + LINE_SEPARATOR +
                ". . . . . . . . " + LINE_SEPARATOR +
                ". . . . . . . . " + LINE_SEPARATOR +
                ". p . . p p p . " + LINE_SEPARATOR +
                ". P . . . P P P " + LINE_SEPARATOR +
                "P . P P . . . . " + LINE_SEPARATOR +
                "R N B Q K B N K ";
        assertEquals(expected, boardString);
    }
}
