package com.northeastern.csye6220.dao;

import com.northeastern.csye6220.entity.Book;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookDao extends DAO{
    public BookDao() {};

    public PageImpl<Book> getBooks(int page, int size) throws Exception {
        try {
            Query q = getSession().createQuery("FROM Book");
            q.setFirstResult(page * size);
            q.setMaxResults(size);
            List<Book> books = q.list();
            close();
            long totalElements = getTotalElements();
            return new PageImpl<>(books, PageRequest.of(page, size), totalElements);
        }
        catch (HibernateException e) {
            rollback();
            throw new Exception("Books Dao Exception - "+e.getMessage());
        }
    }

    public Book getBookById(Long id) throws Exception {
        Book book = getSession().get(Book.class, id);
        close();
        if(book == null) {
            throw new Exception("Book not found");
        }
        return book;
    }

    public PageImpl<Book> findByTitle(String title, int page, int size) {

        Query q = getSession().createQuery("SELECT b FROM Book b WHERE b.title LIKE :titleParam");
        q.setParameter("titleParam", "%" + title + "%");
        q.setFirstResult(page * size);
        q.setMaxResults(size);
        List<Book> books = q.list();
        close();
        long totalElements = getTotalElementsByTitle(title);

        return new PageImpl<>(books, PageRequest.of(page, size), totalElements);
    }

    public PageImpl<Book> findByCategory(String category, int page, int size) {

        Query q = getSession().createQuery("SELECT b FROM Book b WHERE b.category = :categoryParam");
        q.setParameter("categoryParam",  category);
        q.setFirstResult(page * size);
        q.setMaxResults(size);
        List<Book> books = q.list();
        close();
        long totalElements = getTotalElementsByCategory(category);

        return new PageImpl<>(books, PageRequest.of(page, size), totalElements);
    }

    private long getTotalElements() {
        Query q = getSession().createQuery("SELECT COUNT(b) FROM Book b");
        Long count = (Long) q.getSingleResult();
        close();
        return count;
    }

    private long getTotalElementsByTitle(String title) {
        Query q = getSession().createQuery("SELECT COUNT(b) FROM Book b WHERE b.title LIKE :titleParam");
        q.setParameter("titleParam", "%" + title + "%");
        Long count = (Long) q.getSingleResult();
        close();
        return count;
    }

    private long getTotalElementsByCategory(String category) {
        Query q = getSession().createQuery("SELECT COUNT(b) FROM Book b WHERE b.category = :categoryParam");
        q.setParameter("categoryParam",  category );
        Long count = (Long) q.getSingleResult();
        close();
        return count;
    }

    public void saveBook(Book book) {
        try{
            begin();
            getSession().merge(book);
            commit();
            close();
        } catch (HibernateException e) {
            rollback();
            throw e;
        }
    }

    public List<Book> findBooksByIdList(List<Long> bookIds) {
        String hql = "FROM Book b WHERE b.id IN (:bookIds)";
        Query query = getSession().createQuery(hql);
        query.setParameterList("bookIds", bookIds);
        List<Book> books = query.getResultList();
        close();
        return books;
    }

    public void addBook(Book book) {
        try{
            begin();
            getSession().save(book);
            commit();
            close();
        } catch(HibernateException e) {
            rollback();
            close();
            throw e;
        }
    }

    public void deleteById(long id) {
        try{
            begin();
            Book book = getSession().get(Book.class, id);
            if (book != null) {
                getSession().delete(book);
            }
            commit();
        } catch (HibernateException e) {
            rollback();
            close();
            throw e;
        }
    }

}
