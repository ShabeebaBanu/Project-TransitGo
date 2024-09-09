package bugBust.transitgo.service;

import bugBust.transitgo.exception.UserNotFoundException;
import bugBust.transitgo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private final CrudRepository<User,Long> userRepository;


    public UserService(CrudRepository<User, Long> userCrudRepository) {

        this.userRepository = userCrudRepository;
    }

    //User login Authentication
//    public String userAuthentication(User user) throws UserNotFoundException{
//        BCryptPasswordEncoder bcrypt  = new BCryptPasswordEncoder();
//        Optional<User> optUser = userRepository.findById(user.getUsername());
//        if(optUser.isPresent()){
//            User dbUser = optUser.get();
//            if(bcrypt.matches(user.getPassword(),dbUser.getPassword()))
//                return "Authenticated User";
//            else
//                return "Incorrect Password";
//        }
//        throw new UserNotFoundException("User not found");
//    }


    //POST methods
    //Add a new user
    public User saveUser(User user){
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        String encryptedPwd = bcrypt.encode(user.getPassword());
        user.setPassword(encryptedPwd);
        return userRepository.save(user);
    }

    //Add new users
    public List<User> saveUsers(List<User> users){
        return (List<User>) userRepository.saveAll(users);
    }


    //GET Methods
    //Get all users
    public List<User> getUsers(){
        return (List<User>) userRepository.findAll();
    }

    //Get a user by Id
    public User getUserById(long id){
        return userRepository.findById(id).orElseThrow(()->new UserNotFoundException(id));
    }

    //DELETE method
    //delete a user by id
    public String deleteUserById(long id){
        if (!userRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
        return "User with id "+id+" has been deleted successful";
    }

    //UPDATE method
    //update a user
    public User updateUser(User newUser,Long id){
        return userRepository.findById(id)
                .map(user -> {
                    user.setId(newUser.getId());
                    user.setFname(newUser.getFname());
                    user.setLname(newUser.getLname());
                    user.setUsername(newUser.getUsername());
                    user.setEmail(newUser.getEmail());
                    user.setPassword(newUser.getPassword());
                    user.setBusid(newUser.getBusid());
                    user.setBusid(newUser.getBusid());
                    return userRepository.save(user);
                }).orElseThrow(()->new UserNotFoundException(id));

    }

}
