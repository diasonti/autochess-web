package fun.diasonti.autochessweb.engine;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import fun.diasonti.autochessweb.data.form.UserAccountForm;
import fun.diasonti.autochessweb.service.UserAccountService;
import fun.diasonti.autochessweb.utils.HashLinkedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class MatchmakingService {

    private static final Logger log = LoggerFactory.getLogger(MatchmakingService.class);

    private final LoadingCache<String, String> tokenCache = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String key) throws Exception {
                    return UUID.randomUUID().toString();
                }
            });

    private final ExecutorService sync = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("matchmaking-thread").build());
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

    public String getMatchmakingToken(String username) {
        String token;
        try {
            token = tokenCache.get(username);
        } catch (ExecutionException e) {
            log.error("getMatchmakingToken error", e);
            token = UUID.randomUUID().toString();
            tokenCache.put(username, token);
        }
        return token;
    }

    public boolean addToSearchQueue(String username) {
        boolean success = false;
        try {
            final UserAccountForm user = userAccountService.getByUsername(username);
            success = sync.submit(() -> queue.add(user)).get();
        } catch (Exception e) {
            log.error("addToSearchQueue error", e);
        }
        return success;
    }

    public boolean removeFromSearchQueue(String username) {
        boolean success = false;
        try {
            final UserAccountForm user = userAccountService.getByUsername(username);
            success = sync.submit(() -> queue.remove(user)).get();
        } catch (Exception e) {
            log.error("removeFromSearchQueue error", e);
        }
        return success;
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
