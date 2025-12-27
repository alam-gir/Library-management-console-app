package controller.student.feature;

import model.BorrowRequest;
import model.Student;
import service.BorrowRequestService;
import util.pagination.PaginatedListView;

public class RequestHistoryFeature {

    private final BorrowRequestService requestService = new BorrowRequestService();
    private final Student student;

    public RequestHistoryFeature(Student s){
        this.student=s;
    }

    public void start(){

        new PaginatedListView<BorrowRequest>()
                .title("📄 Borrow Request History")
                .pageSize(6)

                .fetch((page,size) -> requestService.getAllRequestsPaged(student.getId(),page,size))
                .count(() -> requestService.countAllRequests(student.getId()))

                .columns("ID","Copy","Status","Date")
                .map(r -> new String[]{
                        r.getId(),
                        r.getCopyId(),
                        r.getStatus().name(),
                        r.getRequestDate().toString()
                })

                .back("Back")
                .run();
    }
}
