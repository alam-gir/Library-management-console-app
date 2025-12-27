package repository;

import model.Student;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentRepository {

    private final String FILE_PATH = "data/students.txt";
    private final FileRepository fileRepository;

    public StudentRepository() {
        this.fileRepository = new FileRepository();
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

    public List<Student> search(String field, String keyword, int page, int size) {

        List<Student> matched = filter(field, keyword);
        return paginate(matched, page, size);
    }

    public int countSearch(String field, String keyword) {
        return filter(field, keyword).size();
    }

    private List<Student> filter(String field, String keyword) {

        String key = keyword.toLowerCase();

        return fileRepository.readAll(FILE_PATH).stream()
                .map(this::map)
                .filter(s -> {
                    if (field.equals("NAME"))
                        return s.getName().toLowerCase().contains(key);
                    if (field.equals("STUDENT_ID"))
                        return s.getStudentID().toLowerCase().contains(key);
                    if (field.equals("DEPARTMENT"))
                        return s.getDepartment().toLowerCase().contains(key);
                    return false;
                })
                .collect(Collectors.toList());
    }

    private List<Student> paginate(List<Student> list, int page, int size) {

        int start = (page - 1) * size;
        int end = Math.min(start + size, list.size());

        if (start >= list.size())
            return new ArrayList<>();

        return list.subList(start, end);
    }

    private Student map(String row) {

        String[] p = row.split("\\|");

        Student s = new Student();
        s.setId(p[0]);
        s.setUserId(p[1]);
        s.setStudentID(p[2]);
        s.setDepartment(p[3]);
        s.setEmail(p[4]);

        return s;
    }
}
