package com.thesnoozingturtle.moneymanagerrestapi;

import com.thesnoozingturtle.moneymanagerrestapi.config.AppConstants;
import com.thesnoozingturtle.moneymanagerrestapi.entity.Role;
import com.thesnoozingturtle.moneymanagerrestapi.repositories.RoleRepo;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MoneyManagerRestApiApplication implements CommandLineRunner {
    private final RoleRepo roleRepo;

    public MoneyManagerRestApiApplication(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    public static void main(String[] args) {
        SpringApplication.run(MoneyManagerRestApiApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Override
    public void run(String... args) throws Exception {
        Role admin = new Role();
        admin.setId(AppConstants.ADMIN_USER);
        admin.setRoleName("ROLE_ADMIN");

        Role normal = new Role();
        normal.setId(AppConstants.NORMAL_USER);
        normal.setRoleName("ROLE_NORMAL");
        this.roleRepo.save(admin);
        this.roleRepo.save(normal);
    }
}
