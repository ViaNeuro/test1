package com.enterprise.portal.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TicketComment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) private Ticket ticket;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) private AppUser author;
    @Column(nullable = false, columnDefinition = "TEXT") private String text;
    @Column(nullable = false) private LocalDateTime createdAt;
    protected TicketComment() {}
    public TicketComment(Ticket ticket, AppUser author, String text){this.ticket=ticket;this.author=author;this.text=text;this.createdAt=LocalDateTime.now();}
    public Long getId(){return id;} public AppUser getAuthor(){return author;} public String getText(){return text;} public LocalDateTime getCreatedAt(){return createdAt;}
}
