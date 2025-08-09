package com.cineflex.api.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.rmi.server.UID;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EmailService {
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("${mail.key.public}")
    private String publicKey;
    @Value("${mail.key.private}")
    private String privateKey;

    public void sendActivationEmail(String token, String email, String host) {
        sendEmail(host + "/api/authentication/verify/" + token, email, "Email xác thực tài khoản");
    }

    public void sendEmail(String content, String email, String subject) {
        try {
            String username = publicKey;
            String password = privateKey;
            String auth = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

            System.out.println(auth);

            String jsonBody = """
{
    "Messages": [
        {
            "From": {
                "Email": "z1ml3w1s123@gmail.com",
                "Name": "Zimlewis"
            },
            "To": [
                {
                    "Email": "%s",
                    "Name": "%s"
                }
            ],
            "Subject": "%s",
            "TextPart": "%s"
        }
    ]
}

            """.formatted(email, "", subject, content);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.mailjet.com/v3.1/send"))
                    .header("Authorization", "Basic " + auth)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            System.out.println("Status: " + response.statusCode());
            System.out.println("Response body: " + response.body());
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        
    }
}
