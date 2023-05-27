package com.northeastern.csye6220.service;

import com.northeastern.csye6220.dao.ReviewDao;
import com.northeastern.csye6220.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

@Service
public class ReviewService {

    private ReviewDao dao;

    public ReviewService(ReviewDao dao) {
        this.dao = dao;
    }
    public PageImpl<Review> findByBookId(long id, int page, int size) {
        return dao.findByBookId(id,page,size);
    }

    public void saveReview(String email, Review review) throws Exception {
        Review reviewEntry = dao.findByEmailAndBookId(email,review.getBookId());
        if(reviewEntry != null) {
            throw new Exception("Review is already created");
        }
        review.setEmail(email);
        review.setDate(Date.valueOf(LocalDate.now()));
        dao.save(review);
    }

    public Boolean userReviewListed(String email, long bookId) throws Exception {
        Review reviewEntry = dao.findByEmailAndBookId(email,bookId);
        if(reviewEntry != null) {
            return true;
        } else {
            return false;
        }
    }
}
