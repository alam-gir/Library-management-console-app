package repository;

import model.Student;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class StudentRepository {

    private final String FILE_PATH = "data/students.txt";
    private final FileRepository fileRepository = new FileRepository();
    private final UserRepository userRepository = new UserRepository();

    public List<Student> findAll() {
        return fileRepository.readAll(FILE_PATH).stream()
                .map(this::map)
                .toList();
    }

    public List<Student> findPage(int page, int size) {

        List<String> rows = fileRepository.readPage(FILE_PATH, page, size);
        List<Student> students = new ArrayList<>();

        for (String row : rows) {
            students.add(map(row));
        }

        return students;
    }

    public int count() {
        return fileRepository.readAll(FILE_PATH).size();
    }

    public Student findById(String id) {

        String row = fileRepository.readById(FILE_PATH, id);
        return row == null ? null : map(row);
    }

    public Student findByStudentID(String studentID) {

        for (String row : fileRepository.readAll(FILE_PATH)) {
            Student s = map(row);
            if (s.getStudentID().equalsIgnoreCase(studentID)) {
                return s;
            }
        }
        return null;
    }

    public Student findByUserId(String userId) {

        for (String row : fileRepository.readAll(FILE_PATH)) {
            Student s = map(row);
            if (s.getUserId().equalsIgnoreCase(userId)) {
                return s;
            }
        }
        return null;
    }

    public List<Student> search(String keyword, int page, int size) {
        return findAll().stream()
                .filter(s -> s.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                        s.getStudentID().toLowerCase().contains(keyword.toLowerCase()) ||
                        (s.getDepartment() != null && s.getDepartment().toLowerCase().contains(keyword.toLowerCase())))
                .skip((page - 1) * size)
                .limit(size)
                .toList();
    }

    public int countSearch(String keyword) {
        return (int) findAll().stream()
                .filter(s -> s.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                        s.getStudentID().toLowerCase().contains(keyword.toLowerCase()) ||
                        (s.getDepartment() != null && s.getDepartment().toLowerCase().contains(keyword.toLowerCase())))
                .count();
    }

    private Student map(String row) {

        String[] p = row.split("\\|");
        User user = userRepository.findById(p[1]);

        Student s = new Student();
        s.setId(p[0]);
        s.setUserId(p[1]);
        s.setName(user.getName());
        s.setPassword(user.getPassword());
        s.setStudentID(p[2]);
        s.setDepartment(p[3]);
        s.setEmail(p[4]);

        return s;
    }
}
