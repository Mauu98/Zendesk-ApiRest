package com.ticket.appticket.controller;


import com.ticket.appticket.service.TicketService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.*;


@RestController
public class ApiZendeskController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/getCommentById/{id}")
    public List<Object> getCommentById(@PathVariable Long id) { return ticketService.getCommentFromTicket(id);
    }

    @RequestMapping(value = "/postComment/{id}/{message}")
    public void putCommentInTicket(@PathVariable Long id, @PathVariable String message ) throws JSONException, IOException, InterruptedException {
            ticketService.putCommentsInTicket(id, message);
    }

}




