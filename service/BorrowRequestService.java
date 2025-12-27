package service;

import model.BorrowRequest;
import model.Book;
import model.BookCopy;
import model.enums.RequestStatus;
import repository.BookCopyRepository;
import repository.BookRepository;
import repository.BorrowRequestRepository;

import java.util.ArrayList;
import java.util.List;

public class BorrowRequestService {

    private final BorrowRequestRepository requestRepository;
    private final BookCopyRepository copyRepository;
    private final BookRepository bookRepository;

    public BorrowRequestService() {
        this.requestRepository = new BorrowRequestRepository();
        this.copyRepository = new BookCopyRepository();
        this.bookRepository = new BookRepository();
    }

     public List<BorrowRequest> paged(RequestStatus status,int page,int size){
        return requestRepository.findByStatusPaged(status,page,size);
    }

    public int count(RequestStatus status){
        return requestRepository.countByStatus(status);
    }

    public BorrowRequest find(String id){ return requestRepository.findById(id); }

    public BorrowRequest findActiveByCopyId(String copyId){ return requestRepository.findActiveByCopyId(copyId); }

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

}
