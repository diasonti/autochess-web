package fun.diasonti.autochessweb.gateway;

public interface EmailGateway {

    boolean send(String recipient, String subject, String body);

}
