package com.enterprise.portal.repository;

import com.enterprise.portal.model.Ticket;
import org.springframework.data.jpa.repository.*;
import java.util.*;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("select distinct t from Ticket t left join fetch t.requester left join fetch t.assignee where t.requester.username = :username order by t.createdAt desc")
    List<Ticket> findMine(String username);
    @Query("select distinct t from Ticket t left join fetch t.requester left join fetch t.assignee order by t.createdAt desc")
    List<Ticket> findAllForPanel();
    @Query("select t from Ticket t left join fetch t.requester left join fetch t.assignee left join fetch t.comments c left join fetch c.author where t.id = :id")
    Optional<Ticket> findDetailed(Long id);
}
