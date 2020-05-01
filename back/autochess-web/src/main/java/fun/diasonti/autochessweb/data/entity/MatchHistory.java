package fun.diasonti.autochessweb.data.entity;

import fun.diasonti.autochessweb.data.entity.base.BaseEntity;
import fun.diasonti.autochessweb.data.pojo.ActiveGame;
import fun.diasonti.chessengine.data.Color;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "match_history")
public class MatchHistory extends BaseEntity {

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @ManyToOne
    @JoinColumn(name = "white_player_id")
    private UserAccount whitePlayer;

    @ManyToOne
    @JoinColumn(name = "black_player_id")
    private UserAccount blackPlayer;

    @Column(name = "white_rank_delta")
    private int whiteRankDelta;

    @Column(name = "black_rank_delta")
    private int blackRankDelta;

    @Column(name = "winner")
    @Enumerated(EnumType.STRING)
    private Color winner;

    @Transient
    private ActiveGame activeGame;

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

    public UserAccount getWhitePlayer() {
        return whitePlayer;
    }

    public void setWhitePlayer(UserAccount whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    public UserAccount getBlackPlayer() {
        return blackPlayer;
    }

    public void setBlackPlayer(UserAccount blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    public int getWhiteRankDelta() {
        return whiteRankDelta;
    }

    public void setWhiteRankDelta(int whiteRankDelta) {
        this.whiteRankDelta = whiteRankDelta;
    }

    public int getBlackRankDelta() {
        return blackRankDelta;
    }

    public void setBlackRankDelta(int blackRankDelta) {
        this.blackRankDelta = blackRankDelta;
    }

    public Color getWinner() {
        return winner;
    }

    public void setWinner(Color winner) {
        this.winner = winner;
    }

    public ActiveGame getActiveGame() {
        return activeGame;
    }

    public void setActiveGame(ActiveGame activeGame) {
        this.activeGame = activeGame;
    }
}
