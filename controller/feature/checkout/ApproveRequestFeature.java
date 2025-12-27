package controller.feature.checkout;

import model.BorrowRequest;
import model.enums.RequestStatus;
import service.BorrowRequestService;
import service.StaffService;
import util.*;
import util.pagination.MenuRenderer;
import util.pagination.PaginationRenderer;
import util.pagination.PaginationState;

import java.util.List;

public class ApproveRequestFeature {

    private final BorrowRequestService reqService = new BorrowRequestService();
    private final StaffService staff = new StaffService();

    public void start() {

        int page = 1, size = 5;

        while (true) {

            ScreenUtil.clear();
            int total = reqService.count(RequestStatus.PENDING);
            List<BorrowRequest> list = reqService.paged(RequestStatus.PENDING, page, size);

            DisplayHelper.printSection("Approve Borrow Requests");
            PaginationRenderer.render(new PaginationState(page, size, total));

            TableRenderer.render(new String[] { "ReqID", "Student", "Copy", "Date" },
                    list.stream().map(r -> new String[] {
                            r.getId(), r.getStudentId(), r.getCopyId(), r.getRequestDate().toString()
                    }).toList());

            int op = MenuRenderer.dynamic("Options",
                    page > 1 ? "Prev Page" : null,
                    page * size < total ? "Next Page" : null,
                    "Approve Request",
                    "Reject Request",
                    "Back");

            int index = 1;

            if (page > 1 && op == index++) {
                page--;
                continue;
            }
            if (page * size < total && op == index++) {
                page++;
                continue;
            }

            if (op == index++) {
                String id = InputHelper.readString("Enter Request ID");
                DisplayHelper.result(staff.approve(id), "Approved", "Failed");
                ScreenUtil.pause();
                continue;
            }

            if (op == index++) {
                String id = InputHelper.readString("Enter Request ID");
                DisplayHelper.result(staff.reject(id), "Rejected", "Failed");
                ScreenUtil.pause();
                continue;
            }

            return;
        }
    }

}
