package service;

import model.BookCopy;
import model.enums.BookStatus;
import repository.BookCopyRepository;
import util.IdGenerator;
import java.util.List;

public class BookCopyService {

    private final BookCopyRepository repository;

    public BookCopyService() {
        this.repository = new BookCopyRepository();
    }

    public List<BookCopy> getCopies(String bookId) {
        return repository.findByBookId(bookId);
    }

    public int availableCount(String bookId) {
        return repository.countAvailable(bookId);
    }

    public void addCopy(String bookId, String barcode) {

        BookCopy c = new BookCopy();
        c.setId(IdGenerator.generate("BOOK_COPY"));
        c.setBookId(bookId);
        c.setBarcode(barcode);
        c.setStatus(BookStatus.AVAILABLE);

        repository.save(c);
    }

    public void removeCopy(String copyId) {
        repository.delete(copyId);
    }
}
