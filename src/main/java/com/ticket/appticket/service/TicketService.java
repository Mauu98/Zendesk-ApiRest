package com.ticket.appticket.service;


import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.*;

@Service
public class TicketService {

    @Value("${user}")
    String username;
    @Value("${password}")
    String password;
    @Value("${domain}")
    String domain;

    public List<Object> getCommentFromTicket(Long id) { //Método para obtener los comentarios de un ticket en específico.
        try {
            String url = "https://" + domain + ".zendesk.com/api/v2/tickets/" + id + "/comments.json";
            String authStr = username + ":" + password;
            String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Basic " + base64Creds);
            HttpEntity request = new HttpEntity(headers);
            ResponseEntity<String> response = new RestTemplate().exchange(url, HttpMethod.GET, request, String.class);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST);
        }
        return Collections.singletonList(new ResponseEntity<Object>("El ticket con ID " + id + " no existe.", HttpStatus.BAD_REQUEST));
    }

    public void putCommentsInTicket(Long id, String message) throws JSONException, IOException, InterruptedException { //Método para actualizar el comentario del ticket.

        String data = "{\"ticket\":{\"comment\":{\"body\":\"" + message + "\"}}}";
        JSONObject json = new JSONObject(data);
        String plainCredentials = username + ":" + password;
        String base64Credentials = new String(Base64.getEncoder().encode(plainCredentials.getBytes()));
        String authorizationHeader = "Basic " + base64Credentials;
        HttpClient client = HttpClient.newHttpClient();

        try {
                java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                        .uri(URI.create("https://" + domain + ".zendesk.com/api/v2/tickets/" + id + ".json"))
                        .PUT(java.net.http.HttpRequest.BodyPublishers.ofString(data))
                        .header("Authorization", authorizationHeader)
                        .header("Content-Type", "application/json")
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                new ResponseEntity<>("Ticket actualizado con éxito", HttpStatus.CREATED);
        } catch (Exception e) {
            e.getStackTrace();
            new ResponseEntity<Object>("TESTING",HttpStatus.BAD_REQUEST);
        }
        new ResponseEntity("El ID "+id+ " de ticket no existe!",HttpStatus.BAD_REQUEST);
    }


}

