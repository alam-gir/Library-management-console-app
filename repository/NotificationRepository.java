package repository;

import model.Notification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationRepository {

    private final String FILE_PATH = "data/notifications.txt";
    private final FileRepository fileRepository;

    public NotificationRepository() {
        this.fileRepository = new FileRepository();
    }

    // Save notification
    public void save(Notification notification) {
        fileRepository.save(FILE_PATH, toRecord(notification));
    }

    // Find notifications by user ID
    public List<Notification> findByUserId(String userId) {

        List<Notification> notifications = new ArrayList<>();

        for (String row : fileRepository.readAll(FILE_PATH)) {

            Notification n = mapToNotification(row);

            if (n.getUserId().equals(userId)) {
                notifications.add(n);
            }
        }

        return notifications;
    }

    private Notification mapToNotification(String row) {

        String[] parts = row.split("\\|");

        Notification n = new Notification();
        n.setId(parts[0]);
        n.setUserId(parts[1]);
        n.setMessage(parts[2]);
        n.setRead(Boolean.parseBoolean(parts[3]));
        n.setCreatedAt(LocalDateTime.parse(parts[4]));

        return n;
    }

    private String toRecord(Notification n) {

        return n.getId() + "|" +
               n.getUserId() + "|" +
               n.getMessage() + "|" +
               n.isRead() + "|" +
               n.getCreatedAt();
    }
}
