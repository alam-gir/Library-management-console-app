package repository;

import model.BookCopy;
import model.enums.BookStatus;

import java.util.ArrayList;
import java.util.List;

public class BookCopyRepository {

    private final String FILE_PATH = "data/book_copies.txt";
    private final FileRepository fileRepository;

    public BookCopyRepository() {
        this.fileRepository = new FileRepository();
    }

    // Get all copies of a book
    public List<BookCopy> findByBookId(String bookId) {
        List<BookCopy> result = new ArrayList<>();
        for (String row : fileRepository.readAll(FILE_PATH)) {
            BookCopy c = map(row);
            if (c.getBookId().equals(bookId)) {
                result.add(c);
            }
        }
        return result;
    }

    // Count available copies only
    public int countAvailable(String bookId) {
        int count = 0;
        for (BookCopy c : findByBookId(bookId)) {
            if (c.getStatus() == BookStatus.AVAILABLE)
                count++;
        }
        return count;
    }

    // count all copies available in library
    public int countAll() {
        return fileRepository.readAll(FILE_PATH).size();
    }

    // count copies by BookStatus
    public int countByStatus(BookStatus status) {
        return (int) fileRepository.readAll(FILE_PATH).stream()
                .map(this::map)
                .filter(c -> c.getStatus() == status)
                .count();
    }

    // ========================
    // NEW -> find by ID
    // ========================
    public BookCopy findById(String copyId) {
        String row = fileRepository.readById(FILE_PATH, copyId);
        return row == null ? null : map(row);
    }

    // Save new copy
    public void save(BookCopy copy) {
        fileRepository.save(FILE_PATH, toRow(copy));
    }

    // Delete copy
    public void delete(String copyId) {
        fileRepository.deleteById(FILE_PATH, copyId);
    }

    // ========================
    // NEW -> update an existing record
    // ========================
    public void update(BookCopy copy) {
        fileRepository.updateById(FILE_PATH, copy.getId(), toRow(copy));
    }

    // ------------------------
    // Internal mapping helpers
    // ------------------------
    private BookCopy map(String row) {
        String[] p = row.split("\\|");

        BookCopy c = new BookCopy();
        c.setId(p[0]);
        c.setBookId(p[1]);
        c.setBarcode(p[2]);
        c.setStatus(BookStatus.valueOf(p[3]));

        return c;
    }

    private String toRow(BookCopy c) {
        return c.getId() + "|" + c.getBookId() + "|" +
                c.getBarcode() + "|" + c.getStatus();
    }
}
