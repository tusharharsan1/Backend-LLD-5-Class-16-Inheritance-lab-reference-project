package com.scaler.inheritancelab;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InheritancelabApplication {

    public static void main(String[] args) {
        SpringApplication.run(InheritancelabApplication.class, args);
    }

    @Bean
    CommandLineRunner lab(UserRepo userRepo) {
        return args -> {
            Customer customer = new Customer();
            customer.setEmail("asha@shopnest.com");
            customer.setShippingAddress("12 MG Road, Pune");
            userRepo.save(customer);

            Seller seller = new Seller();
            seller.setEmail("ravi@shopnest.com");
            seller.setShopName("Ravi Watches");
            userRepo.save(seller);

            userRepo.findAll().forEach(user -> System.out.println(user.getClass().getSimpleName() + ": " + user.getEmail()));
        };
    }
}
