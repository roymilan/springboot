package demo.spring.controller;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import demo.spring.model.User;

@RestController
@RequestMapping("/api/consumer/user")
public class ConsumerApiController {

    private static final String PRODUCER_BASE_URL = "http://localhost:8080/api/user";

    private final RestTemplate restTemplate;

    public ConsumerApiController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsersFromProducer() {
        ResponseEntity<List<User>> response = restTemplate.exchange(
                PRODUCER_BASE_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<User>>() {}
        );
        return new ResponseEntity<>(response.getBody(), response.getStatusCode());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserFromProducer(@PathVariable("id") long id) {
        ResponseEntity<User> response = restTemplate.getForEntity(
                PRODUCER_BASE_URL + "/" + id,
                User.class
        );
        return new ResponseEntity<>(response.getBody(), response.getStatusCode());
    }

    @PostMapping
    public ResponseEntity<User> createUserInProducer(@RequestBody User user) {
        ResponseEntity<User> response = restTemplate.postForEntity(
                PRODUCER_BASE_URL,
                user,
                User.class
        );
        return new ResponseEntity<>(response.getBody(), response.getStatusCode());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUserInProducer(@PathVariable("id") long id, @RequestBody User user) {
        restTemplate.put(
                PRODUCER_BASE_URL + "/" + id,
                user
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserInProducer(@PathVariable("id") long id) {
        restTemplate.delete(PRODUCER_BASE_URL + "/" + id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
