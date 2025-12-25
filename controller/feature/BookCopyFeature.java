package controller.feature;

import model.BookCopy;
import service.BookCopyService;
import util.*;

import java.util.List;

public class BookCopyFeature {

    private final BookCopyService service;

    public BookCopyFeature(BookCopyService service) {
        this.service = service;
    }

    public void start(String bookId) {

        while (true) {
            ScreenUtil.clear();

            List<BookCopy> copies = service.getCopies(bookId);

            TableRenderer.render(
                    new String[]{"Copy ID", "Barcode", "Status"},
                    copies.stream().map(c -> new String[]{
                            c.getId(), c.getBarcode(), c.getStatus().name()
                    }).toList()
            );

            System.out.println("1. Add Copy");
            System.out.println("2. Remove Copy");
            System.out.println("3. Back");

            int c = InputHelper.readInt("Choose", 1, 3);

            if (c == 1) {
                String barcode = InputHelper.readString("Barcode");
                service.addCopy(bookId, barcode);
            } else if (c == 2) {
                String id = InputHelper.readString("Copy ID");
                service.removeCopy(id);
            } else {
                return;
            }
        }
    }
}
