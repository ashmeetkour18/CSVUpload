package com.csv.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String regNumber;
    private String rto;
    private String registrationState;
    private Integer registrationYear;
    private String make;
    private String model;
    private Integer mileage;
    private String bodyType;
    private Integer carScore;
    private String variant;
    private String chassisNumber;
    private String colour;
    private Integer yearOfManufacture;

}
