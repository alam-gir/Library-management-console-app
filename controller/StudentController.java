package controller;

import model.Student;
import service.NotificationService;
import util.*;
import controller.feature.notification.StudentNotificationFeature;
import controller.student.feature.BookExploreFeature;
import controller.student.feature.BorrowedBooksFeature;
import controller.student.feature.RequestHistoryFeature;

public class StudentController {

    private final Student student;
    private final NotificationService notificationService = new NotificationService();

    public StudentController(Student student) {
        this.student = student;
    }

    public void start() {

        while (true) {

            ScreenUtil.clear();

            int unread = notificationService.unreadForStudent(student.getId());

            DisplayHelper.printHeader(" STUDENT DASHBOARD - " + student.getName());
            DisplayHelper.emptyLine();

            // Dashboard-style summary (optional & clean)
            System.out.println("Welcome Back, " + student.getName());
            System.out.println("   Student ID : " + student.getStudentID());
            System.out.println("   Department : " + student.getDepartment());
            DisplayHelper.emptyLine();

            String[] menu = {
                    "Explore Books", // instead of Search Books
                    "My Borrowed Books",
                    "Request History",
                    unread > 0 ? "Notifications (" + unread + ")" : "Notifications",
                    "Logout"
            };

            MenuRenderer.show("Select an option", menu);

            int choice = InputHelper.readInt("Choose", 1, menu.length);

            switch (choice) {

                case 1 -> new BookExploreFeature(student).start();

                case 2 -> new BorrowedBooksFeature(student).start();

                case 3 -> new RequestHistoryFeature(student).start();

                case 4 -> new StudentNotificationFeature(student.getId()).start();

                case 5 -> {
                    DisplayHelper.info("Logging out...");
                    ScreenUtil.pause();
                    return;
                }
            }
        }
    }
}
