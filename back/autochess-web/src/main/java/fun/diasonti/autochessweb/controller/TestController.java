package fun.diasonti.autochessweb.controller;

import com.google.common.collect.ImmutableMap;
import fun.diasonti.autochessweb.data.entity.UserAccount;
import fun.diasonti.autochessweb.data.form.UserAccountForm;
import fun.diasonti.autochessweb.data.mappers.UserAccountMapper;
import fun.diasonti.autochessweb.repository.UserAccountRepository;
import fun.diasonti.chessengine.data.ChessBoard;
import fun.diasonti.chessengine.data.Move;
import fun.diasonti.chessengine.engine.interfaces.MoveEngine;
import fun.diasonti.chessengine.engine.interfaces.SearchEngine;
import fun.diasonti.chessengine.util.BoardUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Random;

@RestController
@RequestMapping("/test")
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    private final Random random = new Random();

    @Autowired
    private MoveEngine moveEngine;
    @Autowired
    private SearchEngine searchEngine;
    @Autowired
    private UserAccountMapper userAccountMapper;
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/user/create")
    public UserAccountForm createUser(UserAccountForm form) {
        UserAccount entity = userAccountMapper.formToEntity(form);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity = userAccountRepository.save(entity);
        return userAccountMapper.entityToForm(entity);
    }

    @GetMapping("/board")
    public String board(@RequestParam(defaultValue = "rnbq1bnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQ1BNR") String fen) {
        return fen;
    }

    @GetMapping(value = "/moves", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter moves(@RequestParam(defaultValue = "rnbq1bnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQ1BNR") String startingFen) {
        final SseEmitter emitter = new SseEmitter();
        new Thread(() -> {
            try {
                ChessBoard board = BoardUtils.fenToBitboard(startingFen);
                for (int i = 0; i < 5; i++) {
                    final Move move = searchEngine.getBestMove(board, board.currentPlayer, random.nextInt(4) + 1);
                    board.currentPlayer = board.currentPlayer.getOpposite();
                    board.moveCount++;
                    emitter.send(SseEmitter.event().name("move").data(ImmutableMap.of("from", Long.numberOfTrailingZeros(move.from), "to", Long.numberOfTrailingZeros(move.to))));
                    board = moveEngine.makeMove(board, move);
                    Thread.sleep(1000L);
                }
                emitter.send(SseEmitter.event().name("close").data("null"));
            } catch (IOException e) {
                emitter.completeWithError(e);
            } catch (InterruptedException e) {
                emitter.completeWithError(e);
                Thread.currentThread().interrupt();
            }
        }).start();
        return emitter;
    }

    @GetMapping(value = "/infinity", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter infinity() {
        final SseEmitter emitter = new SseEmitter();
        new Thread(() -> {
            try {
                emitter.onCompletion(() -> log.info("infinity OnCompletion"));
                emitter.onError(throwable -> log.info("infinity error", throwable));
                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                    log.info("infinity ping");
                    emitter.send(SseEmitter.event().name("ping").data("null"));
                    Thread.sleep(1_000L);
                }
                log.info("infinity send");
                emitter.complete();
            } catch (IOException e) {
                emitter.completeWithError(e);
            } catch (InterruptedException e) {
                emitter.completeWithError(e);
                Thread.currentThread().interrupt();
            }
        }).start();
        return emitter;
    }

}
