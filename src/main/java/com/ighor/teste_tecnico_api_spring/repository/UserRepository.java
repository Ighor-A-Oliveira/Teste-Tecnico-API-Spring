package com.ighor.teste_tecnico_api_spring.repository;

import com.ighor.teste_tecnico_api_spring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //here we are returning a UserDetails because this class is implemented by the User Class
    // and it has all the auth details of our user
    Optional<User> findByEmail(String userName);

    Optional<User> findByCpf(String cpf);

}
