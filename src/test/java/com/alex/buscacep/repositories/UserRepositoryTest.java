package com.alex.buscacep.repositories;

import com.alex.buscacep.domain.models.User;
import com.alex.buscacep.domain.models.UserRole;
import com.alex.buscacep.infra.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager; //usaremos para criar um novo usuário no testdb

    @Test
    @DisplayName("Should get user successfully from testdb")
    void findUserByUsernameCase1(){
        User data = new User("Alex", "senha", UserRole.ADMIN);
        this.createUser(data);
        UserDetails foundedUser = this.userRepository.findByUsername(data.getUsername());
        assertThat(foundedUser.getUsername() == "Alex").isTrue();
    }

    @Test
    void findUserByUsernameCase2(){
    }




    private User createUser(User data){
        User user = new User(data.getUsername(), data.getPassword(), data.getRole());
        this.entityManager.persist(user);
        return user;
    }

}
