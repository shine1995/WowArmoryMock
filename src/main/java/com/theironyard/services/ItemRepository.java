package com.theironyard.services;

import com.theironyard.entities.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Integer>{
//    Item findById (int id);

//    Item findByName (String name);

    Item findByInventoryType (String inventoryType);

//    Item findByItemLevel (String itemLevel);

//    Item findbyArmor (String armor);

}
