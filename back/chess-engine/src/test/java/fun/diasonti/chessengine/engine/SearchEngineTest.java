package fun.diasonti.chessengine.engine;

import fun.diasonti.chessengine.data.ChessBoard;
import fun.diasonti.chessengine.data.Color;
import fun.diasonti.chessengine.data.Move;
import fun.diasonti.chessengine.util.BoardUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchEngineTest {

    private final MinimaxAlphaBetaSearchEngine searchEngine = new MinimaxAlphaBetaSearchEngine();

    @Test
    public void testSearchMoveWhiteTest() {
        final String fen = "p6p/8/8/3r4/4P3/8/8/5P2";
        final ChessBoard board = BoardUtils.fenToBitboard(fen);
        final Move foundMove = searchEngine.getBestMove(board, Color.WHITE, 4);
        final String fenAfterMove = BoardUtils.bitboardToFen(board);
        assertEquals(Move.of(1L << 36, 1L << 27), foundMove);
        assertEquals(fen, fenAfterMove);
    }

}
