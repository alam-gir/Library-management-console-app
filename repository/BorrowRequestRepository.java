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

    // Save new borrow request
    public void save(BorrowRequest request) {
        fileRepository.save(FILE_PATH, toRecord(request));
    }

    // Find by student ID
    public List<BorrowRequest> findByStudentId(String studentId) {
        List<BorrowRequest> requests = new ArrayList<>();
        for (String row : fileRepository.readAll(FILE_PATH)) {
            BorrowRequest r = mapToRequest(row);
            if (r.getStudentId().equals(studentId)) {
                requests.add(r);
            }
        }
        return requests;
    }

    // Find request by ID
    public BorrowRequest findById(String requestId) {

        String row = fileRepository.readById(FILE_PATH, requestId);

        if (row == null) {
            return null;
        }

        return mapToRequest(row);
    }

    // Find all pending requests
    public List<BorrowRequest> findPending() {

        List<BorrowRequest> requests = new ArrayList<>();

        for (String row : fileRepository.readAll(FILE_PATH)) {
            BorrowRequest request = mapToRequest(row);
            if (request.getStatus() == RequestStatus.PENDING) {
                requests.add(request);
            }
        }

        return requests;
    }

    // Update request status
    public void updateStatus(String requestId, RequestStatus status) {

        BorrowRequest request = findById(requestId);

        if (request == null) {
            return;
        }

        request.setStatus(status);
        fileRepository.updateById(FILE_PATH, requestId, toRecord(request));
    }

    private BorrowRequest mapToRequest(String row) {

        String[] parts = row.split("\\|");

        BorrowRequest request = new BorrowRequest();
        request.setId(parts[0]);
        request.setStudentId(parts[1]);
        request.setCopyId(parts[2]);
        request.setStatus(RequestStatus.valueOf(parts[3]));
        request.setRequestDate(LocalDate.parse(parts[4]));

        return request;
    }

    private String toRecord(BorrowRequest request) {

        return request.getId() + "|" +
               request.getStudentId() + "|" +
               request.getCopyId() + "|" +
               request.getStatus() + "|" +
               request.getRequestDate();
    }
}
