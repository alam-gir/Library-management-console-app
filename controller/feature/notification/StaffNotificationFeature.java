package controller.feature.notification;

import model.Notification;
import service.NotificationService;
import util.DisplayHelper;
import util.ScreenUtil;
import util.pagination.PaginatedListView;

public class StaffNotificationFeature {

    private final NotificationService service = new NotificationService();

    public void start(){

        new PaginatedListView<Notification>()
                .title("Notifications")
                .pageSize(6)

                // Pagination data sources
                .fetch((page,size) -> service.getStaffPaged(page,size))
                .count(() -> service.staffCount())

                // Table output
                .columns("ID","Message","Read","Date")
                .map(n -> new String[]{
                        n.getId(),
                        n.getMessage(),
                        n.isRead() ? "✔" : "✘",
                        n.getCreatedAt().toString()
                })

                // Buttons
                .action("Mark All as Read", () -> {
                    service.markAllStaff();
                    DisplayHelper.success("All staff notifications marked read!");
                    ScreenUtil.pause();
                })
                .back("Back")
                .run();
    }
}
