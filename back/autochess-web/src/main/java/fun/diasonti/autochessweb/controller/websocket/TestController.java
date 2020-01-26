package fun.diasonti.autochessweb.controller.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private SimpMessagingTemplate websocket;

    @GetMapping("")
    public String test(@RequestParam(defaultValue = "rnbq1bnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQ1BNR") String fen) {
        websocket.convertAndSend("/topic/test", fen);
        return fen;
    }

}
