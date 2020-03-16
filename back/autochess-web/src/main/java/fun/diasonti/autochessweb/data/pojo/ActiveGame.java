package fun.diasonti.autochessweb.data.pojo;

import fun.diasonti.autochessweb.data.enums.ActiveGameState;
import fun.diasonti.autochessweb.data.form.UserAccountForm;
import fun.diasonti.chessengine.data.ChessBoard;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class ActiveGame {

    private String id;
    private ActiveGameState state;

    private UserAccountForm whitePlayer;
    private UserAccountForm blackPlayer;

    private String whitePieces;
    private String blackPieces;

    private ChessBoard board;
    private List<ChessMove> moves;

    public ActiveGame() {
        this.id = UUID.randomUUID().toString();
        moves = new LinkedList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ActiveGameState getState() {
        return state;
    }

    public void setState(ActiveGameState state) {
        this.state = state;
    }

    public UserAccountForm getWhitePlayer() {
        return whitePlayer;
    }

    public void setWhitePlayer(UserAccountForm whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    public UserAccountForm getBlackPlayer() {
        return blackPlayer;
    }

    public void setBlackPlayer(UserAccountForm blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    public String getWhitePieces() {
        return whitePieces;
    }

    public void setWhitePieces(String whitePieces) {
        this.whitePieces = whitePieces;
    }

    public String getBlackPieces() {
        return blackPieces;
    }

    public void setBlackPieces(String blackPieces) {
        this.blackPieces = blackPieces;
    }

    public ChessBoard getBoard() {
        return board;
    }

    public void setBoard(ChessBoard board) {
        this.board = board;
    }
}
