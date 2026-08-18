package com.enterprise.portal.dto;

import com.enterprise.portal.model.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class Dto {
    public record UserView(Long id, String username, String fullName, Role role) {}
    public record CreateTicket(@NotBlank String title, @NotBlank String category, @NotBlank String description, @NotNull Priority priority) {}
    public record UpdateTicket(TicketStatus status, Long assigneeId) {}
    public record AddComment(@NotBlank String text) {}
    public record CommentView(Long id, UserView author, String text, LocalDateTime createdAt) {}
    public record HistoryView(Long id, UserView actor, String fieldName, String oldValue, String newValue, LocalDateTime createdAt) {}
    public record TicketView(Long id, String title, String category, String description, Priority priority, TicketStatus status, UserView requester, UserView assignee, LocalDateTime createdAt, LocalDateTime updatedAt, List<CommentView> comments, List<HistoryView> history) {}
}
