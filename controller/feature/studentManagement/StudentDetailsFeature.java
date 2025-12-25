package controller.feature.studentManagement;

import model.Student;
import service.StudentService;
import util.*;

public class StudentDetailsFeature {

    private final StudentService studentService;

    public StudentDetailsFeature(StudentService studentService) {
        this.studentService = studentService;
    }

    public void start(String studentId) {

        Student s = studentService.getStudentByStudentId(studentId);

        if (s == null) {
            DisplayHelper.error("Student not found");
            ScreenUtil.pause();
            return;
        }

        while (true) {
            ScreenUtil.clear();
            DisplayHelper.printSection("Student Details");

            System.out.println("Student ID : " + s.getStudentID());
            System.out.println("Name       : " + s.getName());
            System.out.println("Department : " + s.getDepartment());
            System.out.println("Email      : " + s.getEmail());

            DisplayHelper.emptyLine();

            System.out.println("1. View Borrowed Books");
            System.out.println("2. View Borrow Requests");
            System.out.println("3. Back");

            int c = InputHelper.readInt("Choose", 1, 3);

            if (c == 1) {
                new StudentBorrowedBooksFeature().start(s.getId());
            } else if (c == 2) {
                new StudentBorrowRequestsFeature().start(s.getId());
            } else {
                return;
            }
        }
    }
}
