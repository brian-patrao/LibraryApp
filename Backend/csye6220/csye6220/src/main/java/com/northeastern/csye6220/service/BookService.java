package com.northeastern.csye6220.service;

import com.northeastern.csye6220.dao.BookDao;
import com.northeastern.csye6220.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    @Autowired
    private BookDao bookDao;

    public BookService(BookDao bookDao) {
        this.bookDao = bookDao;
    }
    public PageImpl<Book> getBooks(int page, int size) throws Exception {
        return bookDao.getBooks(page,size);
    }

    public Book getBookById(Long id) throws Exception {
        return bookDao.getBookById(id);
    }

    public PageImpl<Book> findByTitle(String title, int page, int size) {
        return bookDao.findByTitle(title,page,size);
    }

    public PageImpl<Book> findByCategory(String category, int page, int size) {
        return bookDao.findByCategory(category,page,size);
    }

    public void addBook(Book book) {
        bookDao.addBook(book);
    }

    public Book updateBook(Book updatedBook) throws Exception {
        Book existingBook =  bookDao.getBookById(updatedBook.getId());
        if (updatedBook.getTitle() != null) {
            existingBook.setTitle(updatedBook.getTitle());
        }
        if (updatedBook.getAuthor() != null) {
            existingBook.setAuthor(updatedBook.getAuthor());
        }
        if (updatedBook.getDescription() != null) {
            existingBook.setDescription(updatedBook.getDescription());
        }
        if (updatedBook.getCopies() != 0) {
            existingBook.setCopies(updatedBook.getCopies());
        }
        if (updatedBook.getCopiesAvailable() != 0) {
            existingBook.setCopiesAvailable(updatedBook.getCopiesAvailable());
        }
        if (updatedBook.getCategory() != null) {
            existingBook.setCategory(updatedBook.getCategory());
        }
        bookDao.saveBook(existingBook);
        return updatedBook;
    }

    public void deleteBookById(Long id) {
        bookDao.deleteById(id);
    }
}
