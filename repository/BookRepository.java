package repository;

import model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookRepository {

    private final String FILE_PATH = "data/books.txt";
    private final FileRepository fileRepository;

    public BookRepository() {
        this.fileRepository = new FileRepository();
    }

    public List<Book> findPage(int page, int size) {

        List<String> rows = fileRepository.readPage(FILE_PATH, page, size);
        List<Book> books = new ArrayList<>();

        for (String row : rows) {
            books.add(map(row));
        }

        return books;
    }

    public int count() {
        return fileRepository.readAll(FILE_PATH).size();
    }

    public List<Book> findAll() {

        List<Book> books = new ArrayList<>();

        for (String row : fileRepository.readAll(FILE_PATH)) {
            books.add(map(row));
        }

        return books;
    }

    public Book findById(String id) {

        String row = fileRepository.readById(FILE_PATH, id);
        return row == null ? null : map(row);
    }

    public void save(Book book) {
        fileRepository.save(FILE_PATH, toRow(book));
    }

    public void delete(String bookId) {
        fileRepository.deleteById(FILE_PATH, bookId);
    }

    // Search support

    public List<Book> search(String field, String keyword, int page, int size) {

        List<Book> matched = filter(field, keyword);
        return paginate(matched, page, size);
    }

    public int countSearch(String field, String keyword) {
        return filter(field, keyword).size();
    }

    private List<Book> filter(String field, String keyword) {

        String key = keyword.toLowerCase();

        return fileRepository.readAll(FILE_PATH).stream()
                .map(this::map)
                .filter(b -> {
                    if (field.equals("TITLE")) return b.getTitle().toLowerCase().contains(key);
                    if (field.equals("AUTHOR")) return b.getAuthor().toLowerCase().contains(key);
                    if (field.equals("ISBN")) return b.getIsbn().toLowerCase().contains(key);
                    return false;
                })
                .collect(Collectors.toList());
    }

    private List<Book> paginate(List<Book> books, int page, int size) {

        int start = (page - 1) * size;
        int end = Math.min(start + size, books.size());

        if (start >= books.size()) return new ArrayList<>();

        return books.subList(start, end);
    }

    private Book map(String row) {

        String[] p = row.split("\\|");

        Book b = new Book();
        b.setId(p[0]);
        b.setIsbn(p[1]);
        b.setTitle(p[2]);
        b.setAuthor(p[3]);

        return b;
    }

    private String toRow(Book b) {
        return b.getId() + "|" + b.getIsbn() + "|" + b.getTitle() + "|" + b.getAuthor();
    }
}
