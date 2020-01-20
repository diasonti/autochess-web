package fun.diasonti.chessengine.engine.interfaces;

import fun.diasonti.chessengine.data.ChessBoard;

public interface EvaluationEngine {
    /**
     * Get the board's score using current pieces' placement
     * @param board the board to evaluate
     * @return score, where >0 - white advantage, <0 - black advantage
     */
    int getScore(ChessBoard board);
}
