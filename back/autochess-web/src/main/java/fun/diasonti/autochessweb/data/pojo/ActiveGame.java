package fun.diasonti.autochessweb.data.pojo;

import fun.diasonti.autochessweb.data.enums.ActiveGameState;
import fun.diasonti.autochessweb.data.form.UserAccountForm;
import fun.diasonti.chessengine.data.ChessBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ActiveGame {

    private String id;
    private ActiveGameState state;

    private UserAccountForm whitePlayer;
    private UserAccountForm blackPlayer;

    private List<MovablePiece> whitePieces;
    private List<MovablePiece> blackPieces;

    private ChessBoard board;

    public ActiveGame() {
        this.id = UUID.randomUUID().toString();
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

    public ChessBoard getBoard() {
        return board;
    }

    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    public List<MovablePiece> getWhitePieces() {
        return whitePieces;
    }

    public void setWhitePieces(String pieces) {
        whitePieces = new ArrayList<>(pieces.length());
        final char[] chars = pieces.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            whitePieces.add(new MovablePiece(i, chars[i]));
        }
    }

    public List<MovablePiece> getBlackPieces() {
        return blackPieces;
    }

    public void setBlackPieces(String pieces) {
        blackPieces = new ArrayList<>(pieces.length());
        final char[] chars = pieces.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            blackPieces.add(new MovablePiece(i + 5, chars[i]));
        }
    }
}
