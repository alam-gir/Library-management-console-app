package service;

import model.Book;
import model.enums.BookStatus;
import repository.BookCopyRepository;
import repository.BookRepository;
import util.IdGenerator;
import util.InputHelper;

import java.util.List;

public class BookService {

    private final BookRepository repository = new BookRepository();
    private final BookCopyRepository copyRepository = new BookCopyRepository();

    public List<Book> getBooks(int page, int size) {
        return repository.findPage(page, size);
    }

    public int getTotalBooks() {
        return repository.count();
    }

    public Book getBook(String bookId) {
        return repository.findById(bookId);
    }

    public void addBook() {
        String title = InputHelper.readString("Title");
        String author = InputHelper.readString("Author");
        String isbn = InputHelper.readString("ISBN");

        Book b = new Book();
        b.setId(IdGenerator.generate("BOOK"));
        b.setIsbn(isbn);
        b.setTitle(title);
        b.setAuthor(author);

        repository.save(b);
        System.out.println("Book Added Successfully!");
    }

    public void deleteBook(String bookId) {
        repository.delete(bookId);
    }

    public List<Book> searchBooks(String keyword, int page, int size) {

        final String key = keyword.toLowerCase();

        List<Book> filtered = repository.findAll()
                .stream()
                .filter(b -> b.getTitle().toLowerCase().contains(key) ||
                        b.getAuthor().toLowerCase().contains(key) ||
                        b.getIsbn().toLowerCase().contains(key))
                .toList();

        int start = (page - 1) * size;
        return start >= filtered.size() ? List.of() : filtered.subList(start, Math.min(start + size, filtered.size()));
    }

    public int countSearchResults(String keyword) {

        final String key = keyword.toLowerCase(); // <-- resolved

        return (int) repository.findAll()
                .stream()
                .filter(b -> b.getTitle().toLowerCase().contains(key) ||
                        b.getAuthor().toLowerCase().contains(key) ||
                        b.getIsbn().toLowerCase().contains(key))
                .count();
    }

    public int countSearchResults(String field, String keyword) {
        return repository.countSearch(field, keyword);
    }

    // for student panel
    public List<Book> getStudentBooks(int page, int size) {
        return repository.findAll().stream()
                .filter(b -> copyRepository.countByBookAndStatus(b.getId(), BookStatus.AVAILABLE) > 0)
                .skip((page - 1) * size)
                .limit(size)
                .toList();
    }

    public int countStudentBooks() {
        return (int) repository.findAll().stream()
                .filter(b -> copyRepository.countByBookAndStatus(b.getId(), BookStatus.AVAILABLE) > 0)
                .count();
    }

    public List<Book> searchStudentBooks(String keyword, int page, int size) {
        return repository.findAll().stream()
                .filter(b -> (b.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                        b.getAuthor().toLowerCase().contains(keyword.toLowerCase()) ||
                        b.getIsbn().toLowerCase().contains(keyword.toLowerCase()))
                        &&
                        copyRepository.countByBookAndStatus(b.getId(), BookStatus.AVAILABLE) > 0)
                .skip((page - 1) * size)
                .limit(size)
                .toList();
    }

    // return only books that have available copies for student view
    public int getStudentBookCount() {
        return (int) repository.findAll().stream()
                .filter(b -> copyRepository.countByBookAndStatus(b.getId(), BookStatus.AVAILABLE) > 0)
                .count();
    }

    public List<Book> getBooksForStudents(int page, int size) {
        return repository.findAll().stream()
                .filter(b -> copyRepository.countByBookAndStatus(b.getId(), BookStatus.AVAILABLE) > 0)
                .skip((page - 1) * size)
                .limit(size)
                .toList();
    }

    // search books but only if at least 1 available copy
    public List<Book> searchBooksForStudents(String keyword, int page, int size) {
        return repository.findAll().stream()
                .filter(b -> (b.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                        b.getAuthor().toLowerCase().contains(keyword.toLowerCase()) ||
                        b.getIsbn().toLowerCase().contains(keyword.toLowerCase()))
                        &&
                        copyRepository.countByBookAndStatus(b.getId(), BookStatus.AVAILABLE) > 0)
                .skip((page - 1) * size)
                .limit(size)
                .toList();
    }

    public int countSearchStudentBooks(String keyword) {
        return (int) repository.findAll().stream()
                .filter(b -> (b.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                        b.getAuthor().toLowerCase().contains(keyword.toLowerCase()) ||
                        b.getIsbn().toLowerCase().contains(keyword.toLowerCase()))
                        &&
                        copyRepository.countByBookAndStatus(b.getId(), BookStatus.AVAILABLE) > 0)
                .count();
    }
}
