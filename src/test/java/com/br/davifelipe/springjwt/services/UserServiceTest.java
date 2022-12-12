package com.br.davifelipe.springjwt.services;

import com.br.davifelipe.springjwt.model.Privilege;
import com.br.davifelipe.springjwt.model.User;
import com.br.davifelipe.springjwt.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repo;

    @Test
    void update() {

        User user = new User();
        user.setId(1);
        user.setName("steve");
        user.setEmail("steve@steve.com");

        List privilegeList = new ArrayList<>();
        Privilege privilege = new Privilege();
        privilege.setId(1);
        privilege.setName("CATEGORY_READ_PRIVILEGE");
        privilegeList.add(privilege);
        user.setPrivileges(privilegeList);

        Optional userOptional = Optional.of(user);

        when(this.repo.findById(user.getId()))
                .thenReturn(userOptional);

        service.update(user);
    }
}