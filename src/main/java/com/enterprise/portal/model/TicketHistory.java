package com.enterprise.portal.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TicketHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) private Ticket ticket;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) private AppUser actor;
    @Column(nullable = false) private String fieldName;
    @Column(columnDefinition = "TEXT") private String oldValue;
    @Column(columnDefinition = "TEXT") private String newValue;
    @Column(nullable = false) private LocalDateTime createdAt;
    protected TicketHistory() {}
    public TicketHistory(Ticket ticket, AppUser actor, String fieldName, String oldValue, String newValue){this.ticket=ticket;this.actor=actor;this.fieldName=fieldName;this.oldValue=oldValue;this.newValue=newValue;this.createdAt=LocalDateTime.now();}
    public Long getId(){return id;} public AppUser getActor(){return actor;} public String getFieldName(){return fieldName;} public String getOldValue(){return oldValue;} public String getNewValue(){return newValue;} public LocalDateTime getCreatedAt(){return createdAt;}
}
