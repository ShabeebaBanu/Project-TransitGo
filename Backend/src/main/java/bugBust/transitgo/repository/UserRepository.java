package bugBust.transitgo.repository;

import bugBust.transitgo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//Will not be able to create instances from this repository

@Repository
public interface UserRepository extends JpaRepository<User, Long> {



}
