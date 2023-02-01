package com.codecool.dadsinventory.controller;

import com.codecool.dadsinventory.model.Item;
import com.codecool.dadsinventory.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/management")
public class ItemsManagementController {


    ItemService itemService;

    @Autowired
    public ItemsManagementController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/item")
    public String addItem(Item item) {
        //son allowed
        System.out.println("add Item : " + item);
        return "management/addItem";
    }


    @GetMapping("/item")
    public String item(Item item) {
        //son allowed
        return "management/addItem";
    }

    @GetMapping("/items")
    public String getAllItems() {
        //dad and sol allowed
        return "management/getAllItems";
    }

    @GetMapping("/item/{id}")
    public String getItemById(@PathVariable("id") Long id) {
        //Dad and son alowed
        return "management/getItem";
    }

}
