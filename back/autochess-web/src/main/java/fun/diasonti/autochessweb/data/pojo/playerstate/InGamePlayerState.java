package fun.diasonti.autochessweb.data.pojo.playerstate;

import fun.diasonti.autochessweb.data.enums.ActiveGameState;
import fun.diasonti.autochessweb.data.enums.PlayerStateStatus;
import fun.diasonti.autochessweb.data.pojo.MovablePiece;
import fun.diasonti.autochessweb.data.view.PlayerView;
import fun.diasonti.chessengine.data.Color;

import java.util.List;

public class InGamePlayerState extends PlayerState {

    private ActiveGameState stage;
    private String board;
    private Color color;
    private List<MovablePiece> movablePieces;
    private PlayerView opponentProfile;

    public InGamePlayerState() {
        super(PlayerStateStatus.GAME);
    }

    public ActiveGameState getStage() {
        return stage;
    }

    public void setStage(ActiveGameState stage) {
        this.stage = stage;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List<MovablePiece> getMovablePieces() {
        return movablePieces;
    }

    public void setMovablePieces(List<MovablePiece> movablePieces) {
        this.movablePieces = movablePieces;
    }

    public PlayerView getOpponentProfile() {
        return opponentProfile;
    }

    public void setOpponentProfile(PlayerView opponentProfile) {
        this.opponentProfile = opponentProfile;
    }
}
