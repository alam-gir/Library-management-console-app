package controller.feature.studentManagement;

import model.Student;
import service.StudentService;
import util.*;

import java.util.List;

public class StudentListFeature {

    private final StudentService studentService;

    private boolean searchMode = false;
    private String searchField;
    private String searchKeyword;

    public StudentListFeature(StudentService studentService) {
        this.studentService = studentService;
    }

    public void start() {

        int page = 1;
        int size = 5;

        while (true) {
            ScreenUtil.clear();

            int total = searchMode
                    ? studentService.countSearch(searchField, searchKeyword)
                    : studentService.getTotalStudents();

            PaginationState state =
                    new PaginationState(page, size, total);

            List<Student> students = searchMode
                    ? studentService.searchStudents(searchField, searchKeyword, page, size)
                    : studentService.getStudents(page, size);

            String title = searchMode
                    ? "Search Results (" + searchField + ": " + searchKeyword + ")"
                    : "Student List";

            DisplayHelper.printSection(title);

            TableRenderer.render(
                    new String[]{"Student ID", "Name", "Department"},
                    students.stream().map(s -> new String[]{
                            s.getStudentID(),
                            s.getName(),
                            s.getDepartment()
                    }).toList()
            );

            PaginationRenderer.render(state);

            int idx = 1;
            Integer prev = null, next = null;

            if (state.hasPrevious()) {
                System.out.println(idx + ". Previous Page");
                prev = idx++;
            }

            if (state.hasNext()) {
                System.out.println(idx + ". Next Page");
                next = idx++;
            }

            System.out.println(idx + ". View Student Details");
            int view = idx++;

            System.out.println(idx + ". Search Students");
            int search = idx++;

            Integer clear = null;
            if (searchMode) {
                System.out.println(idx + ". Clear Search");
                clear = idx++;
            }

            System.out.println(idx + ". Back");
            int back = idx;

            int choice = InputHelper.readInt("Choose", 1, back);

            if (prev != null && choice == prev) { page--; continue; }
            if (next != null && choice == next) { page++; continue; }

            if (choice == view) {
                String id = InputHelper.readString("Student ID");
                new StudentDetailsFeature(studentService).start(id);
                continue;
            }

            if (choice == search) {
                activateSearch();
                page = 1;
                continue;
            }

            if (clear != null && choice == clear) {
                clearSearch();
                page = 1;
                continue;
            }

            if (choice == back) {
                return;
            }
        }
    }

    private void activateSearch() {

        ScreenUtil.clear();
        DisplayHelper.printSection("Search Students");

        System.out.println("1. Name");
        System.out.println("2. Student ID");
        System.out.println("3. Department");

        int c = InputHelper.readInt("Search by", 1, 3);

        searchField = c == 1 ? "NAME" : c == 2 ? "STUDENT_ID" : "DEPARTMENT";
        searchKeyword = InputHelper.readString("Keyword");

        searchMode = true;
    }

    private void clearSearch() {
        searchMode = false;
        searchField = null;
        searchKeyword = null;
    }
}
