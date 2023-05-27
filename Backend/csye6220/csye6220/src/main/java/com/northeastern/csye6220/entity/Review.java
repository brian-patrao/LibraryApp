package com.northeastern.csye6220.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "review")
@Data
public class Review {

    public Review() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name="user_email")
    private String email;

    @Column
    @CreationTimestamp
    private Date date;

    @Column
    private double rating;

    @Column(name="book_id")
    private long bookId;

    @Column(name="review_description")
    private String reviewDescr;
}