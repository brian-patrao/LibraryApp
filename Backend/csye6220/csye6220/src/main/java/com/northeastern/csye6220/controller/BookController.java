package com.northeastern.csye6220.controller;
import com.northeastern.csye6220.entity.Book;
import com.northeastern.csye6220.exceptions.CustomErrorResponse;
import com.northeastern.csye6220.service.BookService;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book")
@CrossOrigin(origins = "http://localhost:3000")
public class BookController {
    private BookService service;
    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(value= "page", defaultValue = "0") int page, @RequestParam(value = "size",defaultValue = "10") int size) throws Exception {
        try {
            PageImpl<Book> books = service.getBooks(page, size);
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorResponse(404,e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) throws Exception {
        try {
            Book book = service.getBookById(id);
            return ResponseEntity.ok(book);
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorResponse(404,e.getMessage()));
        }
    }

    @GetMapping("/search/getByTitle")
    public ResponseEntity<?> getByTitle(@RequestParam("title") String title,
                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "10") int size) throws Exception {
        try {
            PageImpl<Book> books = service.findByTitle(title, page, size);
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorResponse(404,e.getMessage()));
        }
    }

    @GetMapping("/search/getByCategory")
    public ResponseEntity<?> getByCategory(@RequestParam("category") String category,
                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "10") int size) throws Exception {
        try {
            PageImpl<Book> books = service.findByCategory(category, page, size);
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorResponse(404,e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> AddBook(@RequestBody Book book) {
        try {
            service.addBook(book);
            return new ResponseEntity<Book>(book, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorResponse(400,e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<?> updateBook(@RequestBody Book book) {
        try {
            Book updatedBook = service.updateBook(book);
            return ResponseEntity.ok(updatedBook);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorResponse(400,e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        try {
            service.deleteBookById(id);
            String message = "Book with ID " + id + " has been deleted.";
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorResponse(400,e.getMessage()));
        }
    }
}
