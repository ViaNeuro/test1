package com.enterprise.portal.controller;

import com.enterprise.portal.dto.Dto.*;
import com.enterprise.portal.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/it")
public class ItController {
    private final TicketService service;
    public ItController(TicketService service){this.service=service;}
    @GetMapping("/tickets") public List<TicketView> all(){ return service.all(); }
    @GetMapping("/tickets/{id}") public TicketView get(@PathVariable Long id, Authentication auth){ return service.get(id, auth.getName(), true); }
    @PutMapping("/tickets/{id}") public TicketView update(@PathVariable Long id, @RequestBody UpdateTicket req, Authentication auth){ return service.update(id, req, auth.getName()); }
    @PostMapping("/tickets/{id}/comments") public TicketView comment(@PathVariable Long id, @Valid @RequestBody AddComment req, Authentication auth){ return service.comment(id, req, auth.getName()); }
    @GetMapping("/specialists") public List<UserView> specialists(){ return service.specialists(); }
}
