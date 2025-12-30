package repository;

import model.Notification;
import model.enums.NotificationType;
import java.time.LocalDateTime;
import java.util.*;

public class NotificationRepository {

    private final String FILE_PATH = "data/notifications.txt";
    private final FileRepository fileRepo = new FileRepository();

    //================== CRUD ==================//

    public void save(Notification n){
        fileRepo.save(FILE_PATH, toRecord(n));
    }

    public Notification findById(String id){
        String row = fileRepo.readById(FILE_PATH,id);
        return row==null ? null : map(row);
    }

    public void markRead(String id){
        Notification n = findById(id);
        if(n==null) return;
        n.setRead(true);
        fileRepo.updateById(FILE_PATH,id,toRecord(n));
    }


    //================== PAGINATION (sorted newest first) ==================//

    /** Get total student notifications count */
    public int countStudent(String userId){
        return (int) readAllSorted().stream()
                .filter(n -> n.getType()==NotificationType.STUDENT &&
                             n.getUserId().equals(userId))
                .count();
    }

    /** Get paged notifications for student NEWEST FIRST */
    public List<Notification> pagedStudent(String userId,int page,int size){
        return readAllSorted().stream()
                .filter(n -> n.getType()==NotificationType.STUDENT &&
                             n.getUserId().equals(userId))
                .skip((page-1)*size)
                .limit(size)
                .toList();
    }

    /** Staff count (global) */
    public int countStaff(){
        return (int) readAllSorted().stream()
                .filter(n -> n.getType()==NotificationType.STAFF)
                .count();
    }

    /** Staff paginated NEWEST FIRST */
    public List<Notification> pagedStaff(int page,int size){
        return readAllSorted().stream()
                .filter(n -> n.getType()==NotificationType.STAFF)
                .skip((page-1)*size)
                .limit(size)
                .toList();
    }


    //================== UNREAD ==================//

    public int countUnreadStudent(String userId){
        return (int) readAllSorted().stream()
                .filter(n -> n.getType()==NotificationType.STUDENT &&
                             n.getUserId().equals(userId) &&
                             !n.isRead())
                .count();
    }

    public int countUnreadStaff(){
        return (int) readAllSorted().stream()
                .filter(n -> n.getType()==NotificationType.STAFF &&
                             !n.isRead())
                .count();
    }


    //================== MARK ALL READ ==================//

    public void markAllStudent(String userId){
        readAllSorted().stream()
                .filter(n -> n.getType()==NotificationType.STUDENT &&
                             n.getUserId().equals(userId))
                .forEach(n -> {
                    n.setRead(true);
                    fileRepo.updateById(FILE_PATH,n.getId(),toRecord(n));
                });
    }

    public void markAllStaffRead(){
        readAllSorted().stream()
                .filter(n -> n.getType()==NotificationType.STAFF)
                .forEach(n -> {
                    n.setRead(true);
                    fileRepo.updateById(FILE_PATH,n.getId(),toRecord(n));
                });
    }


    //================== INTERNAL HELPERS ==================//

    /** Read all notifications & sort newest first, unread prioritized */
    private List<Notification> readAllSorted(){
        return fileRepo.readAll(FILE_PATH).stream()
                .map(this::map)
                .sorted((a,b) -> {
                    // unread first
                    if(!a.isRead() && b.isRead()) return -1;
                    if(a.isRead() && !b.isRead()) return 1;
                    // then newest first
                    return b.getCreatedAt().compareTo(a.getCreatedAt());
                })
                .toList();
    }


    //================== Mapping ==================//

    private Notification map(String row){
        String[] p=row.split("\\|");
        Notification n = new Notification();
        n.setId(p[0]);
        n.setUserId(p[1]);
        n.setType(NotificationType.valueOf(p[2]));
        n.setMessage(p[3]);
        n.setRead(Boolean.parseBoolean(p[4]));
        n.setCreatedAt(LocalDateTime.parse(p[5]));
        return n;
    }

    private String toRecord(Notification n){
        return n.getId()+"|"+n.getUserId()+"|"+n.getType()+"|"+
                n.getMessage()+"|"+n.isRead()+"|"+n.getCreatedAt();
    }
}
