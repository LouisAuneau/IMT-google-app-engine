package com.zenika.zencontact.email;

import java.util.Properties;
import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import com.zenika.zencontact.domain.Email;
import com.zenika.zencontact.resource.auth.AuthenticationService;

public class EmailService {

    private static final Logger LOG = Logger.getLogger(EmailService.class.getName());
    private static EmailService INSTANCE = new EmailService();

    public static EmailService getInstance() {
        return INSTANCE;
    }

    public void sendEmail(Email email) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props);

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(
                AuthenticationService.getInstance().getUser().getEmail(), 
                AuthenticationService.getInstance().getUser().getNickname())
            );
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email.to, email.toName));
            msg.setReplyTo(new Address[] {new InternetAddress("contact@imt-louisauneau.appspotmail.com", "Contact us") });
            msg.setSubject(email.subject);
            msg.setText(email.body);
            Transport.send(msg);
            LOG.warning("Mail sent.");
        } catch (Exception e) { }
    }

    public void logEmail(HttpServletRequest request) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props);
        try {
            MimeMessage message = new MimeMessage(session, request.getInputStream());
            Multipart multipart = (Multipart) message.getContent();
            BodyPart part = multipart.getBodyPart(0);
            LOG.info("Mail received.");
            LOG.info("Subject:" + message.getSubject());
            LOG.info("Body:" + (String) part.getContent());
        } catch (Exception e) { }
    }
}