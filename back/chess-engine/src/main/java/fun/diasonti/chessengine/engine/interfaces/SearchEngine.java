package fun.diasonti.chessengine.engine.interfaces;

import fun.diasonti.chessengine.data.ChessBoard;
import fun.diasonti.chessengine.data.Color;
import fun.diasonti.chessengine.data.Move;

public interface SearchEngine {

    /**
     * Get the best move for the player
     * @param board board to search a move on
     * @param color color of the player whose move to search
     * @param maxDepth maximum depth of search
     * @return the best move
     */
    Move getBestMove(ChessBoard board, Color color, int maxDepth);
}
