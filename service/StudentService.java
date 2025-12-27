package service;

import model.Student;
import repository.StudentRepository;

import java.util.List;

public class StudentService {

    private final StudentRepository repository;

    public StudentService() {
        this.repository = new StudentRepository();
    }

    public List<Student> getStudents(int page, int size) {
        return repository.findPage(page, size);
    }

    public int getTotalStudents() {
        return repository.count();
    }

    public Student getStudent(String id) {
        return repository.findById(id);
    }

    public Student getStudentByStudentId(String studentId) {
        return repository.findByStudentID(studentId);
    }

    public int countSearch(String keyword) {
        return repository.countSearch(keyword);
    }

    public List<Student> searchStudents(String keyword, int page, int size) {
        return repository.search(keyword, page, size);
    }
}
