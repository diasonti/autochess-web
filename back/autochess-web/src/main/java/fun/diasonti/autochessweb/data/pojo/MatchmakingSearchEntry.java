package fun.diasonti.autochessweb.data.pojo;

import fun.diasonti.autochessweb.data.form.UserAccountForm;

import java.util.Date;
import java.util.Objects;

public class MatchmakingSearchEntry {

    private final UserAccountForm player;
    private final long createdAt;

    public MatchmakingSearchEntry(UserAccountForm player) {
        this.player = player;
        this.createdAt = (new Date().getTime() / 1000);
    }

    public UserAccountForm getPlayer() {
        return player;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof MatchmakingSearchEntry))
            return false;
        MatchmakingSearchEntry that = (MatchmakingSearchEntry) o;
        return Objects.equals(player, that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player);
    }
}
