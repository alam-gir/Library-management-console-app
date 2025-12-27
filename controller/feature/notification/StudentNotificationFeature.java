package controller.feature.notification;

import model.Notification;
import service.NotificationService;
import util.DisplayHelper;
import util.ScreenUtil;
import util.pagination.PaginatedListView;

public class StudentNotificationFeature {

    private final NotificationService service = new NotificationService();
    private final String studentId;

    public StudentNotificationFeature(String studentId){
        this.studentId = studentId;
    }

    public void start(){

        new PaginatedListView<Notification>()
                .title("Your Notifications")
                .pageSize(6)

                // Pagination data sources
                .fetch((page,size) -> service.getStudentPaged(studentId,page,size))
                .count(() -> service.studentCount(studentId))

                // Table format
                .columns("ID","Message","Read","Date")
                .map(n -> new String[]{
                        n.getId(),
                        n.getMessage(),
                        n.isRead() ? "✔" : "✘",
                        n.getCreatedAt().toString()
                })

                // Buttons
                .action("Mark All as Read", () -> {
                    service.markAllStudent(studentId);
                    DisplayHelper.success("All notifications marked as read!");
                    ScreenUtil.pause();
                })
                .back("Back")
                .run();
    }
}
