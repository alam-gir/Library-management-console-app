package controller.feature.studentManagement;

import service.BorrowRequestService;
import util.*;

import java.util.List;

import model.BorrowedBookView;

public class StudentBorrowedBooksFeature {

    private final BorrowRequestService borrowService;

    public StudentBorrowedBooksFeature() {
        this.borrowService = new BorrowRequestService();
    }

    public void start(String studentId) {

        ScreenUtil.clear();
        DisplayHelper.printSection("Borrowed Books");

        List<BorrowedBookView> books =
                borrowService.getBorrowedBooks(studentId);

        if (books.isEmpty()) {
            DisplayHelper.empty("No active borrowed books");
        } else {
            TableRenderer.render(
                    new String[]{"Copy ID", "Book Title", "Borrow Date", "Status"},
                    books.stream().map(b -> new String[]{
                            b.getCopyId(),
                            b.getBookTitle(),
                            b.getBorrowDate(),
                            b.getStatus()
                    }).toList()
            );
        }
    }
}
