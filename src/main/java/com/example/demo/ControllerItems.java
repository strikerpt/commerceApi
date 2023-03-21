package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ControllerItems {


    @Autowired
    private ItemRepository itemRepository;
    private ItemService itemService;


    @GetMapping(value="/items")
    public ResponseEntity<List<Item>> getItems() {
        List<ItemEntity> itemEntities = itemRepository.findAll();
        ArrayList<Item> items = new ArrayList<>();
        for (ItemEntity itemEntity : itemEntities) {
            Client client = null;
            ClientEntity clientEntity = itemEntity.getClient();
            if (clientEntity != null) {
                client = new Client(
                        clientEntity.getNum(), clientEntity.getPrenom(), clientEntity.getSolde());
            }
            Item item = new Item(itemEntity.getNum(), itemEntity.getDescription(),
                    itemEntity.getPrix(), client);
            items.add(item);
        }
        return new ResponseEntity<List<Item>>(items, HttpStatus.OK );
    }

    //achat d'un item disponible
    @PostMapping(value="/items/{num}")
    public ResponseEntity<Item> achatItem(@PathVariable int num, @RequestBody Client client) {
        itemService = new ItemService();
        try {
            Item item = itemService.achatItem(num, client);
            return new ResponseEntity<Item>(item, HttpStatus.OK );
        } catch (AddNotPossibleException e) {
            return new ResponseEntity<Item>(HttpStatus.CONFLICT);
        }

    }


}
