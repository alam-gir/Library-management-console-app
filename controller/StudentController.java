// controller/StudentController.java
package controller;

import model.Student;
import service.BookService;
import service.BorrowRequestService;
import service.NotificationService;
import service.StudentService;
import util.DisplayHelper;
import util.InputHelper;
import util.MenuRenderer;
import util.ScreenUtil;


import controller.feature.notification.StudentNotificationFeature;

public class StudentController {

    private final Student student;
    private final BookService bookService;
    private final BorrowRequestService requestService;
    private final StudentService studentService;
    private final NotificationService notificationService;

    public StudentController(Student student) {
        this.student = student;
        this.bookService = new BookService();
        this.requestService = new BorrowRequestService();
        this.studentService = new StudentService();
        this.notificationService = new NotificationService();
    }

    public void start() {

        while (true) {
            ScreenUtil.clear();
            DisplayHelper.printHeader("Student Dashboard");
            DisplayHelper.info("Name: " + student.getName());
            DisplayHelper.info("Department: " + student.getDepartment());


            MenuRenderer.show(
                    "Student Menu",
                    "View Books",
                    "Request Book",
                    "My Requests",
                    "Notifications",
                    "Logout"
            );

            int choice = InputHelper.readInt("Choose option", 1, 5);

            switch (choice) {
                case 1:
                    // viewBooks();
                    break;
                case 2:
                    // requestBook();
                    break;
                case 3:
                    // viewRequests();
                    break;
                case 4:
                    new StudentNotificationFeature(student.getId()).start();
                    break;
                case 5:
                    return;
            }
        }
    }
}
