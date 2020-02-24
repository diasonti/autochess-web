package fun.diasonti.autochessweb.engine;

import fun.diasonti.autochessweb.data.form.UserAccountForm;
import fun.diasonti.autochessweb.data.pojo.ActiveGame;
import fun.diasonti.chessengine.data.ChessBoard;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ActiveGameEngine {

    private Map<String, ActiveGame> activeGames = new HashMap<>();

    public String createGame(UserAccountForm player1, UserAccountForm player2) {
        final ActiveGame game = new ActiveGame();
        game.setBoard(new ChessBoard());
        if (Math.random() > 0.5) {
            game.setWhitePlayer(player1);
            game.setBlackPlayer(player2);
        } else {
            game.setBlackPlayer(player1);
            game.setWhitePlayer(player2);
        }
        game.setWhitePieces(RandomStringUtils.random(5, "RNBQP"));
        game.setBlackPieces(RandomStringUtils.random(5, "rnbqp"));
        activeGames.put(game.getId(), game);
        return game.getId();
    }

}
