package com.northeastern.csye6220.dao;

import com.northeastern.csye6220.entity.Review;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class ReviewDao extends DAO{
    public ReviewDao() {}

    public PageImpl<Review> findByBookId(long bookId, int page, int size) {
        Session session = getSession();
        Query q = session.createQuery("SELECT r FROM Review r WHERE r.bookId = :bookId",Review.class);
        q.setParameter("bookId",  bookId);
        q.setFirstResult(page * size);
        q.setMaxResults(size);
        List<Review> reviews = q.list();
        close();
        long totalElements = getTotalElementsByBookId(bookId);

        return new PageImpl<>(reviews, PageRequest.of(page, size), totalElements);
    }

    private Long getTotalElementsByBookId(long bookId) {
        Query q = getSession().createQuery("SELECT COUNT(r) FROM Review r WHERE r.bookId = :bookId", Review.class);
        q.setParameter("bookId",  bookId);
        Long count = (Long) q.getSingleResult();
        close();
        return count;
    }

    public Review findByEmailAndBookId(String email, Long bookId) throws Exception {
        Session session = getSession();
        Query q = session.createQuery("SELECT r FROM Review r WHERE r.email = :email AND r.bookId = :bookId", Review.class);
        q.setParameter("bookId",  bookId);
        q.setParameter("email",  email);
        Review review = null;
        try {
            review = (Review) q.getSingleResult();
        } catch (NoResultException e) {

        }
        close();
        return review;
    }
    public void save(Review review) {
        try{
            begin();
            getSession().merge(review);
            commit();
            close();
        } catch(Exception e) {
            rollback();
            close();
            throw e;
        }
    }
}
