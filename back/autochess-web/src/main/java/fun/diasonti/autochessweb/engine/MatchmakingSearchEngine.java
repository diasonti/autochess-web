package fun.diasonti.autochessweb.engine;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import fun.diasonti.autochessweb.data.form.UserAccountForm;
import fun.diasonti.autochessweb.data.pojo.MatchmakingSearchEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Component
public class MatchmakingSearchEngine {

    private static final Logger log = LoggerFactory.getLogger(MatchmakingSearchEngine.class);

    private final LoadingCache<UserAccountForm, String> searchTokenCache;
    private final Map<Long, MatchmakingSearchEntry> queue;

    private final ActiveGameEngine activeGameEngine;

    @Autowired
    public MatchmakingSearchEngine(ActiveGameEngine activeGameEngine) {
        this.activeGameEngine = activeGameEngine;
        this.searchTokenCache = CacheBuilder.newBuilder()
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .build(new CacheLoader<UserAccountForm, String>() {
                    @Override
                    public String load(UserAccountForm key) throws Exception {
                        return UUID.randomUUID().toString();
                    }
                });
        this.queue = new LinkedHashMap<>();
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
        synchronized (queue) {
            queue.put(player.getId(), entry);
        }
    }

    public void remove(UserAccountForm player) {
        if (player == null)
            return;
        synchronized (queue) {
            queue.remove(player.getId());
        }
    }

    @Scheduled(fixedDelay = 100)
    protected void sweep() {
        final List<MatchmakingSearchEntry> players = new ArrayList<>(2);
        synchronized (queue) {
            if (queue.size() >= 2) {
                final Iterator<MatchmakingSearchEntry> queueIterator = queue.values().iterator();
                players.add(queueIterator.next());
                queueIterator.remove();
                players.add(queueIterator.next());
                queueIterator.remove();
            }
        }
        if (players.size() == 2) {
            final String gameId = activeGameEngine.startGame(players.get(0).getPlayer(), players.get(1).getPlayer());
            players.parallelStream().forEach(entry -> entry.getOnGameFoundCallback().accept(gameId));
        }
    }
}
