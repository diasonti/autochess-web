package fun.diasonti.autochessweb.engine;

import fun.diasonti.autochessweb.data.form.UserAccountForm;
import fun.diasonti.autochessweb.engine.game.GameService;
import fun.diasonti.autochessweb.service.UserAccountService;
import fun.diasonti.autochessweb.utils.HashLinkedQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MatchmakingService {

    private final HashLinkedQueue<UserAccountForm> queue = new HashLinkedQueue<>();

    private final GameService gameService;
    private final ConnectionService connectionService;
    private final UserAccountService userAccountService;

    @Autowired
    public MatchmakingService(GameService gameService, ConnectionService connectionService, UserAccountService userAccountService) {
        this.gameService = gameService;
        this.connectionService = connectionService;
        this.userAccountService = userAccountService;
    }

    public boolean addToSearchQueue(String username) {
        final UserAccountForm user = userAccountService.getByUsername(username);
        synchronized (queue) {
            return queue.add(user);
        }
    }

    public boolean removeFromSearchQueue(String username) {
        final UserAccountForm user = userAccountService.getByUsername(username);
        synchronized (queue) {
            return queue.remove(user);
        }
    }

    @Scheduled(fixedDelay = 100)
    protected void searchLoop() {
        UserAccountForm first = null;
        UserAccountForm second = null;
        String gameId = null;
        synchronized (queue) {
            if (queue.size() >= 2) {
                first = queue.remove();
                second = queue.remove();
                gameId = gameService.startGameAsync(first, second);
            }
        }
        if (gameId != null) {
            connectionService.sendGameFound(first, second, gameId);
        }
    }
}
