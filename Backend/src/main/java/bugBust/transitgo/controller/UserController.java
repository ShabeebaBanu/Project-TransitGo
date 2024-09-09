package bugBust.transitgo.controller;

import bugBust.transitgo.model.User;
import bugBust.transitgo.service.UserService;
import jakarta.persistence.GenerationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("http://localhost:3000")
public class  UserController <T extends User> {

    @Autowired
   private UserService userService;


    //POST
    //Add a user
    @PostMapping("/user")
    public User saveUser(@RequestBody User user){
          return userService.saveUser(user);
    }
    //Add new users
    @PostMapping("/users")
    public List<User> saveUsers(@RequestBody List<User> users){
        return (List<User>) userService.saveUsers(users);
    }

    //GET
    //Get all users
    @GetMapping("/users")
    public List<User> getUsers(){
        return (List<User>) userService.getUsers();
    }

    //Get a user by id
    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable long id){
        return userService.getUserById(id);
    }
    //UPDATE
    //update user
    @PutMapping("/updateUser/{id}")
    public User updateUser(@RequestBody User newUser, @PathVariable Long id){
        return userService.updateUser(newUser, id);
    }

    //DELETE
    //Delete user by id
    @DeleteMapping("/deleteUser/{id}")
    public String deleteUserById(@PathVariable long id){
        return userService.deleteUserById(id);
    }
}
