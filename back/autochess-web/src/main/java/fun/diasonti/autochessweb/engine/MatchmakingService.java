package fun.diasonti.autochessweb.engine;

import fun.diasonti.autochessweb.data.form.UserAccountForm;
import fun.diasonti.autochessweb.data.pojo.MatchmakingSearchEntry;
import fun.diasonti.autochessweb.data.pojo.SearchStatus;
import fun.diasonti.autochessweb.engine.game.GameService;
import fun.diasonti.autochessweb.service.UserAccountService;
import fun.diasonti.autochessweb.utils.HashLinkedQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MatchmakingService {

    private final HashLinkedQueue<MatchmakingSearchEntry> queue = new HashLinkedQueue<>();

    private final GameService gameService;
    private final UserAccountService userAccountService;

    @Autowired
    public MatchmakingService(GameService gameService, UserAccountService userAccountService) {
        this.gameService = gameService;
        this.userAccountService = userAccountService;
    }

    public SearchStatus getStatus(String username) {
        final UserAccountForm user = userAccountService.getByUsername(username);
        MatchmakingSearchEntry searchEntry = null;
        synchronized (queue) {
            searchEntry = queue.peek(new MatchmakingSearchEntry(user));
        }
        final SearchStatus searchStatus = new SearchStatus();
        if (searchEntry != null) {
            searchStatus.setInProgress(true);
            searchStatus.setStartedAt(searchEntry.getCreatedAt());
        }
        return searchStatus;
    }

    public boolean addToSearchQueue(String username) {
        final UserAccountForm user = userAccountService.getByUsername(username);
        synchronized (queue) {
            return queue.add(new MatchmakingSearchEntry(user));
        }
    }

    public boolean removeFromSearchQueue(String username) {
        final UserAccountForm user = userAccountService.getByUsername(username);
        synchronized (queue) {
            return queue.remove(new MatchmakingSearchEntry(user));
        }
    }

    @Scheduled(fixedDelay = 100)
    protected void searchLoop() {
        synchronized (queue) {
            if (queue.size() >= 2) {
                UserAccountForm first = queue.remove().getPlayer();
                UserAccountForm second = queue.remove().getPlayer();
                gameService.startGameAsync(first, second);
            }
        }
    }
}
