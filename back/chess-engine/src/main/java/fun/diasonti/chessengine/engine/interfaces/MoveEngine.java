package fun.diasonti.chessengine.engine.interfaces;

import fun.diasonti.chessengine.data.ChessBoard;
import fun.diasonti.chessengine.data.Color;
import fun.diasonti.chessengine.data.Move;

import java.util.Set;

public interface MoveEngine {

    /**
     * Apply the move to the board
     * @param board board for the move to apply to
     * @param move move to apply
     * @return new board object with the move been applied
     */
    ChessBoard makeMove(ChessBoard board, Move move);

    /**
     * Find available moves for one of the players
     * @param board board to search moves on
     * @param color color of the player whose moves to search
     * @return Set of available moves
     */
    Set<Move> getAvailableMoves(ChessBoard board, Color color);

}
