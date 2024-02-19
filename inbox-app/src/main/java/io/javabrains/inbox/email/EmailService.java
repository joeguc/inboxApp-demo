package io.javabrains.inbox.email;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import io.javabrains.inbox.emaillist.EmailList;
import io.javabrains.inbox.emaillist.EmailListItemKey;
import io.javabrains.inbox.emaillist.EmailListItemRepository;
import io.javabrains.inbox.folders.UnreadEmailStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private EmailListItemRepository emailListItemRepository;
    @Autowired
    private UnreadEmailStatsRepository unreadEmailStatsRepository;

    public void sendEmail(String from, List<String> to , String subject ,String body){
        Email email = new Email();
        email.setTo(to);
        email.setFrom(from);
        email.setBody(body);
        email.setSubject(subject);
        email.setId(Uuids.timeBased());
        emailRepository.save(email);

        to.forEach(toId -> {
             EmailList item = createEmailListItem(from, to, subject, email,"Inbox");
            emailListItemRepository.save(item);
            unreadEmailStatsRepository.incrementUnreadCount(toId,"Inbox");

        });
        EmailList sentItemsEntry = createEmailListItem(from, to, subject, email, "Sent Items");
        sentItemsEntry.setUnread(false);
        emailListItemRepository.save(sentItemsEntry);

    }

    private static EmailList createEmailListItem(String itemOwner, List<String> to, String subject, Email email,String folder) {
        EmailListItemKey key = new EmailListItemKey();
        key.setId(itemOwner);
        key.setLabel(folder);
        key.setTimeUUID(email.getId());
        EmailList item = new EmailList();
        item.setKey(key);
        item.setTo(to);
        item.setSubject(subject);
        item.setUnread(true);
        return item;
    }

}


















