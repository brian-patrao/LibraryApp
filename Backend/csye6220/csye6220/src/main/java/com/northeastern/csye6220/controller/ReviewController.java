package com.northeastern.csye6220.controller;

import com.northeastern.csye6220.entity.Review;
import com.northeastern.csye6220.exceptions.CustomErrorResponse;
import com.northeastern.csye6220.service.ReviewService;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
@CrossOrigin(origins = "http://localhost:3000")
public class ReviewController {
    private ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }
    @GetMapping("/search/findByBookId")
    public ResponseEntity<?> findByBookId(@RequestParam("bookId") long id,
                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                          @RequestParam(value = "size", defaultValue = "10") int size) throws Exception {
        try {
            PageImpl<Review> reviews = service.findByBookId(id, page, size);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorResponse(404,e.getMessage()));
        }
    }

    @PostMapping
    public void postReview(@RequestParam(value="userEmail") String email,
                           @RequestBody Review review) throws Exception {
        if (email == null) {
            throw new Exception("User email is missing");
        }
        if (review.getBookId() == 0) {
            throw new Exception("Book Id is missing");
        }
        service.saveReview(email, review);
    }

    @GetMapping("reviewBookByUser")
    public Boolean reviewBookByUser(@RequestParam(value="userEmail") String email, @RequestParam(value="bookId") long bookId) throws Exception {
        if (email == null) {
            throw new Exception("User email is missing");
        }
        return service.userReviewListed(email,bookId);
    }

}

