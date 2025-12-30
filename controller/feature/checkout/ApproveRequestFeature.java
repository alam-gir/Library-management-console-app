package controller.feature.checkout;

import model.BorrowRequest;
import model.enums.RequestStatus;
import service.BorrowRequestService;
import service.StaffService;
import util.DisplayHelper;
import util.pagination.PaginatedListView;

public class ApproveRequestFeature {

    private final BorrowRequestService reqService = new BorrowRequestService();
    private final StaffService staff = new StaffService();

    public void start() {

        new PaginatedListView<BorrowRequest>()
            .title("📄 Approve Borrow Requests")
            .pageSize(6)

            // fetch only pending
            .fetch((page,size) -> reqService.paged(RequestStatus.PENDING,page,size))
            .count(() -> reqService.count(RequestStatus.PENDING))

            .columns("ReqID","Student Id","Copy Id","Date")
            .map(r -> new String[]{
                    r.getId(),
                    r.getStudentId(),
                    r.getCopyId(),
                    r.getRequestDate().toString()
            })

            .action("Approve", id -> {
                boolean ok = staff.approve(id);
                DisplayHelper.result(ok, "Approved Successfully!", "Approval Failed");
            })

            .action("Reject", id -> {
                boolean ok = staff.reject(id);
                DisplayHelper.result(ok, "Request Rejected", "Reject Failed");
            })

            .back("Back")
            .run();
    }
}
