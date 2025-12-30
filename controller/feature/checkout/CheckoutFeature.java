package controller.feature.checkout;

import model.BorrowRequest;
import model.enums.RequestStatus;
import service.BorrowRequestService;
import service.StaffService;
import util.DisplayHelper;
import util.pagination.PaginatedListView;

public class CheckoutFeature {

    private final BorrowRequestService reqService = new BorrowRequestService();
    private final StaffService staff = new StaffService();

    public void start() {

        new PaginatedListView<BorrowRequest>()
            .title("📦 Checkout Requests (Ready for Pickup)")
            .pageSize(6)

            // Fetch approved requests only
            .fetch((page,size) -> reqService.paged(RequestStatus.APPROVED,page,size))
            .count(() -> reqService.count(RequestStatus.APPROVED))

            .columns("ReqID","Student","Copy","Date")
            .map(r -> new String[]{
                    r.getId(),
                    r.getStudentId(),
                    r.getCopyId(),
                    r.getRequestDate().toString()
            })

            .action("Checkout", id -> {
                boolean ok = staff.checkout(id);
                DisplayHelper.result(ok,"Checkout Completed","Checkout Failed");
            })

            .back("Back")
            .run();
    }
}
