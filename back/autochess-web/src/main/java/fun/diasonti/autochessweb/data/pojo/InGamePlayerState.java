package fun.diasonti.autochessweb.data.pojo;

import fun.diasonti.autochessweb.data.enums.ActiveGameState;
import fun.diasonti.autochessweb.data.enums.PlayerStateStatus;
import fun.diasonti.autochessweb.data.view.PlayerView;

import java.util.List;

public class InGamePlayerState extends PlayerState {

    private ActiveGameState stage;
    private String board;
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
