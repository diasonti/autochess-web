package fun.diasonti.autochessweb.data.pojo;

import fun.diasonti.autochessweb.data.form.UserAccountForm;

import java.util.function.Consumer;

public class MatchmakingSearchEntry {

    private final UserAccountForm player;
    private final Consumer<String> onGameFoundCallback;

    public MatchmakingSearchEntry(UserAccountForm player, Consumer<String> onGameFoundCallback) {
        this.player = player;
        this.onGameFoundCallback = onGameFoundCallback;
    }

    public UserAccountForm getPlayer() {
        return player;
    }

    public Consumer<String> getOnGameFoundCallback() {
        return onGameFoundCallback;
    }
}
