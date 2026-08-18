package com.enterprise.portal.service;

import com.enterprise.portal.dto.Dto.*;
import com.enterprise.portal.model.*;
import com.enterprise.portal.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class TicketService {
    private final TicketRepository tickets; private final AppUserRepository users;
    public TicketService(TicketRepository tickets, AppUserRepository users){this.tickets=tickets;this.users=users;}
    @Transactional public TicketView create(CreateTicket req, String username){
        AppUser requester = user(username); Ticket t = new Ticket();
        t.setTitle(req.title()); t.setCategory(req.category()); t.setDescription(req.description()); t.setPriority(req.priority()); t.setRequester(requester);
        tickets.save(t); t.getHistory().add(new TicketHistory(t, requester, "status", null, TicketStatus.NEW.name()));
        return view(t);
    }
    @Transactional(readOnly = true) public List<TicketView> mine(String username){ return tickets.findMine(username).stream().map(this::view).toList(); }
    @Transactional(readOnly = true) public List<TicketView> all(){ return tickets.findAllForPanel().stream().map(this::view).toList(); }
    @Transactional(readOnly = true) public TicketView get(Long id, String username, boolean it){
        Ticket t = tickets.findDetailed(id).orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
        if (!it && !t.getRequester().getUsername().equals(username)) throw new AccessDeniedException("Not your ticket");
        return view(t);
    }
    @Transactional public TicketView update(Long id, UpdateTicket req, String actorName){
        Ticket t = tickets.findDetailed(id).orElseThrow(() -> new EntityNotFoundException("Ticket not found")); AppUser actor = user(actorName);
        if (req.status()!=null && req.status()!=t.getStatus()) { t.getHistory().add(new TicketHistory(t, actor, "status", t.getStatus().name(), req.status().name())); t.setStatus(req.status()); }
        if (req.assigneeId()!=null) { AppUser assignee = users.findById(req.assigneeId()).orElseThrow(() -> new EntityNotFoundException("Assignee not found"));
            if (assignee.getRole()==Role.USER) throw new IllegalArgumentException("Assignee must be IT specialist or admin");
            String old = t.getAssignee()==null ? null : t.getAssignee().getUsername(); if (!Objects.equals(old, assignee.getUsername())) { t.getHistory().add(new TicketHistory(t, actor, "assignee", old, assignee.getUsername())); t.setAssignee(assignee); }
        }
        return view(t);
    }
    @Transactional public TicketView comment(Long id, AddComment req, String actorName){ Ticket t=tickets.findDetailed(id).orElseThrow(() -> new EntityNotFoundException("Ticket not found")); AppUser a=user(actorName); t.getComments().add(new TicketComment(t,a,req.text())); t.getHistory().add(new TicketHistory(t,a,"comment",null,"added")); return view(t); }
    @Transactional(readOnly = true) public List<UserView> specialists(){ return users.findByRoleIn(List.of(Role.IT_SPECIALIST, Role.ADMIN)).stream().map(this::userView).toList(); }
    public AppUser user(String username){ return users.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found")); }
    public UserView userView(AppUser u){ return new UserView(u.getId(), u.getUsername(), u.getFullName(), u.getRole()); }
    private TicketView view(Ticket t){ return new TicketView(t.getId(),t.getTitle(),t.getCategory(),t.getDescription(),t.getPriority(),t.getStatus(),userView(t.getRequester()),t.getAssignee()==null?null:userView(t.getAssignee()),t.getCreatedAt(),t.getUpdatedAt(),t.getComments().stream().map(c->new CommentView(c.getId(),userView(c.getAuthor()),c.getText(),c.getCreatedAt())).toList(),t.getHistory().stream().map(h->new HistoryView(h.getId(),userView(h.getActor()),h.getFieldName(),h.getOldValue(),h.getNewValue(),h.getCreatedAt())).toList()); }
}
