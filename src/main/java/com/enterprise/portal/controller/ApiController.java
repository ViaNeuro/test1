package com.enterprise.portal.controller;

import com.enterprise.portal.dto.Dto.*;
import com.enterprise.portal.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final TicketService service;
    public ApiController(TicketService service){this.service=service;}
    @GetMapping("/me") public UserView me(Authentication auth){ return service.userView(service.user(auth.getName())); }
    @PostMapping("/tickets") public ResponseEntity<TicketView> create(@Valid @RequestBody CreateTicket req, Authentication auth){ return ResponseEntity.ok(service.create(req, auth.getName())); }
    @GetMapping("/tickets") public List<TicketView> mine(Authentication auth){ return service.mine(auth.getName()); }
    @GetMapping("/tickets/{id}") public TicketView get(@PathVariable Long id, Authentication auth){ return service.get(id, auth.getName(), false); }
}
