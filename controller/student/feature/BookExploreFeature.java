package controller.student.feature;

import model.Book;
import model.Student;
import service.BookService;
import service.BorrowRequestService;
import util.*;
import util.pagination.PaginatedListView;

public class BookExploreFeature {

    private final BookService bookService = new BookService();
    private final BorrowRequestService reqService = new BorrowRequestService();
    private final Student student;

    public BookExploreFeature(Student student){
        this.student = student;
    }

    public void start(){

        new PaginatedListView<Book>()
                .title("Explore Books")
                .pageSize(6)

                // normal fetch
                .fetch(bookService::getStudentBooks)
                .count(bookService::countStudentBooks)

                // search mode
                .search(bookService::searchStudentBooks)
                .searchCount(bookService::countSearchStudentBooks)

                .columns("ID","Title","Author")
                .map(b -> new String[]{
                        b.getId(),
                        b.getTitle(),
                        b.getAuthor()
                })

                // main student action
                .action("Request Borrow", id -> {
                    boolean ok = reqService.requestBorrow(student.getId(), id);
                    DisplayHelper.result(ok,"Request Sent","Failed or unavailable");
                })

                .back("Back")
                .run();
    }
}
