package io.javabrains.inbox.emaillist;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface EmailListItemRepository extends CassandraRepository<EmailList,EmailListItemKey> {

    //List<Folder> findAllById(EmailListItemKey id) ;
    List<EmailList> findAllByKey_IdAndKey_Label(String id, String label) ;
}
