package fun.diasonti.autochessweb.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Primary
@Profile("dev")
public class DevEmailGateway implements EmailGateway {

    private final Logger log = LoggerFactory.getLogger(DevEmailGateway.class);

    @Override
    public boolean send(String recipient, String subject, String body) {
        log.info("Email 'sent'. To: '{}', subject: '{}', body: '{}'", recipient, subject, body);
        return true;
    }
}
