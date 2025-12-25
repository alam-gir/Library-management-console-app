package service;

import model.Book;
import repository.BookRepository;
import util.IdGenerator;
import java.util.List;

public class BookService {

    private final BookRepository repository;

    public BookService() {
        this.repository = new BookRepository();
    }

    public List<Book> getBooks(int page, int size) {
        return repository.findPage(page, size);
    }

    public int getTotalBooks() {
        return repository.count();
    }

    public Book getBook(String bookId) {
        return repository.findById(bookId);
    }

    public void addBook(String isbn, String title, String author) {

        Book b = new Book();
        b.setId(IdGenerator.generate("BOOK"));
        b.setIsbn(isbn);
        b.setTitle(title);
        b.setAuthor(author);

        repository.save(b);
    }

    public void deleteBook(String bookId) {
        repository.delete(bookId);
    }

    public List<Book> searchBooks(String field, String keyword, int page, int size) {
        return repository.search(field, keyword, page, size);
    }

    public int countSearchResults(String field, String keyword) {
        return repository.countSearch(field, keyword);
    }
}
