package io.github.hobbstech.commons.usermanager.user.service.events;

import io.github.hobbstech.commons.utilities.util.SystemProperties;
import io.github.hobbstech.commons.notifications.service.EmailMessageNotifierTemplate;
import io.github.hobbstech.commons.notifications.service.EmailSender;
import io.github.hobbstech.commons.notifications.service.EmailUserImpl;
import lombok.val;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class NewUserEventEmailSendingListener extends EmailMessageNotifierTemplate
        implements ApplicationListener<NewUserEvent> {

    private final SystemProperties systemProperties;

    public NewUserEventEmailSendingListener(EmailSender emailSender, SystemProperties systemProperties) {
        super(emailSender);
        this.systemProperties = systemProperties;
    }

    @Override
    public void onApplicationEvent(NewUserEvent newUserEvent) {

        val user = newUserEvent.getUser();

        subject = "User account for " + systemProperties.systemName + " created successfully";

        recipients.add(new EmailUserImpl(user.getUsername(), user.getEmail()));

        emailMessageFormatter.addParagraph("Your account was created for " + systemProperties.systemName + ". The " +
                "account credentials that you use to sign in are as follows:");

        val table = new HashMap<String, String>();
        table.put("username", user.getUsername());
        table.put("password", newUserEvent.getRawPassword());

        emailMessageFormatter.addTabularHierarchy("Account credentials", table);

        emailMessageFormatter.addParagraph("You can change the password to one that you can easily remember " +
                "when you login");

        sendEmail();

    }

}