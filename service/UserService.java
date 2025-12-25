package service;

import model.User;
import model.Student;
import model.Staff;
import model.enums.Role;
import repository.UserRepository;
import repository.StudentRepository;
import repository.StaffRepository;

public class UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final StaffRepository staffRepository;

    public UserService() {
        this.userRepository = new UserRepository();
        this.studentRepository = new StudentRepository();
        this.staffRepository = new StaffRepository();
    }

    // Login user using name and password
    public User login(String name, String password) {

        User user = userRepository.authenticate(name, password);

        if (user == null) {
            return null;
        }

        if (user.getRole() == Role.STUDENT) {
            return loadStudent(user);
        }

        if (user.getRole() == Role.STAFF) {
            return loadStaff(user);
        }

        return null;
    }

    // Load full student object by combining user and student data
    private Student loadStudent(User user) {

        Student studentData = studentRepository.findById(user.getId());

        if (studentData == null) {
            return null;
        }

        Student student = new Student();
        student.setId(user.getId());
        student.setName(user.getName());
        student.setPassword(user.getPassword());
        student.setRole(Role.STUDENT);

        student.setStudentID(studentData.getStudentID());
        student.setDepartment(studentData.getDepartment());

        return student;
    }

    // Load full staff object by combining user and staff data
    private Staff loadStaff(User user) {

        Staff staffData = staffRepository.findByUserId(user.getId());

        if (staffData == null) {
            return null;
        }

        Staff staff = new Staff();
        staff.setId(user.getId());
        staff.setName(user.getName());
        staff.setPassword(user.getPassword());
        staff.setRole(Role.STAFF);

        staff.setEmployeeID(staffData.getEmployeeID());
        staff.setDesignation(staffData.getDesignation());
        staff.setDepartment(staffData.getDepartment());

        return staff;
    }
}
