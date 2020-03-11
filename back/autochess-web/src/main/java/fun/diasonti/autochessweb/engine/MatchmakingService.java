package fun.diasonti.autochessweb.engine;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import fun.diasonti.autochessweb.data.form.UserAccountForm;
import fun.diasonti.autochessweb.utils.HashLinkedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class MatchmakingService {

    private static final Logger log = LoggerFactory.getLogger(MatchmakingService.class);

    private final ExecutorService sync = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("matchmaking-thread").build());
    private final HashLinkedQueue<UserAccountForm> queue = new HashLinkedQueue<>();

    private final GameService gameService;
    private final ConnectionService connectionService;

    @Autowired
    public MatchmakingService(GameService gameService, ConnectionService connectionService) {
        this.gameService = gameService;
        this.connectionService = connectionService;
    }

    public boolean addToSearchQueue(UserAccountForm user) {
        boolean success = false;
        try {
            success = sync.submit(() -> queue.add(user)).get();
        } catch (Exception e) {
            log.error("addToSearchQueue error", e);
        }
        return success;
    }

    public boolean removeFromSearchQueue(UserAccountForm user) {
        boolean success = false;
        try {
            success = sync.submit(() -> queue.remove(user)).get();
        } catch (Exception e) {
            log.error("removeFromSearchQueue error", e);
        }
        return success;
    }

    public void removeFromSearchQueue(Collection<UserAccountForm> users) {
        sync.execute(() -> queue.removeIf(users::contains));
    }

    @Scheduled(fixedDelay = 100)
    protected void searchLoop() {
        sync.execute(() -> {
            if (queue.size() >= 2) {
                final UserAccountForm first = queue.remove();
                final UserAccountForm second = queue.remove();
                final String gameId = gameService.startGameAsync(first, second);
                connectionService.sendGameFound(first, second, gameId);
            }
        });
    }
}
