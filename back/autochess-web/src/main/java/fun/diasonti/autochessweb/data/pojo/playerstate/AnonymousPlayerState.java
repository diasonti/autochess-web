package fun.diasonti.autochessweb.data.pojo.playerstate;

import fun.diasonti.autochessweb.data.enums.PlayerStateStatus;

public class AnonymousPlayerState extends PlayerState {

    private static final AnonymousPlayerState INSTANCE = new AnonymousPlayerState();

    private AnonymousPlayerState() {
        super(PlayerStateStatus.ANONYMOUS);
    }

    public static AnonymousPlayerState getInstance() {
        return INSTANCE;
    }
}
