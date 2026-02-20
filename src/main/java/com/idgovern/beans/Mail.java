package com.idgovern.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.Set;


/**
 * Represents an email message to be sent.
 *
 * <p>
 * This class encapsulates all necessary information for sending an email,
 * including subject, content, and recipient addresses.
 * </p>
 *
 * <p>
 * Business Rules:
 * <ul>
 *     <li>Subject should be concise and meaningful.</li>
 *     <li>Message can contain plain text or HTML content.</li>
 *     <li>Receivers must contain at least one valid email address.</li>
 * </ul>
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author     | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-17 | Lilian S.  | Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mail {

    /** Subject of the email. */
    private String subject;

    /** Body content of the email. Can be plain text or HTML. */
    private String message;

    /** Set of recipient email addresses. Must not be empty. */
    private Set<String> receivers;
}
