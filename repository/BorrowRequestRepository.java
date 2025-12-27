package repository;

import model.BorrowRequest;
import model.enums.RequestStatus;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class BorrowRequestRepository {

    private final String FILE_PATH = "data/borrow_requests.txt";
    private final FileRepository fileRepository;

    public BorrowRequestRepository() {
        this.fileRepository = new FileRepository();
    }

    //================== SAVE / FIND ==================//

    public void save(BorrowRequest request) {
        fileRepository.save(FILE_PATH, toRecord(request));
    }

    public BorrowRequest findById(String id) {
        String row = fileRepository.readById(FILE_PATH, id);
        return row == null ? null : map(row);
    }

    //================== LIST + SORT (NEWEST FIRST) ==================//

    /** Returns all requests sorted by latest date first */
    public List<BorrowRequest> findAll() {
        return fileRepository.readAll(FILE_PATH).stream()
                .map(this::map)
                // 🔥 sort newest first
                .sorted(Comparator.comparing(BorrowRequest::getRequestDate).reversed())
                .collect(Collectors.toList());
    }

    public List<BorrowRequest> findByStudentId(String studentId) {
        return findAll().stream()
                .filter(r -> r.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    public List<BorrowRequest> findByStatus(RequestStatus status) {
        return findAll().stream()
                .filter(r -> r.getStatus() == status)
                .collect(Collectors.toList());
    }

    //================== PAGINATION (NEWEST FIRST) ==================//

    /** Return status filtered & paginated */
    public List<BorrowRequest> findByStatusPaged(RequestStatus status, int page, int size) {
        List<BorrowRequest> filtered = findByStatus(status);
        int start = (page - 1) * size, end = Math.min(start + size, filtered.size());
        return start >= filtered.size() ? new ArrayList<>() : filtered.subList(start, end);
    }

    /** Student paginated active/borrowed requests */
    public List<BorrowRequest> findPagedByStudent(String studentId, RequestStatus status, int page, int size) {
        int skip = (page - 1) * size;
        return findAll().stream()
                .filter(r -> r.getStudentId().equals(studentId) && r.getStatus() == status)
                .skip(skip)
                .limit(size)
                .collect(Collectors.toList());
    }

    /** Student request history paginated */
    public List<BorrowRequest> findPagedAll(String studentId, int page, int size) {
        int skip = (page - 1) * size;
        return findAll().stream()
                .filter(r -> r.getStudentId().equals(studentId))
                .skip(skip)
                .limit(size)
                .collect(Collectors.toList());
    }

    //================== COUNT ==================//

    public int countByStudentAndStatus(String studentId, RequestStatus status) {
        return (int) fileRepository.readAll(FILE_PATH).stream()
                .map(this::map)
                .filter(r -> r.getStudentId().equals(studentId) && r.getStatus() == status)
                .count();
    }

    public int countAllStudentRequests(String studentId) {
        return (int) fileRepository.readAll(FILE_PATH).stream()
                .map(this::map)
                .filter(r -> r.getStudentId().equals(studentId))
                .count();
    }

    public int countByStatus(RequestStatus status) {
        return (int) findAll().stream()
                .filter(r -> r.getStatus() == status)
                .count();
    }

    //================== ACTIVE REQUEST ==================//

    public BorrowRequest findActiveByCopyId(String copyId) {
        return findAll().stream()
                .filter(r -> r.getCopyId().equals(copyId) &&
                        (r.getStatus() == RequestStatus.CHECKED_OUT ||
                         r.getStatus() == RequestStatus.APPROVED))
                .findFirst()
                .orElse(null);
    }

    //================== UPDATE ==================//

    public void update(BorrowRequest request) {
        fileRepository.updateById(FILE_PATH, request.getId(), toRecord(request));
    }

    //================== MAPPING ==================//

    private BorrowRequest map(String row) {
        String[] p = row.split("\\|");

        BorrowRequest r = new BorrowRequest();
        r.setId(p[0]);
        r.setStudentId(p[1]);
        r.setCopyId(p[2]);
        r.setStatus(RequestStatus.valueOf(p[3]));
        r.setRequestDate(LocalDate.parse(p[4]));
        r.setDueDate(p.length > 5 && !p[5].isEmpty() ? LocalDate.parse(p[5]) : null);

        return r;
    }

    private String toRecord(BorrowRequest r) {
        return r.getId() + "|" + r.getStudentId() + "|" + r.getCopyId() + "|" +
                r.getStatus() + "|" + r.getRequestDate() + "|" +
                (r.getDueDate() == null ? "" : r.getDueDate());
    }
}
