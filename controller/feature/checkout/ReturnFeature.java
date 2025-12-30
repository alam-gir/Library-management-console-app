package controller.feature.checkout;

import model.BorrowRequest;
import model.enums.RequestStatus;
import service.BorrowRequestService;
import service.StaffService;
import util.DisplayHelper;
import util.pagination.PaginatedListView;

public class ReturnFeature {

    private final BorrowRequestService reqService = new BorrowRequestService();
    private final StaffService staff = new StaffService();

    public void start() {

        new PaginatedListView<BorrowRequest>()
            .title("📚 Return Borrowed Books")
            .pageSize(6)

            // Only show checked out items
            .fetch((page,size) -> reqService.paged(RequestStatus.CHECKED_OUT,page,size))
            .count(() -> reqService.count(RequestStatus.CHECKED_OUT))

            .columns("ReqID","Copy","Student","BorrowDate","Due Date")
            .map(r -> new String[]{
                    r.getId(),
                    r.getCopyId(),
                    r.getStudentId(),
                    r.getRequestDate().toString(),
                    r.getDueDate()==null ? "-" : r.getDueDate().toString()
            })

            .action("Return Book", id -> {
                boolean ok = staff.returnBook(id);
                DisplayHelper.result(ok,"Returned Successfully","Return Failed");
            })

            .back("Back")
            .run();
    }
}
