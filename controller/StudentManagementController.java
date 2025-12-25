package controller;

import controller.feature.studentManagement.StudentListFeature;
import service.StudentService;

public class StudentManagementController {

    private final StudentService studentService;

    public StudentManagementController() {
        this.studentService = new StudentService();
    }

    public void start() {
        new StudentListFeature(studentService).start();
    }
}
