package service;

import model.BorrowRequest;
import model.BookCopy;
import model.enums.BookStatus;
import model.enums.RequestStatus;
import repository.BookCopyRepository;
import repository.BorrowRequestRepository;

import java.time.LocalDate;

public class StaffService {

    private final BorrowRequestRepository requestRepo;
    private final BookCopyRepository copyRepo;
    private final NotificationService notify;

    public StaffService() {
        this.requestRepo = new BorrowRequestRepository();
        this.copyRepo = new BookCopyRepository();
        this.notify = new NotificationService();
    }

    public boolean approve(String requestId) {

        BorrowRequest r = requestRepo.findById(requestId);
        if (r == null || r.getStatus() != RequestStatus.PENDING)
            return false;

        BookCopy cp = copyRepo.findById(r.getCopyId());
        if (cp == null || cp.getStatus() != BookStatus.REQUESTED)
            return false;

        cp.setStatus(BookStatus.READY_FOR_PICKUP);
        copyRepo.update(cp);

        r.setStatus(RequestStatus.APPROVED);
        requestRepo.update(r);

        notify.notifyStudent(r.getStudentId(),
                "Book is ready to collect.");

        notify.notifyStaff("Request " + requestId + " approved for student " + r.getStudentId());

        return true;
    }

    public boolean reject(String requestId) {
        BorrowRequest r = requestRepo.findById(requestId);
        if (r == null || r.getStatus() != RequestStatus.PENDING)
            return false;

        r.setStatus(RequestStatus.REJECTED);
        requestRepo.update(r);

        notify.notifyStudent(r.getStudentId(), "Borrow request was rejected.");
        return true;
    }

    public boolean checkout(String requestId) {

        BorrowRequest r = requestRepo.findById(requestId);
        if (r == null || r.getStatus() != RequestStatus.APPROVED)
            return false;

        BookCopy cp = copyRepo.findById(r.getCopyId());
        if (cp == null || cp.getStatus() != BookStatus.READY_FOR_PICKUP)
            return false;

        cp.setStatus(BookStatus.BORROWED);
        copyRepo.update(cp);

        r.setStatus(RequestStatus.CHECKED_OUT);
        r.setDueDate(LocalDate.now().plusDays(14));
        requestRepo.update(r);

        notify.notifyStudent(r.getStudentId(), "Book borrowed. Due date: " + r.getDueDate());
        return true;
    }

    public boolean returnBook(String copyId) {

        BookCopy cp = copyRepo.findById(copyId);
        if (cp == null || cp.getStatus() != BookStatus.BORROWED)
            return false;

        BorrowRequest r = requestRepo.findActiveByCopyId(copyId);
        if (r == null)
            return false;

        cp.setStatus(BookStatus.AVAILABLE);
        copyRepo.update(cp);

        notify.notifyStudent(r.getStudentId(), "Book returned successfully.");
        return true;
    }
}
