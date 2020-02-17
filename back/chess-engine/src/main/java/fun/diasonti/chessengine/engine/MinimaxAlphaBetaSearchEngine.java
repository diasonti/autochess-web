package fun.diasonti.chessengine.engine;

import fun.diasonti.chessengine.data.ChessBoard;
import fun.diasonti.chessengine.data.Color;
import fun.diasonti.chessengine.data.Move;
import fun.diasonti.chessengine.engine.interfaces.EvaluationEngine;
import fun.diasonti.chessengine.engine.interfaces.MoveEngine;
import fun.diasonti.chessengine.engine.interfaces.SearchEngine;

import java.util.Set;

public class MinimaxAlphaBetaSearchEngine implements SearchEngine {

    private EvaluationEngine evaluationEngine;
    private MoveEngine moveEngine;

    public void setEvaluationEngine(EvaluationEngine evaluationEngine) {
        this.evaluationEngine = evaluationEngine;
    }

    public void setMoveEngine(MoveEngine moveEngine) {
        this.moveEngine = moveEngine;
    }

    @Override
    public Move getBestMove(ChessBoard board, Color color, int maxDepth) {
        if (evaluationEngine == null) {
            throw new IllegalStateException("Evaluation engine cannot be null");
        }
        if (moveEngine == null) {
            throw new IllegalStateException("Move engine cannot be null");
        }
        return minimaxAlphaBeta(board, color, 0, -10_000, 10_000, null, maxDepth);
    }

    private Move minimaxAlphaBeta(ChessBoard board, Color color, int depth, int alpha, int beta, Move move, int maxDepth) {
        if (depth == maxDepth) {
            move.score = evaluationEngine.getScore(board);
            return move;
        }

        final Set<Move> moves = moveEngine.getAvailableMoves(board, color);

        Move alphaMove = Move.empty();
        Move betaMove = Move.empty();
        for (Move m : moves) {
            final ChessBoard boardAfterMove = moveEngine.makeMove(board, m);
            final Move bestMove = minimaxAlphaBeta(boardAfterMove, color.getOpposite(), depth + 1 , alpha, beta, m, maxDepth);
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
