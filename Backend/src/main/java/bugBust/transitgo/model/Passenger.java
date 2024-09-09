package bugBust.transitgo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;



public class Passenger extends User{
    public Passenger(Long id, String fname, String lname, String password, String username, String email, String busid, String type) {
        super(id, fname, lname, password, username, email, busid, type);
    }
}
