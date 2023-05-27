package com.northeastern.csye6220.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "checkout")
@Data
public class Checkout {

    public Checkout() {}

    public Checkout(String email, String checkout, String returnDate, Long bookId) {
        this.email = email;
        this.checkout = checkout;
        this.returnDate = returnDate;
        this.bookId = bookId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_email")
    private String email;

    @Column(name = "checkout_date")
    private String checkout;

    @Column(name = "return_date")
    private String returnDate;

    @Column(name = "book_id")
    private Long bookId;
}
