package controller;

import model.Staff;
import model.BorrowRequest;
import service.BookService;
import service.NotificationService;
import service.StaffService;
import util.*;

import java.util.List;

public class StaffController {

    private final Staff staff;
    private final StaffService staffService;
    private final NotificationService notificationService;
    private final BookManagementController bookManagementController;
    private final StudentManagementController studentManagementController;

    public StaffController(Staff staff) {
        this.staff = staff;
        this.staffService = new StaffService();
        this.notificationService = new NotificationService();
        this.bookManagementController =
                new BookManagementController(new BookService());
        this.studentManagementController =
                new StudentManagementController();
    }

    public void start() {

        while (true) {
            ScreenUtil.clear();
            DisplayHelper.printHeader("STAFF DASHBOARD");
            DisplayHelper.info("Welcome, " + staff.getName());
            DisplayHelper.emptyLine();

            MenuRenderer.show(
                    "Select Option",
                    "View Pending Requests",
                    "Approve Request",
                    "Checkout Book",
                    "Return Book",
                    "Manage Books",
                    "Manage Students",
                    "Notifications",
                    "Logout"
            );

            int choice = InputHelper.readInt("Choose option", 1, 8);

            switch (choice) {
                case 1:
                    viewPendingRequests();
                    break;
                case 2:
                    approveRequest();
                    break;
                case 3:
                    checkoutBook();
                    break;
                case 4:
                    returnBook();
                    break;
                case 5:
                    bookManagementController.start();
                    break;
                case 6:
                    studentManagementController.start();
                    break;
                case 7:
                    viewNotifications();
                    break;
                case 8:
                    return;
            }
        }
    }

    private void viewPendingRequests() {

        ScreenUtil.clear();
        DisplayHelper.printSection("Pending Requests");

        List<BorrowRequest> requests = staffService.viewPendingRequests();

        if (requests.isEmpty()) {
            DisplayHelper.empty("No pending requests");
        } else {
            TableRenderer.render(
                    new String[]{"Request ID", "Student ID", "Copy ID"},
                    requests.stream().map(r -> new String[]{
                            r.getId(),
                            r.getStudentId(),
                            r.getCopyId()
                    }).toList()
            );
        }

        ScreenUtil.pause();
    }

    private void approveRequest() {

        ScreenUtil.clear();
        DisplayHelper.printSection("Approve Request");

        String requestId = InputHelper.readString("Request ID");
        // staffService.approveRequest(requestId);

        DisplayHelper.success("Request approved");
        ScreenUtil.pause();
    }

    private void checkoutBook() {

        ScreenUtil.clear();
        DisplayHelper.printSection("Checkout Book");

        String copyId = InputHelper.readString("Copy ID");
        // staffService.checkoutBook(copyId);

        DisplayHelper.success("Book checked out");
        ScreenUtil.pause();
    }

    private void returnBook() {

        ScreenUtil.clear();
        DisplayHelper.printSection("Return Book");

        String copyId = InputHelper.readString("Copy ID");
        // staffService.returnBook(copyId);

        DisplayHelper.success("Book returned");
        ScreenUtil.pause();
    }

    private void viewNotifications() {

        ScreenUtil.clear();
        DisplayHelper.printSection("Notifications");

        var notifications =
                notificationService.getUserNotifications(staff.getId());

        if (notifications.isEmpty()) {
            DisplayHelper.empty("No notifications");
        } else {
            TableRenderer.render(
                    new String[]{"Message"},
                    notifications.stream()
                            .map(n -> new String[]{n.getMessage()})
                            .toList()
            );
        }

        ScreenUtil.pause();
    }
}
