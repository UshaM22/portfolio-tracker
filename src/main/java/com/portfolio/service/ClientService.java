package com.portfolio.service;
import com.portfolio.exception.ClientNotFoundException;
import com.portfolio.model.Client;
import com.portfolio.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Client save(Client client){
        return clientRepository.save(client);
    }

    public Client findById(Long id){
        return clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException("Client not found with id" +id));

    }

    public List<Client> findAll(){
        return clientRepository.findAll();
    }

    public Client update(Long id, Client client){
        Client existing = findById(id);
        existing.setName(client.getName());
        existing.setEmail(client.getEmail());
        existing.setPhone(client.getPhone());
        return clientRepository.save(existing);
    }

    public void delete(Long id){
        Client client = clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException("Client not found with id" +id));
        clientRepository.deleteById(id);
    }


}
