package service;

import model.BorrowRequest;
import model.BorrowedBookView;
import model.Book;
import model.BookCopy;
import model.enums.BookStatus;
import model.enums.RequestStatus;
import repository.BookCopyRepository;
import repository.BookRepository;
import repository.BorrowRequestRepository;
import util.IdGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BorrowRequestService {

    private final BorrowRequestRepository requestRepository = new BorrowRequestRepository();;
    private final BookCopyRepository copyRepository =new BookCopyRepository();
    private final BookRepository bookRepository = new BookRepository();
    private final NotificationService notificationService = new NotificationService();

    private static final int MAX_BORROW_LIMIT = 10;

     public List<BorrowRequest> paged(RequestStatus status,int page,int size){
        return requestRepository.findByStatusPaged(status,page,size);
    }

    public int count(RequestStatus status){
        return requestRepository.countByStatus(status);
    }

    public BorrowRequest find(String id){ return requestRepository.findById(id); }

    public BorrowRequest findActiveByCopyId(String copyId){ return requestRepository.findActiveById(copyId); }

    // Get active borrowed books for a student
    public List<BorrowedBookView> getBorrowedBooks(String studentId) {

        List<BorrowedBookView> result = new ArrayList<>();

        List<BorrowRequest> requests =
                requestRepository.findByStudentId(studentId);

        for (BorrowRequest r : requests) {

            if (r.getStatus() != RequestStatus.APPROVED &&
                r.getStatus() != RequestStatus.CHECKED_OUT) {
                continue;
            }

            BookCopy copy =
                    copyRepository.findById(r.getCopyId());

            if (copy == null) continue;

            Book book =
                    bookRepository.findById(copy.getBookId());

            if (book == null) continue;

            BorrowedBookView view = new BorrowedBookView();
            view.setCopyId(copy.getId());
            view.setBookTitle(book.getTitle());
            view.setBorrowDate(r.getRequestDate().toString());
            view.setStatus(r.getStatus().name());

            result.add(view);
        }

        return result;
    }

    public List<BorrowRequest> getRequestsByStudent(String studentId) {
        return requestRepository.findByStudentId(studentId);
    }

    public List<BorrowRequest> getBorrowedPaged(String sid,int page,int size){
        return requestRepository.findPagedByStudent(sid, RequestStatus.CHECKED_OUT,page,size);
    }

    public int countBorrowed(String sid){
        return requestRepository.countByStudentAndStatus(sid, RequestStatus.CHECKED_OUT);
    }

    public List<BorrowRequest> getAllRequestsPaged(String sid,int page,int size){
        return requestRepository.findPagedAll(sid,page,size);
    }

    public int countAllRequests(String sid){
        return requestRepository.countAllStudentRequests(sid);
    }

    public boolean requestBorrow(String studentId, String bookId){

        // 1) Check student borrow limit
        int activeBorrowed = requestRepository.countByStudentAndStatus(studentId, RequestStatus.CHECKED_OUT);
        if(activeBorrowed >= MAX_BORROW_LIMIT){
            notificationService.notifyStudent(studentId,"Borrow limit reached! Return books first.");
            return false;
        }

        // 2) Prevent duplicate pending requests
        boolean alreadyRequested = requestRepository.findAll().stream()
                .anyMatch(r -> r.getStudentId().equals(studentId) &&
                               r.getCopyId().startsWith(bookId) &&
                               r.getStatus()==RequestStatus.PENDING);
        if(alreadyRequested){
            notificationService.notifyStudent(studentId,"You already requested this book. Wait for approval.");
            return false;
        }

        // 3) Find available copy
        BookCopy copy = copyRepository.findFirstAvailableCopy(bookId);
        if(copy == null){
            notificationService.notifyStudent(studentId,"No available copy for this book right now.");
            return false;
        }

        // 4) Create request entry
        BorrowRequest req = new BorrowRequest();
        req.setId(IdGenerator.generate("REQ"));
        req.setStudentId(studentId);
        req.setCopyId(copy.getId());
        req.setStatus(RequestStatus.PENDING);
        req.setRequestDate(LocalDate.now());
        req.setDueDate(null);

        requestRepository.save(req);

        // 5) Mark copy as requested
        copyRepository.updateStatus(copy.getId(), BookStatus.REQUESTED);

        // 6) Notify staff globally
        notificationService.notifyStaff(
                "New borrow request from Student: "+studentId+" for BookID: "+bookId
        );

        // 7) Confirm user
        notificationService.notifyStudent(studentId,"Borrow request submitted. Waiting approval.");
        return true;
    }

}
