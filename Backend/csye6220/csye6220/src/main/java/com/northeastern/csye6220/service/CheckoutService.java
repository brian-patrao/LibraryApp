package com.northeastern.csye6220.service;

import com.northeastern.csye6220.dao.BookDao;
import com.northeastern.csye6220.dao.CheckoutDao;
import com.northeastern.csye6220.entity.Book;
import com.northeastern.csye6220.entity.Checkout;
import com.northeastern.csye6220.responsemodels.ShelfCurrentLoansResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
@Service
public class CheckoutService {
    @Autowired
    private BookDao bookDao;
    @Autowired
    private CheckoutDao checkoutDao;

    public CheckoutService(BookDao bookDao,CheckoutDao checkoutDao) {
        this.bookDao = bookDao;
        this.checkoutDao = checkoutDao;
    }

    public Book findByEmailAndBookId(String email, long bookId) throws Exception{
        Book book = bookDao.getBookById(bookId);
        Checkout checkout = checkoutDao.findByEmailAndBookId(email,bookId);

        if(checkout!= null || book.getCopiesAvailable() <= 0) {
            throw new Exception("Book has already been checked out or No copies available");
        }

        book.setCopiesAvailable(book.getCopiesAvailable() - 1);
        bookDao.saveBook(book);

        checkoutDao.save(new Checkout(email, LocalDate.now().toString(), LocalDate.now().plusDays(7).toString(),book.getId()));
        return book;
    }

    public Boolean checkoutBookByUser(String email, Long bookId) throws Exception {
        Checkout checkout = checkoutDao.findByEmailAndBookId(email, bookId);
        if (checkout != null) {
            return true;
        } else {
            return false;
        }
    }

    public int currentCheckoutCount(String email) {
        List<Checkout> checkout = checkoutDao.findBooksByEmail(email);
        return checkout.size();
    }

    public List<ShelfCurrentLoansResponse> currentLoans(String userEmail) throws Exception {

        List<ShelfCurrentLoansResponse> shelfCurrentLoansResponses = new ArrayList<>();

        List<Checkout> checkoutList = checkoutDao.findBooksByEmail(userEmail);
        List<Long> bookIdList = new ArrayList<>();

        for (Checkout i: checkoutList) {
            bookIdList.add(i.getBookId());
        }

        List<Book> books = bookDao.findBooksByIdList(bookIdList);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (Book book : books) {
            Optional<Checkout> checkout = checkoutList.stream()
                    .filter(x -> x.getBookId() == book.getId()).findFirst();

            if (checkout.isPresent()) {

                Date d1 = sdf.parse(checkout.get().getReturnDate());
                Date d2 = sdf.parse(LocalDate.now().toString());

                TimeUnit time = TimeUnit.DAYS;

                long difference_In_Time = time.convert(d1.getTime() - d2.getTime(),
                        TimeUnit.MILLISECONDS);

                shelfCurrentLoansResponses.add(new ShelfCurrentLoansResponse(book, (int) difference_In_Time));
            }
        }
        return shelfCurrentLoansResponses;
    }
}
