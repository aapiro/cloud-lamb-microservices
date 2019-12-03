package com.example.demo;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Student3 {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String passportNumber;

}

