package model;

import model.enums.Role;

public class Student extends User {

    private String studentID;
    private String department;
    private String email;

    public Student() {
        this.role = Role.STUDENT;
    }

    public Student(String id, String name, String password,
        String studentID, String department) {
        super(id, name, password, Role.STUDENT);
        this.studentID = studentID;
        this.department = department;
    }


    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
