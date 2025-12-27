package controller.student.feature;

import model.BorrowRequest;
import model.Student;
import service.BorrowRequestService;
import util.pagination.PaginatedListView;

public class BorrowedBooksFeature {

    private final BorrowRequestService requestService = new BorrowRequestService();
    private final Student student;

    public BorrowedBooksFeature(Student student){
        this.student = student;
    }

    public void start(){

        new PaginatedListView<BorrowRequest>()
                .title("📘 My Borrowed Books")
                .pageSize(6)

                .fetch((page,size) -> requestService.getBorrowedPaged(student.getId(),page,size))
                .count(() -> requestService.countBorrowed(student.getId()))

                .columns("ReqID","Copy","Date","Due")
                .map(r -> new String[]{
                        r.getId(),
                        r.getCopyId(),
                        r.getRequestDate().toString(),
                        r.getDueDate()==null? "-" : r.getDueDate().toString()
                })

                .back("Back")
                .run();
    }
}
