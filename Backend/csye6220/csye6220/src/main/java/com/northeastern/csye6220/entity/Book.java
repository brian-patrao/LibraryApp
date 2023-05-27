package com.northeastern.csye6220.entity;

import lombok.Data;
import jakarta.persistence.*;


@Entity(name = "Book")
@Table(name = "book")
@Data
public class Book {

    public Book() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;
    @Column
    private String title;
    @Column
    private String author;
    @Column
    private String description;
    @Column
    private int copies;
    @Column(name = "copies_available")
    private int copiesAvailable;
    @Column
    private String category;
}
