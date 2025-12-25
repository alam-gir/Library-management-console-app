package controller;

import controller.feature.BookListFeature;
import service.BookService;

public class BookManagementController {

    private final BookService bookService;

    public BookManagementController(BookService bookService) {
        this.bookService = bookService;
    }

    public void start() {
        new BookListFeature(bookService).start();
    }
}
