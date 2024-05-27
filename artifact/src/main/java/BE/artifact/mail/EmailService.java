package BE.artifact.mail;

public interface EmailService {
    String sendSimpleMessage(String to,
                             String subject,
                             String text);
    void sendSimpleMessageUsingTemplate(String to,
                                        String subject,
                                        String templateModel);
    void sendMessageWithAttachment(String to,
                                   String subject,
                                   String text,
                                   String pathToAttachment);
}