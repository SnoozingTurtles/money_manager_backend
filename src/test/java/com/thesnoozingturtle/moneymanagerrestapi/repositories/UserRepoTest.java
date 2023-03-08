package com.thesnoozingturtle.moneymanagerrestapi.repositories;

import com.thesnoozingturtle.moneymanagerrestapi.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    @AfterEach
    void tearDown() {
        this.userRepo.deleteAll();
    }

//    @Test
//    void itShouldFindUserByEmailIfEmailExists() {
//        //given
//        String email = "abc@gmail.com";
//        User user = new User(1, "Test", "abc", email, null, null, null, null, null);
//        User savedUser = this.userRepo.save(user);
//
//        //when
//        User expectedUser = this.userRepo.findByEmail(email);
//
//        //then
//        assertThat(expectedUser).isEqualTo(savedUser);
//    }
    @Test
    void itShouldReturnNullForFindUserByEmailIfEmailDoesNotExists() {
        //given
        String email = "abc@gmail.com";

        //when
        User expectedUser = this.userRepo.findByEmail(email);

        //then
        assertThat(expectedUser).isNull();
    }
}