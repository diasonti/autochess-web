package fun.diasonti.autochessweb.engine;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import fun.diasonti.autochessweb.data.form.UserAccountForm;
import fun.diasonti.autochessweb.data.pojo.MatchmakingSearchEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Component
public class MatchmakingSearchEngine {

    private static final Logger log = LoggerFactory.getLogger(MatchmakingSearchEngine.class);

    private final LoadingCache<UserAccountForm, String> searchTokenCache;

    private final Object lock = new Object();
    private MatchmakingSearchEntry waiting;

    private Executor onGameFoundExecutor = Executors.newCachedThreadPool();

    public MatchmakingSearchEngine() {
        this.searchTokenCache = CacheBuilder.newBuilder()
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .build(new CacheLoader<UserAccountForm, String>() {
                    @Override
                    public String load(UserAccountForm key) throws Exception {
                        return UUID.randomUUID().toString();
                    }
                });
    }

    public String getSearchToken(UserAccountForm player) {
        try {
            return searchTokenCache.get(player);
        } catch (ExecutionException e) {
            log.error("Error loading search token", e);
            return null;
        }
    }

    public void add(UserAccountForm player, Consumer<String> onGameFound) {
        final MatchmakingSearchEntry entry = new MatchmakingSearchEntry(player, onGameFound);
        MatchmakingSearchEntry opponent = null;
        synchronized (lock) {
            if (waiting == null) {
                waiting = entry;
            } else {
                opponent = waiting;
                waiting = null;
            }
        }
        if (opponent != null) {
            final String gameId = UUID.randomUUID().toString();
            onGameFoundExecutor.execute(() -> entry.getOnGameFoundCallback().accept(gameId));
            entry.getOnGameFoundCallback().accept(gameId);
        }
    }

    public void remove(UserAccountForm player) {
        synchronized (lock) {
            if (waiting != null && waiting.getPlayer().equals(player)) {
                waiting = null;
            }
        }
    }
}
