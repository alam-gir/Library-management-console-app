package controller;

import model.Staff;
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

            int unreadCountNotification = notificationService.unreadForStaff();
            String notificationMenuTitle = unreadCountNotification > 0 ?
                    "Notifications (" + unreadCountNotification + ")" :
                    "Notifications";

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
                    new StaffNotificationFeature().start();
                    break;
                case 7:
                    return;
            }
        }
    }
}
