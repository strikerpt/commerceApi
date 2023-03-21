package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ControllerClient {


    @Autowired
    private ClientRepository clientRepository;


    @GetMapping(value="/clients/{num}")
    public ResponseEntity<Client> getClient(@PathVariable int num) {
        Optional opt = clientRepository.findById(num);
        if(opt.isEmpty()) {
            return new ResponseEntity<Client>(HttpStatus.NOT_FOUND);
        }
        ClientEntity clientEntity = (ClientEntity) opt.get();
        Client client = new Client(clientEntity.getNum(), clientEntity.getPrenom(), clientEntity.getSolde());
        return new ResponseEntity<Client>(client, HttpStatus.OK );

    }
    @GetMapping(value="/client")
    public ResponseEntity<Client> getClient(@RequestParam String prenom) {
        Optional opt = clientRepository.findByPrenom(prenom);
        if(opt.isEmpty()) {
            return new ResponseEntity<Client>(HttpStatus.NOT_FOUND);
        }
        ClientEntity clientEntity = (ClientEntity) opt.get();
        Client client = new Client(clientEntity.getNum(), clientEntity.getPrenom(), clientEntity.getSolde());
        return new ResponseEntity<Client>(client, HttpStatus.OK );

    }


    //Dans le body, il faut passer un object client pas un string
    @PostMapping(value="/clients")
    public ResponseEntity<Client> addClient(@RequestBody Client client) {
        Optional opt = clientRepository.findByPrenom(client.getPrenom());
        if(opt.isPresent()) {
            return new ResponseEntity<Client>(HttpStatus.CONFLICT);
        }

        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setPrenom(client.getPrenom());
        clientEntity.setSolde(new BigDecimal(50));
        ClientEntity clientAdded = clientRepository.save(clientEntity);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(clientAdded.getNum())
                .toUri();
        return ResponseEntity.created(uri).build();
    }








}
