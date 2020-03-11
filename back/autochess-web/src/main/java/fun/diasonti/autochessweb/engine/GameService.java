package fun.diasonti.autochessweb.engine;

import fun.diasonti.autochessweb.data.form.UserAccountForm;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GameService {

    public String startGameAsync(UserAccountForm player1, UserAccountForm player2) {
        final String gameId = UUID.randomUUID().toString();
        // Game should be asynchronously started here
        return gameId;
    }

}
