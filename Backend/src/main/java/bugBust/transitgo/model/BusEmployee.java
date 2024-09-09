package bugBust.transitgo.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.security.SecureRandom;



public class BusEmployee extends User {
    public BusEmployee(Long id, String fname, String lname, String password, String username, String email, String busid, String type) {
        super(id, fname, lname, password, username, email, busid, type);
    }
}