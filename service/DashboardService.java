package service;

import repository.BookRepository;
import repository.BookCopyRepository;
import repository.StudentRepository;
import repository.BorrowRequestRepository;
import model.enums.BookStatus;
import model.enums.RequestStatus;

public class DashboardService {

    private final BookRepository bookRepo = new BookRepository();
    private final BookCopyRepository copyRepo = new BookCopyRepository();
    private final BorrowRequestRepository reqRepo = new BorrowRequestRepository();
    private final StudentRepository studentRepo = new StudentRepository();

    public int totalBooks(){ return bookRepo.count(); }

    public int totalCopies(){ return copyRepo.countAll(); }

    public int availableCopies(){ return copyRepo.countByStatus(BookStatus.AVAILABLE); }

    public int borrowedCopies(){ return copyRepo.countByStatus(BookStatus.BORROWED); }

    public int totalStudents(){ return studentRepo.count(); }

    public int pendingRequests(){ return reqRepo.countByStatus(RequestStatus.PENDING); }

    public int approvedWaitingPickup(){ return reqRepo.countByStatus(RequestStatus.APPROVED); }

    public int currentlyBorrowed(){ return reqRepo.countByStatus(RequestStatus.CHECKED_OUT); }

    public int overdue(){
        return reqRepo.findAll().stream()
                .filter(r -> r.getDueDate()!=null && r.getDueDate().isBefore(java.time.LocalDate.now()))
                .toList().size();
    }
}
