package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserProfile;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);
   // protected MockMvc mockMvc;

@Before
public void SetUp(){
 userController = new UserController();
  //  this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    TestUtils.injectObjects(userController,"userRepository",userRepo);
    TestUtils.injectObjects(userController,"cartRepository",cartRepo);
    TestUtils.injectObjects(userController,"bCryptPasswordEncoder",encoder);
}

@Test
public void create_user_happy_path() {
   // byte[] salt = new byte[16];when(createSalt()).thenReturn(salt);
  // when(encoder.encode("testPassword")).thenReturn("thisIsHahed");
    CreateUserRequest r = new CreateUserRequest();
    r.setUsername("test");
    r.setPassword("testPassword");
    r.setConfirmPassword("testPassword");
    final ResponseEntity<User> response = userController.createUser(r);

    assertNotNull(response);
    assertEquals(200,response.getStatusCodeValue());
    User u= response.getBody();
    assertNotNull(u);
    assertEquals(0,u.getId());
    assertEquals(0,u.getId());
    assertNotEquals("thisIsHahed",u.getPassword());
}
    @Test
    public void test_find_by_id() {
        User us=new User();
        us.setId(1l);
        us.setUsername("testuser");
        us.setPassword("pass");
        when(userRepo.findById(1l)).thenReturn(Optional.of(us));
        ResponseEntity<User> resp=userController.findById(1l);
        assertNotNull(resp);
        assertEquals(200,resp.getStatusCodeValue());
        User u= resp.getBody();
        assertNotNull(u);
        //assertEquals(0,u.getId());
        assertEquals("testuser",u.getUsername());
        assertEquals("pass",u.getPassword());
        assertEquals(1l,u.getId());

    }
    @Test
    public void test_find_by_username() {
        User us=new User();
        us.setId(1l);
        us.setUsername("testuser");
        us.setPassword("pass");
        UserProfile up=new UserProfile();
        up.setAge(15);
        up.setUser(us);
        up.setHobby("lecture");
        up.setFullName("fullname");
        up.setCity("Nouakchott");
        us.setProfile(up);
        when(userRepo.findByUsername("testuser")).thenReturn(us);
        ResponseEntity<User> resp=userController.findByUserName("testuser");
        assertNotNull(resp);
        assertEquals(200,resp.getStatusCodeValue());
        User u= resp.getBody();
        assertNotNull(u);
        //assertEquals(0,u.getId());
        assertNotNull(us);
        assertEquals("testuser",u.getUsername());
        assertEquals("pass",u.getPassword());
        assertEquals(1l,u.getId());
    }
    @Test
    //@WithMockUser(username = "user1", password = "pwd", roles = "USER")
    public void test_get_user_profile() throws Exception {
        /*mockMvc.perform(get("/userprofile"))
                .andExpect(status().isNotFound());*/
        User us=new User();
        us.setPassword("yyy");
        us.setUsername("usertest");
        UserProfile up=new UserProfile();
        up.setAge(15);
        up.setUser(us);
        up.setHobby("lecture");
        up.setFullName("fullname");
        up.setCity("Nouakchott");
        us.setProfile(up);
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


        ResponseEntity<UserProfile> userProfile = userController.getUserProfile(currentLoggedIn) ;
        assertNotNull(userProfile);
        assertEquals(200,userProfile.getStatusCodeValue());
        UserProfile upr= userProfile.getBody();
        assertNotNull(upr);
        //assertEquals(0,u.getId());
        assertEquals("usertest",upr.getUser().getUsername());
        assertEquals("yyy",upr.getUser().getPassword());
      //  assertEquals(1l,u.getId());
    }

    @Test
    //@WithMockUser(username = "user1", password = "pwd", roles = "USER")
    public void test_cart_details() throws Exception {
        Cart cc=new Cart();
        List<Item> nn = new ArrayList<>();
        User us=new User();
        us.setPassword("yyy");
        us.setUsername("usertest");
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
        cc.setUser(us);


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
        ResponseEntity<Cart> userCart = userController.getCartDetails(currentLoggedIn) ;
        assertNotNull(userCart);
        assertEquals(200,userCart.getStatusCodeValue());
        Cart crt= userCart.getBody();
        assertNotNull(crt);
        //assertEquals(0,u.getId());
        assertEquals("usertest",crt.getUser().getUsername());
        assertEquals("yyy",crt.getUser().getPassword());
    }

}
