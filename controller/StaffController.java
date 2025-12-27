package controller;

import model.Staff;
import service.DashboardService;
import service.NotificationService;
import util.*;

import controller.feature.checkout.ApproveRequestFeature;
import controller.feature.checkout.CheckoutFeature;
import controller.feature.checkout.ReturnFeature;
import controller.feature.notification.StaffNotificationFeature;

public class StaffController {

    private final Staff staff;
    private final NotificationService notificationService;
    private final BookManagementController bookManagementController;
    private final StudentManagementController studentManagementController;
    private final DashboardService dash = new DashboardService();

    public StaffController(Staff staff) {
        this.staff = staff;
        this.notificationService = new NotificationService();
        this.bookManagementController = new BookManagementController();
        this.studentManagementController = new StudentManagementController();
    }

    public void start() {

        while (true) {
            ScreenUtil.clear();

            DisplayHelper.printHeader("Library Dashboard - Welcome " + staff.getName());
            DisplayHelper.emptyLine();

            System.out.println("[BOOKS]");
            System.out.println("  Total Books           : " + dash.totalBooks());
            System.out.println("  Total Copies          : " + dash.totalCopies());
            System.out.println("  Available Copies      : " + dash.availableCopies());
            System.out.println("  Borrowed Copies       : " + dash.borrowedCopies());
            DisplayHelper.emptyLine();

            System.out.println("[USERS]");
            System.out.println("  Total Students        : " + dash.totalStudents());
            DisplayHelper.emptyLine();

            System.out.println("[REQUESTS]");
            System.out.println("  Pending Requests      : " + dash.pendingRequests());
            System.out.println("  Ready for Pickup      : " + dash.approvedWaitingPickup());
            System.out.println("  Overdue Books         : " + dash.overdue());
            DisplayHelper.emptyLine();

            int unreadCountNotification = notificationService.unreadForStaff();
            String notificationMenuTitle = unreadCountNotification > 0
                    ? "Notifications (" + unreadCountNotification + ")"
                    : "Notifications";

            MenuRenderer.show(
                    "Select Option",
                    "Approve Request",
                    "Checkout Book",
                    "Return Book",
                    "Manage Books",
                    "Manage Students",
                    notificationMenuTitle,
                    "Logout");

            int choice = InputHelper.readInt("Choose option", 1, 8);

            switch (choice) {
                case 1:
                    new ApproveRequestFeature().start();
                    break;
                case 2:
                    new CheckoutFeature().start();
                    ;
                    break;
                case 3:
                    new ReturnFeature().start();
                    break;
                case 4:
                    bookManagementController.start();
                    break;
                case 5:
                    studentManagementController.start();
                    break;
                case 6:
                    new StaffNotificationFeature().start();
                    break;
                case 7:
                    return;
            }
        }
    }
}
