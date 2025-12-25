// controller/StudentController.java
package controller;

import model.Student;
import model.BorrowRequest;
import service.BookService;
import service.BorrowRequestService;
import service.NotificationService;
import service.StudentService;
import util.DisplayHelper;
import util.InputHelper;
import util.MenuRenderer;
import util.ScreenUtil;

import java.util.List;

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

            int unread = notificationService
                    .getUserNotifications(student.getId())
                    .stream()
                    .filter(n -> !n.isRead())
                    .toArray().length;

            DisplayHelper.info("Unread Notifications: " + unread);
            System.out.println();

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
                    viewBooks();
                    break;
                case 2:
                    requestBook();
                    break;
                case 3:
                    viewRequests();
                    break;
                case 4:
                    viewNotifications();
                    break;
                case 5:
                    return;
            }
        }
    }

    private void viewBooks() {

        ScreenUtil.clear();
        DisplayHelper.printSection("Books");

        bookService.getBooks(1, 20).forEach(book ->
                System.out.println(
                        book.getId() + " | " +
                        book.getTitle() + " | " +
                        book.getAuthor()
                )
        );

        ScreenUtil.pause();
    }

    private void requestBook() {

        // ScreenUtil.clear();
        // DisplayHelper.printSection("Request Book");

        // String bookId = InputHelper.readString("Enter Book ID");

        // boolean success = requestService.createRequest(student.getId(), bookId);

        // if (success) {
        //     DisplayHelper.success("Book request submitted");
        // } else {
        //     DisplayHelper.error("No available copy found");
        // }

        // ScreenUtil.pause();
    }

    private void viewRequests() {

        // ScreenUtil.clear();
        // DisplayHelper.printSection("My Requests");

        // List<BorrowRequest> requests =
        //         studentService.getMyRequests(student.getId());

        // if (requests.isEmpty()) {
        //     DisplayHelper.empty("No requests found");
        // } else {
        //     for (BorrowRequest r : requests) {
        //         System.out.println(
        //                 r.getId() + " | " +
        //                 r.getCopyId() + " | " +
        //                 r.getStatus()
        //         );
        //     }
        // }

        ScreenUtil.pause();
    }

    private void viewNotifications() {

        ScreenUtil.clear();
        DisplayHelper.printSection("Notifications");

        notificationService.getUserNotifications(student.getId())
                .forEach(n -> System.out.println("- " + n.getMessage()));

        ScreenUtil.pause();
    }
}
