package fun.diasonti.autochessweb.data.pojo;

import fun.diasonti.autochessweb.data.enums.PlayerStateStatus;

public abstract class PlayerState {

    private final PlayerStateStatus status;

    protected PlayerState(PlayerStateStatus status) {
        this.status = status;
    }

    public PlayerStateStatus getStatus() {
        return status;
    }
}
