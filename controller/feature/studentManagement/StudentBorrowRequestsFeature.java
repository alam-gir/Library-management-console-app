package controller.feature.studentManagement;

import model.BorrowRequest;
import service.BorrowRequestService;
import util.*;

import java.util.List;

public class StudentBorrowRequestsFeature {

    private final BorrowRequestService borrowService;

    public StudentBorrowRequestsFeature() {
        this.borrowService = new BorrowRequestService();
    }

    public void start(String studentId) {

        ScreenUtil.clear();
        DisplayHelper.printSection("Borrow Requests");

        List<BorrowRequest> requests =
                borrowService.getRequestsByStudent(studentId);

        if (requests.isEmpty()) {
            DisplayHelper.empty("No borrow requests found");
        } else {
            TableRenderer.render(
                    new String[]{"Request ID", "Copy ID", "Status", "Request Date"},
                    requests.stream().map(r -> new String[]{
                            r.getId(),
                            r.getCopyId(),
                            r.getStatus().name(),
                            r.getRequestDate().toString()
                    }).toList()
            );
        }

        ScreenUtil.pause();
    }
}
