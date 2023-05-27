package com.northeastern.csye6220.dao;

import com.northeastern.csye6220.entity.Checkout;
import jakarta.persistence.NoResultException;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class CheckoutDao extends DAO{

    public Checkout findByEmailAndBookId(String email, Long bookId) throws Exception {
        Query q = getSession().createQuery("SELECT c FROM Checkout c WHERE c.email = :email AND c.bookId = :bookId");
        q.setParameter("bookId",  bookId);
        q.setParameter("email",  email);
        Checkout checkout = null;
        try {
            checkout = (Checkout) q.getSingleResult();
        } catch (NoResultException e) {
            throw e;
        }
        close();
        return checkout;
    }

    public List<Checkout> findBooksByEmail(String email) {
        Query q = getSession().createQuery("SELECT c FROM Checkout c WHERE c.email = :email",Checkout.class);
        q.setParameter("email",  email);
        List<Checkout> checkouts = q.list();
        close();
        return checkouts;
    }

    public void save(Checkout checkout) {
        try{
            begin();
            getSession().merge(checkout);
            commit();
            close();
        } catch (HibernateException e) {
            rollback();
            close();
            throw e;
        }
    }

    public long getTotalElements(String email, Long bookId) {
        Query q = getSession().createQuery("SELECT COUNT(c) FROM Checkout c WHERE c.email = :email AND c.bookId = :bookId");
        q.setParameter("bookId",  bookId);
        q.setParameter("email",  email);
        Long count = (Long) q.getSingleResult();
        close();
        return count;
    }
}
