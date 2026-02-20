package com.idgovern.util;

import com.idgovern.beans.Mail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 * Utility class for sending emails using Apache Commons Email.
 *
 * <p>
 * Provides simplified methods to send HTML emails to one or more recipients.
 * Handles SMTP connection setup, authentication, and logging of send status.
 * </p>
 *
 * <p>
 * Business Rules:
 * <ul>
 *     <li>Email must have at least one recipient.</li>
 *     <li>SMTP host, port, and authentication credentials must be configured.</li>
 *     <li>All send attempts are logged with success or failure messages.</li>
 *     <li>Returns a boolean indicating success (true) or failure (false).</li>
 * </ul>
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author     | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-03-17 | Lilian S.  | Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian
 * @version 1.0
 * @since 1.0
 */
@Slf4j
public class MailUtil {

    /**
     * Sends an HTML email to one or more recipients.
     *
     * <p>
     * Uses {@link HtmlEmail} from Apache Commons Email for sending.
     * Logs the result of the send operation.
     * </p>
     *
     * @param mail the {@link Mail} object containing recipients, subject, and message body
     * @return true if the email was sent successfully; false otherwise
     */
    public static boolean send(Mail mail) {

        // TODO: Configure actual SMTP credentials and host
        String from = "";
        int port = 25;
        String host = "";
        String pass = "";
        String nickname = "";

        HtmlEmail email = new HtmlEmail();
        try {
            email.setHostName(host);
            email.setCharset("UTF-8");

            // Add all recipients
            for (String str : mail.getReceivers()) {
                email.addTo(str);
            }

            email.setFrom(from, nickname);
            email.setSmtpPort(port);
            email.setAuthentication(from, pass);
            email.setSubject(mail.getSubject());
            email.setMsg(mail.getMessage());
            email.send();

            log.info("{} email to {}", from, StringUtils.join(mail.getReceivers(), ","));
            return true;

        } catch (EmailException e) {
            log.error("{} failed to send email to {}", from, StringUtils.join(mail.getReceivers(), ","), e);
            return false;
        }
    }

}

