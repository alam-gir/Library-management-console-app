package controller.feature;

import model.Book;
import service.BookService;
import service.BookCopyService;
import util.*;

public class BookDetailsFeature {

    private final BookService bookService;
    private final BookCopyService copyService;

    public BookDetailsFeature(BookService bookService) {
        this.bookService = bookService;
        this.copyService = new BookCopyService();
    }

    public void start(String bookId) {

        Book b = bookService.getBook(bookId);
        if (b == null) {
            DisplayHelper.error("Book not found");
            return;
        }

        ScreenUtil.clear();
        DisplayHelper.printSection("Book Details");

        System.out.println("ID    : " + b.getId());
        System.out.println("Title : " + b.getTitle());
        System.out.println("Author: " + b.getAuthor());
        System.out.println("ISBN  : " + b.getIsbn());
        System.out.println("Available Copies: " + copyService.availableCount(bookId));

        DisplayHelper.emptyLine();
        System.out.println("1. View Book Copies");
        System.out.println("2. Back");

        int c = InputHelper.readInt("Choose", 1, 2);

        if (c == 1) {
            new BookCopyFeature(copyService).start(bookId);
        }
    }
}
