package com.csv.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryId;
    @Column(unique = true)
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
    @Column(columnDefinition = "boolean default false")
    private boolean deleteStatus = false;

}
