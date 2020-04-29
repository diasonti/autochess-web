package fun.diasonti.autochessweb.gateway;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGridAPI;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Profile("!dev")
public class SendGridEmailGateway implements EmailGateway {

    private final Logger log = LoggerFactory.getLogger(SendGridEmailGateway.class);

    private final SendGridAPI api;
    private final Email from;

    @Autowired
    public SendGridEmailGateway(SendGridAPI api, @Value("${email-gateway.from.address}") String fromAddress,
                                @Value("${email-gateway.from.name}") String fromName) {
        this.api = api;
        this.from = new Email(fromAddress, fromName);
    }

    @Override
    public boolean send(String recipient, String subject, String body) {
        final Email to = new Email(recipient);
        final Content content = new Content(MediaType.TEXT_HTML_VALUE, body);
        final Mail mail = new Mail(from, subject, to, content);

        final Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            final Response response = api.api(request);
            if (response.getStatusCode() != HttpStatus.SC_ACCEPTED) {
                log.error("Email sending error. To: '{}', subject: '{}', body: '{}'. Api response code: '{}', body: '{}'",
                        recipient, subject, body, response.getStatusCode(), response.getBody());
                return false;
            }
            return true;
        } catch (IOException e) {
            log.error("Email sending error. To: '{}', subject: '{}', body: '{}'", recipient, subject, body, e);
            return false;
        }
    }
}
