package com.northeastern.csye6220.controller;

import com.northeastern.csye6220.entity.Book;
import com.northeastern.csye6220.exceptions.CustomErrorResponse;
import com.northeastern.csye6220.responsemodels.ShelfCurrentLoansResponse;
import com.northeastern.csye6220.service.CheckoutService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checkout")
@CrossOrigin(origins = "http://localhost:3000")
public class CheckoutController {

    private CheckoutService checkoutService;
    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PutMapping
    public ResponseEntity<?> findByEmailAndBookId(@RequestParam("bookId") long id, @RequestParam("userEmail") String email) {
        try{
            Book book = checkoutService.findByEmailAndBookId(email,id);
            return ResponseEntity.ok(book);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorResponse(404,e.getMessage()));
        }

    }

    @GetMapping("/checkedOutBy")
    public Boolean checkoutBookByUser(@RequestParam("userEmail") String email,
                                      @RequestParam("bookId") Long bookId) throws Exception {
        return checkoutService.checkoutBookByUser(email, bookId);
    }

    @GetMapping("/count")
    public int currentCheckoutCount(@RequestParam("userEmail") String email) {
        return checkoutService.currentCheckoutCount(email);
    }

    @GetMapping("/getCheckedOut")
    public List<ShelfCurrentLoansResponse> currentCheckout(@RequestParam("userEmail") String email)
            throws Exception
    {
        return checkoutService.currentLoans(email);
    }
}
