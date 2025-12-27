package controller.feature.studentManagement;

import model.Student;
import service.StudentService;
import util.ScreenUtil;
import util.pagination.PaginatedListView;

public class StudentListFeature {

    private final StudentService service;

    public StudentListFeature(StudentService service){
        this.service = service;
    }

    public void start(){

        new PaginatedListView<Student>()
            .title("Student List")
            .pageSize(6)

            .fetch(service::getStudents)
            .count(service::getTotalStudents)

            .search(service::searchStudents)
            .searchCount(service::countSearch)

            .columns("StudentID","Name","Department")
            .map(s -> new String[]{
                    s.getStudentID(),
                    s.getName(),
                    s.getDepartment()
            })

            .action("View Details", id -> {
                ScreenUtil.clear();
                new StudentDetailsFeature(service).start(id);
            })

            .back("Back")
            .run();
    }
}
