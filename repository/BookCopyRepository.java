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

    public List<BookCopy> findByBookId(String bookId) {

        List<BookCopy> copies = new ArrayList<>();

        for (String row : fileRepository.readAll(FILE_PATH)) {
            BookCopy c = map(row);
            if (c.getBookId().equals(bookId)) {
                copies.add(c);
            }
        }

        return copies;
    }

    public BookCopy findById(String copyId) {

        String row = fileRepository.readById(FILE_PATH, copyId);

        if (row == null) {
            return null;
        }

        return map(row);
    }

    public int countAvailable(String bookId) {

        int count = 0;

        for (BookCopy c : findByBookId(bookId)) {
            if (c.getStatus() == BookStatus.AVAILABLE) {
                count++;
            }
        }

        return count;
    }

    public void save(BookCopy copy) {
        fileRepository.save(FILE_PATH, toRow(copy));
    }

    public void delete(String copyId) {
        fileRepository.deleteById(FILE_PATH, copyId);
    }

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
        return c.getId() + "|" + c.getBookId() + "|" + c.getBarcode() + "|" + c.getStatus();
    }
}
