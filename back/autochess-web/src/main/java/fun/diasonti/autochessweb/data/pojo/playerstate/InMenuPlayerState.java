package fun.diasonti.autochessweb.data.pojo.playerstate;

import fun.diasonti.autochessweb.data.enums.PlayerStateStatus;
import fun.diasonti.autochessweb.data.pojo.FinishedGame;
import fun.diasonti.autochessweb.data.pojo.GlobalStatistics;
import fun.diasonti.autochessweb.data.pojo.SearchStatus;
import fun.diasonti.autochessweb.data.view.PlayerView;

public class InMenuPlayerState extends PlayerState {

    private PlayerView profile;
    private SearchStatus search;
    private GlobalStatistics statistics;
    private FinishedGame finishedGame;

    public InMenuPlayerState() {
        super(PlayerStateStatus.MENU);
    }

    public PlayerView getProfile() {
        return profile;
    }

    public void setProfile(PlayerView profile) {
        this.profile = profile;
    }

    public SearchStatus getSearch() {
        return search;
    }

    public void setSearch(SearchStatus search) {
        this.search = search;
    }

    public GlobalStatistics getStatistics() {
        return statistics;
    }

    public void setStatistics(GlobalStatistics statistics) {
        this.statistics = statistics;
    }

    public FinishedGame getFinishedGame() {
        return finishedGame;
    }

    public void setFinishedGame(FinishedGame finishedGame) {
        this.finishedGame = finishedGame;
    }
}
