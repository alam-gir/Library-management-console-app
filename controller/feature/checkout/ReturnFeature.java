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

public class ReturnFeature {

    private final BorrowRequestService reqService = new BorrowRequestService();
    private final StaffService staff = new StaffService();

    public void start(){

        int page = 1, size = 5;

        while(true){

            ScreenUtil.clear();
            int total = reqService.count(RequestStatus.CHECKED_OUT);
            List<BorrowRequest> list = reqService.paged(RequestStatus.CHECKED_OUT,page,size);

            DisplayHelper.printSection("Return Books");
            PaginationRenderer.render(new PaginationState(page,size,total));

            TableRenderer.render(
                new String[]{"ReqID","Copy","Student","BorrowDate","Due"},
                list.stream().map(r -> new String[]{
                    r.getId(),r.getCopyId(),r.getStudentId(),
                    r.getRequestDate().toString(),
                    r.getDueDate()==null? "-" : r.getDueDate().toString()
                }).toList()
            );

            int op = MenuRenderer.dynamic("Options",
                    page>1 ? "Prev Page" : null,
                    page*size<total ? "Next Page" : null,
                    "Return Book",
                    "Back"
            );

            int index = 1;

            if(page > 1 && op == index++) { page--; continue; }
            if(page*size < total && op == index++) { page++; continue; }

            if(op == index++){
                String copyId = InputHelper.readString("Enter Copy ID");
                DisplayHelper.result(staff.returnBook(copyId), "Book Returned", "Failed");
                ScreenUtil.pause();
                continue;
            }

            return; // Back
        }
    }
}
