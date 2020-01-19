package fun.diasonti.chessengine.engine;

import fun.diasonti.chessengine.data.ChessBoard;
import fun.diasonti.chessengine.data.Color;
import fun.diasonti.chessengine.data.Move;

import java.util.Set;

public class MinimaxAlphaBetaSearchEngine {

    private static final int MAX_DEPTH = 4;

    private static EvaluationEngine evaluationEngine = new EvaluationEngine();
    private static MoveEngine moveEngine = new MoveEngine();

    public Move getMove(ChessBoard board, Color color) {
        return minimaxAlphaBeta(board, color, 0, -10_000, 10_000, null);
    }

    private Move minimaxAlphaBeta(ChessBoard board, Color color, int depth, int alpha, int beta, Move move) {
        if (depth == MAX_DEPTH) {
            move.score = evaluationEngine.getScore(board);
            return move;
        }

        final Set<Move> moves = moveEngine.getAvailableMoves(board, color);

        Move alphaMove = Move.empty();
        Move betaMove = Move.empty();
        for (Move m : moves) {
            final ChessBoard boardAfterMove = moveEngine.makeMove(board, m);
            final Move bestMove = minimaxAlphaBeta(boardAfterMove, color.getOpposite(), depth + 1 , alpha, beta, m);
            final int bestScore = bestMove.score;
            if (color == Color.WHITE) {
                if (bestScore > alpha) {
                    alpha = bestScore;
                    alphaMove = m;
                }
            } else {
                if (bestScore < beta) {
                    beta = bestScore;
                    betaMove = m;
                }
            }

            if (alpha >= beta) {
                if (color == Color.WHITE) {
                    alphaMove.score = alpha;
                    return alphaMove;
                } else {
                    betaMove.score = beta;
                    return betaMove;
                }
            }
        }
        if (color == Color.WHITE) {
            alphaMove.score = alpha;
            return alphaMove;
        } else {
            betaMove.score = beta;
            return betaMove;
        }
    }

}
