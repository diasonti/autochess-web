package fun.diasonti.autochessweb.data.view;

import java.util.List;

public class PlayerView {

    private String username;
    private int rank;
    private List<MatchView> matchHistory;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public List<MatchView> getMatchHistory() {
        return matchHistory;
    }

    public void setMatchHistory(List<MatchView> matchHistory) {
        this.matchHistory = matchHistory;
    }
}
