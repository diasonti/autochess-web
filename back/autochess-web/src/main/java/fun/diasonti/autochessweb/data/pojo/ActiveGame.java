package fun.diasonti.autochessweb.data.pojo;

import fun.diasonti.autochessweb.data.enums.ActiveGameState;
import fun.diasonti.autochessweb.data.form.UserAccountForm;
import fun.diasonti.chessengine.data.ChessBoard;
import fun.diasonti.chessengine.util.BoardUtils;

import java.time.LocalDateTime;
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

    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

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

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public void setupStartingBoard() {
        final StringBuilder black = new StringBuilder();
        for (int i = 0; i < blackPieces.size(); i++) {
            blackPieces.get(i).setPosition(i);
            black.append(blackPieces.get(i).getPiece());
        }
        final StringBuilder white = new StringBuilder();
        for (int i = 0; i < whitePieces.size(); i++) {
            whitePieces.get(i).setPosition(63 - i);
            white.insert(0, whitePieces.get(i).getPiece());
        }
        final String startingFen = black + "3/8/8/8/8/8/8/3" + white;
        board = BoardUtils.fenToBitboard(startingFen);
    }
}
