package controller;

import model.Staff;
import service.NotificationService;
import util.*;


import controller.feature.checkout.ApproveRequestFeature;
import controller.feature.checkout.CheckoutFeature;
import controller.feature.checkout.ReturnFeature;

public class StaffController {

    private final Staff staff;
    private final NotificationService notificationService;
    private final BookManagementController bookManagementController;
    private final StudentManagementController studentManagementController;

    public StaffController(Staff staff) {
        this.staff = staff;
        this.notificationService = new NotificationService();
        this.bookManagementController = new BookManagementController();
        this.studentManagementController = new StudentManagementController();
    }

    public void start() {

        while (true) {
            ScreenUtil.clear();
            DisplayHelper.printHeader("STAFF DASHBOARD");
            DisplayHelper.info("Welcome, " + staff.getName());
            DisplayHelper.emptyLine();

            MenuRenderer.show(
                    "Select Option",
                    "Approve Request",
                    "Checkout Book",
                    "Return Book",
                    "Manage Books",
                    "Manage Students",
                    "Notifications",
                    "Logout");

            int choice = InputHelper.readInt("Choose option", 1, 8);

            switch (choice) {
                case 1:
                    new ApproveRequestFeature().start();
                    break;
                case 2:
                    new CheckoutFeature().start();;
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
                    viewNotifications();
                    break;
                case 7:
                    return;
            }
        }
    }

    private void viewNotifications() {

        ScreenUtil.clear();
        DisplayHelper.printSection("Notifications");

        var notifications = notificationService.getUserNotifications(staff.getId());

        if (notifications.isEmpty()) {
            DisplayHelper.empty("No notifications");
        } else {
            TableRenderer.render(
                    new String[] { "Message" },
                    notifications.stream()
                            .map(n -> new String[] { n.getMessage() })
                            .toList());
        }

        ScreenUtil.pause();
    }
}
