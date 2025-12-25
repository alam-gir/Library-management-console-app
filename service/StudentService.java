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

    public List<Student> searchStudents(String field, String keyword, int page, int size) {
        return repository.search(field, keyword, page, size);
    }

    public int countSearch(String field, String keyword) {
        return repository.countSearch(field, keyword);
    }
}
