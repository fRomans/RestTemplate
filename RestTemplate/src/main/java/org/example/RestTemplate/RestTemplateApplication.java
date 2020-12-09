package org.example.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.RestTemplate.domain.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;


@SpringBootApplication
public class RestTemplateApplication {

    public static final String URL = "http://91.241.64.178:7081/api/users";


    public static void main(String[] args) throws IOException {
        SpringApplication.run(RestTemplateApplication.class, args);
        StringBuilder magicCode = new StringBuilder();
        String sessionId;
///_______________________________получение
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<User>> response = restTemplate.exchange(
                URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {
                });
        List<User> users = response.getBody();
        System.out.println(users);
//___________________________добавление
        sessionId = response.getHeaders().getFirst("Set-Cookie");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Set-Cookie", sessionId);
        headers.add("Cookie", sessionId);

        User addUser = new User(3L, "James", "Brown", (byte) 25);
        HttpEntity<User> requestEntityAdd = new HttpEntity<>(addUser, headers);
        HttpEntity<String> responseAdd = restTemplate.exchange(URL, HttpMethod.POST, requestEntityAdd, String.class);
        magicCode.append(responseAdd.getBody());

        System.out.println(magicCode);
//__________________________________изменение

       // User editUser = new User(3L, "Thomas", "Shelby", (byte) 25);
        User editUser = addUser;
        editUser.setName("Thomas");
        editUser.setLastName("Shelby");
        HttpEntity<User> requestEntityEdit = new HttpEntity<>(editUser, headers);
        HttpEntity<String> responseEdit = restTemplate.exchange(URL, HttpMethod.PUT, requestEntityEdit, String.class);
        magicCode.append(responseEdit.getBody());
        System.out.println(magicCode);


        //__________________________________удаление

        User deleteUser = editUser;
        HttpEntity<User> requestEntityDelete = new HttpEntity<>(deleteUser, headers);
        HttpEntity<String> responseDelete = restTemplate.exchange(URL+"/"+deleteUser.getId(), HttpMethod.DELETE, requestEntityDelete, String.class);
        magicCode.append(responseDelete.getBody());
        System.out.println(magicCode);


    }


}

