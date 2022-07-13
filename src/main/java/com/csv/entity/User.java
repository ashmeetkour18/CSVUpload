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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String mobileNo;
    private String address;
    @Column(columnDefinition = "boolean default false")
    private boolean deleteStatus;
    @OneToOne
    private Role role;
}
