package com.scaler.inheritancelab;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Seller extends User {

    private String shopName;
}
