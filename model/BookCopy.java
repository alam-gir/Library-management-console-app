package model;

import model.enums.BookStatus;

public class BookCopy {

    private String id;
    private String bookId;
    private String barcode;
    private BookStatus status;

    public BookCopy() {}

    public BookCopy(String id, String bookId, String barcode, BookStatus status) {
        this.id = id;
        this.bookId = bookId;
        this.barcode = barcode;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }
}
