package model;

import model.enums.Role;

public class Staff extends User {

    private String employeeID;
    private String designation;
    private String department;

    public Staff() {
        this.role = Role.STAFF;
    }

    public Staff(String id, String name, String password,
        String employeeID, String designation, String department) {
        super(id, name, password, Role.STAFF);
        this.employeeID = employeeID;
        this.designation = designation;
        this.department = department;
    }


    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
