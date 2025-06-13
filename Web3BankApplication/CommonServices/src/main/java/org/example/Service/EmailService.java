package org.example.Service;

import org.example.dto.EmailDetails;

public interface EmailService {
    static String sendSimpleEmail(EmailDetails emailDetails);
    String sendEmailWithAttachment(EmailDetails emailDetails);

}
