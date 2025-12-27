package service;

import model.Notification;
import model.enums.NotificationType;
import repository.NotificationRepository;
import util.IdGenerator;

import java.time.LocalDateTime;
import java.util.List;

public class NotificationService {

    private final NotificationRepository repo = new NotificationRepository();

    //================== Send Notifications ==================//

    public void notifyStudent(String userId,String message){
        Notification n = new Notification();
        n.setId(IdGenerator.generate("NT"));
        n.setUserId(userId);
        n.setType(NotificationType.STUDENT);
        n.setMessage(message);
        n.setRead(false);
        n.setCreatedAt(LocalDateTime.now());
        repo.save(n);
    }

    public void notifyStaff(String message){
        Notification n = new Notification();
        n.setId(IdGenerator.generate("NT"));
        n.setUserId("*");
        n.setType(NotificationType.STAFF);
        n.setMessage(message);
        n.setRead(false);
        n.setCreatedAt(LocalDateTime.now());
        repo.save(n);
    }

    //================== Fetch Pagination ==================//

    public List<Notification> getStudentPaged(String id,int page,int size){
        return repo.pagedStudent(id,page,size);
    }

    public int studentCount(String id){
        return repo.countStudent(id);
    }

    public List<Notification> getStaffPaged(int page,int size){
        return repo.pagedStaff(page,size);
    }

    public int staffCount(){
        return repo.countStaff();
    }

    //================== Read ==================//

    public void markAllStudent(String studentId){
        repo.markAllStudent(studentId);
    }

    public void markAllStaff(){
        repo.markAllStaffRead();
    }

    //================== Unread Count ==================//

    public int unreadForStudent(String id){
        return repo.countUnreadStudent(id);
    }

    public int unreadForStaff(){
        return repo.countUnreadStaff();
    }
}
