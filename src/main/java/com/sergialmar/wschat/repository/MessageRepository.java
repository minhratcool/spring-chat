package com.sergialmar.wschat.repository;

import com.sergialmar.wschat.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by mac on 8/8/17.
 */
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("select m from Message m where m.offerId = :offerId order by m.createdOn DESC ")
    Page<Message> getLatestMessagesByOffer(@Param(value = "offerId") Long offerId, Pageable pageable);
}
