package com.example.demo;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ClientRepository clientRepository;


    @Transactional
    public Item achatItem(int num, Client client) throws AddNotPossibleException {
        Optional<ItemEntity> itemEntity = itemRepository.findById(num);
        if (itemEntity.isEmpty()) {
            throw new AddNotPossibleException("Item non disponible");
        }

        Optional<ClientEntity> clientEntity = clientRepository.findByPrenom(client.getPrenom());
        if (clientEntity.isEmpty()) {
            throw new AddNotPossibleException("Client non disponible");
        }

        if(itemEntity.get().getClient() != null) {
            throw new AddNotPossibleException("Item déjà acheté");
        }

        if (clientEntity.get().getSolde().compareTo(itemEntity.get().getPrix()) < 0) {
            throw new AddNotPossibleException("Solde insuffisant");
        }

        clientEntity.get().setSolde(clientEntity.get().getSolde().subtract(itemEntity.get().getPrix()));
        itemEntity.get().setClient(clientEntity.get());
        return new Item(itemEntity.get().getNum(), itemEntity.get().getDescription(),
                itemEntity.get().getPrix(), client);
    }
}
