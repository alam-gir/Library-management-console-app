package repository;

import model.BorrowRequest;
import model.enums.RequestStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BorrowRequestRepository {

    private final String FILE_PATH = "data/borrow_requests.txt";
    private final FileRepository fileRepository;

    public BorrowRequestRepository() {
        this.fileRepository = new FileRepository();
    }

    public void save(BorrowRequest request) {
        fileRepository.save(FILE_PATH, toRecord(request));
    }

    public BorrowRequest findById(String id) {
        String row = fileRepository.readById(FILE_PATH, id);
        return row == null ? null : map(row);
    }

    
    public List<BorrowRequest> findByStudentId(String studentId) {
        List<BorrowRequest> requests = new ArrayList<>();
        for (String row : fileRepository.readAll(FILE_PATH)) {
            BorrowRequest r = map(row);
            if (r.getStudentId().equals(studentId)) {
                requests.add(r);
            }
        }
        return requests;
    }

    public List<BorrowRequest> findAll() {
        List<BorrowRequest> list = new ArrayList<>();
        for(String row:fileRepository.readAll(FILE_PATH)) list.add(map(row));
        return list;
    }

    // return only requests with given status (paged)
    public List<BorrowRequest> findByStatusPaged(RequestStatus status, int page, int size){
        List<BorrowRequest> filtered = findByStatus(status);
        int start=(page-1)*size, end=Math.min(start+size, filtered.size());
        return start>=filtered.size()?new ArrayList<>():filtered.subList(start,end);
    }

    public int countByStatus(RequestStatus status){
        int c=0;
        for(BorrowRequest r:findAll()) if(r.getStatus()==status) c++;
        return c;
    }

    public List<BorrowRequest> findByStatus(RequestStatus status){
        List<BorrowRequest> list=new ArrayList<>();
        for(BorrowRequest r:findAll()) if(r.getStatus()==status) list.add(r);
        return list;
    }

    public BorrowRequest findActiveByCopyId(String copyId) {
        for (BorrowRequest r:findAll()){
            if(r.getCopyId().equals(copyId) &&
               (r.getStatus()==RequestStatus.CHECKED_OUT || r.getStatus()==RequestStatus.APPROVED))
               return r;
        }
        return null;
    }

    public void update(BorrowRequest request) {
        fileRepository.updateById(FILE_PATH, request.getId(), toRecord(request));
    }

    private BorrowRequest map(String row){
        String[] p=row.split("\\|");
        BorrowRequest r=new BorrowRequest();

        r.setId(p[0]);
        r.setStudentId(p[1]);
        r.setCopyId(p[2]);
        r.setStatus(RequestStatus.valueOf(p[3]));
        r.setRequestDate(LocalDate.parse(p[4]));
        r.setDueDate(p.length>5 && !p[5].isEmpty()?LocalDate.parse(p[5]):null);

        return r;
    }

    private String toRecord(BorrowRequest r){
        return r.getId()+"|"+r.getStudentId()+"|"+r.getCopyId()+"|"+
                r.getStatus()+"|"+r.getRequestDate()+"|"+
                (r.getDueDate()==null?"":r.getDueDate());
    }
}
