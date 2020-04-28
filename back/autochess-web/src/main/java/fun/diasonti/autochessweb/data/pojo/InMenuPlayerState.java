package fun.diasonti.autochessweb.data.pojo;

import fun.diasonti.autochessweb.data.enums.PlayerStateStatus;
import fun.diasonti.autochessweb.data.view.PlayerView;

public class InMenuPlayerState extends PlayerState {

    private PlayerView profile;
    private SearchStatus search;
    private GlobalStatistics statistics;

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
}
