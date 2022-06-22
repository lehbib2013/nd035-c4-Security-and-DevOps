package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.ItemRepository;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepo = mock(ItemRepository.class);
    @Before
    public void SetUp(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController,"itemRepository",itemRepo);
    }
    @Test
    public void test_item_find_by_id() {
        Item item = new Item();
        item.setId(1l);
        item.setName("fruit");
        item.setPrice(BigDecimal.valueOf(11));
        item.setDescription("fruits description");

        when(itemRepo.findById(1l)).thenReturn(Optional.of(item));
     //   when(itemRepo.save(item)).thenReturn(item);

        ResponseEntity<Item> response=itemController.getItemById(1l);

        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());

        Item  itm= response.getBody();
        assertNotNull(itm);
        assertEquals(1l,itm.getId().longValue());
        assertEquals("fruit",itm.getName());
        assertEquals(BigDecimal.valueOf(11),itm.getPrice());
        assertEquals("fruits description",itm.getDescription());


    }

//    } m/
    @Test
    public void test_item_find_by_name() {
        Item item1 = new Item(1l,"fruit old",new BigDecimal(45),"fruit description");
        Item item2 = new Item(1l,"carrot new",new BigDecimal(45),"fruit description");
        Item item3 = new Item(1l,"fruit hight",new BigDecimal(45),"fruit description");
        List<Item> resultSearch = new ArrayList<Item>();
        resultSearch.add(item1);
        resultSearch.add(item2);
        resultSearch.add(item3);
        when(itemRepo.findByName("fruit")).thenReturn(resultSearch);
        ResponseEntity<List<Item>> response = itemController.getItemsByName("fruit");
        List<Item>  itms= response.getBody();
        assertNotNull(itms);
        assertEquals(3,itms.stream().count());
        assertEquals("fruit old",itms.get(0).getName());
        assertEquals(new BigDecimal(45),itms.get(0).getPrice());
        assertEquals("fruit description",itms.get(0).getDescription());

    }
    @Test
    public void test_get_items() {
        Item item1 = new Item(1l,"fruit old",new BigDecimal(45),"fruit description");
        Item item2 = new Item(1l,"carrot new",new BigDecimal(45),"fruit description");
        Item item3 = new Item(1l,"fruit hight",new BigDecimal(45),"fruit description");
        List<Item> resultSearch = new ArrayList<Item>();
        resultSearch.add(item1);
        resultSearch.add(item2);
        resultSearch.add(item3);
        when(itemRepo.findAll()).thenReturn(resultSearch);
        ResponseEntity<List<Item>> response=itemController.getItems();

        List<Item>  itms= response.getBody();
        assertNotNull(itms);
        assertEquals(3,itms.stream().count());
        assertEquals("fruit old",itms.get(0).getName());
        assertEquals(new BigDecimal(45),itms.get(0).getPrice());
        assertEquals("fruit description",itms.get(0).getDescription());
 }


}
