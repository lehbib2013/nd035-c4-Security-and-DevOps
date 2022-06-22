package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);
    @Before
    public void SetUp(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController,"userRepository",userRepo);
        TestUtils.injectObjects(cartController,"cartRepository",cartRepo);
        TestUtils.injectObjects(cartController,"itemRepository",itemRepo);
  }
  @Test
public void failed_adding_to_cart(){
    ModifyCartRequest request =new ModifyCartRequest() ;
    request.setItemId(1l);
    request.setQuantity(5);
    request.setUsername("user1");


    ResponseEntity<Cart> response=cartController.addTocart(request);

    assertNotNull(response);
    assertEquals(404,response.getStatusCodeValue());
    Cart crt= response.getBody();
    assertNull(crt);


}
    @Test
    public void success_adding_to_cart(){
        ModifyCartRequest request =new ModifyCartRequest() ;
        Cart cc=new Cart();
        List<Item> nn = new ArrayList<>();
        User us=new User();
        us.setPassword("test");
        us.setUsername("user1");
        us.setId(1l);

        request.setItemId(1l);
        request.setQuantity(5);
        request.setUsername("user1");
        Item it=new Item();
        it.setId(1l);
        it.setName("uuu");
        it.setName("fruit");

        it.setPrice(new BigDecimal(42));
        nn.add(it);
        cc.setTotal(new BigDecimal(45));
        cc.setId(45l);
        cc.setItems(nn);
        us.setCart(cc);

        when(userRepo.findByUsername("user1")).thenReturn(null);
        ResponseEntity<Cart> response=cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());

        when(userRepo.findByUsername("user1")).thenReturn(us);
        when(itemRepo.findById(1l)).thenReturn(Optional.of(it));
        when(cartRepo.save(cc)).thenReturn(cc);

        ResponseEntity<Cart> response1=cartController.addTocart(request);
        Cart crt= response1.getBody();
        assertNotNull(crt);
        assertEquals(200,response1.getStatusCodeValue());
        assertEquals(6,cc.getItems().stream().count());


    }
    @Test
    public void test_remove_from_cart(){
        ModifyCartRequest request =new ModifyCartRequest() ;
        Cart cc=new Cart();

        User us=new User();
        us.setPassword("test");
        us.setUsername("user1");
        us.setId(1l);

        request.setItemId(1l);
        request.setQuantity(5);
        request.setUsername("user1");
        Item it=new Item();
        it.setId(1l);
        it.setName("uuu");
        it.setName("fruit");

        it.setPrice(new BigDecimal(42));
        cc.setTotal(new BigDecimal(45));
        cc.setId(45l);

        us.setCart(cc);

        when(userRepo.findByUsername("user1")).thenReturn(us);
        when(itemRepo.findById(1l)).thenReturn(Optional.of(it));
        when(cartRepo.save(cc)).thenReturn(cc);
        ResponseEntity<Cart> resp=cartController.removeFromcart(request);
        assertNotNull(resp);
        assertEquals(200,resp.getStatusCodeValue());
    }



}
