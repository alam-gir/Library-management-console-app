package service;

import model.Notification;
import repository.NotificationRepository;
import util.IdGenerator;

import java.time.LocalDateTime;
import java.util.List;

public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService() {
        this.notificationRepository = new NotificationRepository();
    }

    public void notifyUser(String userId, String message) {

        Notification n = new Notification();
        n.setId(IdGenerator.generate("NOTIFICATION"));
        n.setUserId(userId);
        n.setMessage(message);
        n.setRead(false);
        n.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(n);
    }

    public List<Notification> getUserNotifications(String userId) {
        return notificationRepository.findByUserId(userId);
    }
}
