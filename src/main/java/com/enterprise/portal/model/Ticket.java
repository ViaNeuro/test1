package com.enterprise.portal.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ticket {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String title;
    @Column(nullable = false) private String category;
    @Column(nullable = false, columnDefinition = "TEXT") private String description;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Priority priority;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private TicketStatus status = TicketStatus.NEW;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) private AppUser requester;
    @ManyToOne(fetch = FetchType.LAZY) private AppUser assignee;
    @Column(nullable = false) private LocalDateTime createdAt;
    @Column(nullable = false) private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true) private List<TicketComment> comments = new ArrayList<>();
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true) private List<TicketHistory> history = new ArrayList<>();
    @PrePersist void onCreate(){ createdAt = LocalDateTime.now(); updatedAt = createdAt; }
    @PreUpdate void onUpdate(){ updatedAt = LocalDateTime.now(); }
    public Long getId(){return id;} public String getTitle(){return title;} public void setTitle(String title){this.title=title;}
    public String getCategory(){return category;} public void setCategory(String category){this.category=category;}
    public String getDescription(){return description;} public void setDescription(String description){this.description=description;}
    public Priority getPriority(){return priority;} public void setPriority(Priority priority){this.priority=priority;}
    public TicketStatus getStatus(){return status;} public void setStatus(TicketStatus status){this.status=status;}
    public AppUser getRequester(){return requester;} public void setRequester(AppUser requester){this.requester=requester;}
    public AppUser getAssignee(){return assignee;} public void setAssignee(AppUser assignee){this.assignee=assignee;}
    public LocalDateTime getCreatedAt(){return createdAt;} public LocalDateTime getUpdatedAt(){return updatedAt;}
    public List<TicketComment> getComments(){return comments;} public List<TicketHistory> getHistory(){return history;}
}
