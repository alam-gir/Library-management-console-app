package repository;

import model.Notification;
import model.enums.NotificationType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    //================== PAGINATION ==================//

    public int countStudent(String userId){
        return (int) fileRepo.readAll(FILE_PATH).stream()
                .map(this::map)
                .filter(n -> n.getType()==NotificationType.STUDENT &&
                             n.getUserId().equals(userId))
                .count();
    }

    public List<Notification> pagedStudent(String userId,int page,int size){
        int skip=(page-1)*size;

        return fileRepo.readAll(FILE_PATH).stream()
                .map(this::map)
                .filter(n -> n.getType()==NotificationType.STUDENT &&
                             n.getUserId().equals(userId))
                .skip(skip)
                .limit(size)
                .collect(Collectors.toList());
    }

    public int countStaff(){
        return (int) fileRepo.readAll(FILE_PATH).stream()
                .map(this::map)
                .filter(n -> n.getType()==NotificationType.STAFF)
                .count();
    }

    public List<Notification> pagedStaff(int page,int size){
        int skip = (page-1)*size;

        return fileRepo.readAll(FILE_PATH).stream()
                .map(this::map)
                .filter(n -> n.getType()==NotificationType.STAFF)
                .skip(skip)
                .limit(size)
                .collect(Collectors.toList());
    }

    //================== UNREAD COUNT ==================//

    public int countUnreadStudent(String userId){
        return (int) fileRepo.readAll(FILE_PATH).stream()
                .map(this::map)
                .filter(n -> n.getType()==NotificationType.STUDENT &&
                             n.getUserId().equals(userId) &&
                             !n.isRead())
                .count();
    }

    public int countUnreadStaff(){
        return (int) fileRepo.readAll(FILE_PATH).stream()
                .map(this::map)
                .filter(n -> n.getType()==NotificationType.STAFF &&
                             !n.isRead())
                .count();
    }

    //================== MARK ALL ==================//

    public void markAllStudent(String userId){
        List<Notification> list = pagedStudent(userId,1,9999);
        list.forEach(n -> {
            n.setRead(true);
            fileRepo.updateById(FILE_PATH,n.getId(),toRecord(n));
        });
    }

    public void markAllStaffRead(){
        List<Notification> list = fileRepo.readAll(FILE_PATH).stream()
                .map(this::map)
                .filter(n -> n.getType()==NotificationType.STAFF)
                .collect(Collectors.toList());

        list.forEach(n -> {
            n.setRead(true);
            fileRepo.updateById(FILE_PATH,n.getId(),toRecord(n));
        });
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
