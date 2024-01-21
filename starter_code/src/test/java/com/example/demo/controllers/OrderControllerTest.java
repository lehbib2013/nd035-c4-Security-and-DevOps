package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private UserRepository userRepo = mock(UserRepository.class);
    private OrderRepository  orderRepo = mock(OrderRepository.class);

    @Before
    public void SetUp(){
        orderController = new OrderController();

        TestUtils.injectObjects(orderController,"userRepository",userRepo);
        TestUtils.injectObjects(orderController,"orderRepository",orderRepo);

    }
    @Test
    public void test_if_order_submitted_successfully() {
        List<Item> cartItems = new ArrayList<Item>();
        cartItems.add(new Item(1L,"item1",new BigDecimal(45),"for saL1"));
        cartItems.add(new Item(2L,"item2",new BigDecimal(60),"for saL2"));
        cartItems.add(new Item(3L,"item3",new BigDecimal(521),"for saL3"));

        Cart newCart = new Cart();
        newCart.setItems(cartItems);
        newCart.setTotal(new BigDecimal(477));
        User userSuccess = new User();
        UserOrder userOrder = new UserOrder();
        userOrder.setUser(userSuccess);
        userOrder.setItems(cartItems);
        userOrder.setTotal(new BigDecimal(477));
        userSuccess.setUsername("testUser");
        userSuccess.setPassword("google");
        userSuccess.setCart(newCart);
        UserOrder order = UserOrder.createFromCart(newCart);
        when(userRepo.findByUsername("testuser")).thenReturn(userSuccess);
        when(orderRepo.save(order)).thenReturn(userOrder);
        final ResponseEntity<UserOrder> response = orderController.submit("testuser");

        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());
        UserOrder userOrderss= response.getBody();
        assertNotNull(userOrderss);
        assertEquals(3,userOrderss.getItems().stream().count());

        assertEquals(new BigDecimal(477),userOrderss.getTotal());



    }
    @Test
    public void test_if_order_submitted_failed() {

        List<Item> cartItems = new ArrayList<Item>();
        cartItems.add(new Item(1L,"item1",new BigDecimal(45),"for saL1"));
        cartItems.add(new Item(2L,"item2",new BigDecimal(60),"for saL2"));
        cartItems.add(new Item(3L,"item3",new BigDecimal(521),"for saL3"));

        Cart newCart = new Cart();
        newCart.setItems(cartItems);
        newCart.setTotal(new BigDecimal(477));
        User userSuccess = new User();
        UserOrder userOrder = new UserOrder();
        userOrder.setUser(userSuccess);
        userOrder.setItems(cartItems);
        userOrder.setTotal(new BigDecimal(477));
        userSuccess.setUsername("testUser");
        userSuccess.setPassword("google");
        userSuccess.setCart(newCart);
        UserOrder order = UserOrder.createFromCart(newCart);
        when(userRepo.findByUsername("testuser")).thenReturn(userSuccess);
        when(orderRepo.save(order)).thenReturn(userOrder);
        final ResponseEntity<UserOrder> response = orderController.submit("testuser1");

        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());

    }
    @Test
    public void test_get_histry() {
        User us=new User();
        us.setPassword("yyy");
        us.setUsername("usertest");
        UserOrder userOrder = new UserOrder();
        userOrder.setUser(us);
        userOrder.setTotal(new BigDecimal(477));
        List<UserOrder> listOfUserOrders = new ArrayList<>();
        listOfUserOrders.add(userOrder);

        when(orderRepo.findByUser(us)).thenReturn(listOfUserOrders);
        when(userRepo.findByUsername("usertest")).thenReturn(us);
        Authentication currentLoggedIn =new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(boolean b) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return "usertest";
            }
        };
        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(currentLoggedIn,"usertest");

        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());
    }


}
