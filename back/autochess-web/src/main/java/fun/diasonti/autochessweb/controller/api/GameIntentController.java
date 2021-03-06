package fun.diasonti.autochessweb.controller.api;

import fun.diasonti.autochessweb.config.security.data.AppUser;
import fun.diasonti.autochessweb.engine.game.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/intent/game")
public class GameIntentController {

    private final GameService gameService;

    @Autowired
    public GameIntentController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/move")
    public void movePiece(AppUser user, @RequestParam int fromCell, @RequestParam int toCell) {
        gameService.attemptMovePiece(user.getUsername(), fromCell, toCell);
    }

}
