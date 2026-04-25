package com.portfolio.controller;

import com.portfolio.model.Client;
import com.portfolio.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    public ResponseEntity<Client> save(@RequestBody Client client){
        return ResponseEntity.ok(clientService.save(client));
    }
    @GetMapping
    public ResponseEntity<List<Client>> findAll(){
        return ResponseEntity.ok(clientService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> findById(@PathVariable Long id){
        return ResponseEntity.ok(clientService.findById(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Client> update(@PathVariable Long id, @RequestBody Client client){
        return ResponseEntity.ok(clientService.update(id,client));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id){
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

