package service;

import model.BorrowRequest;
import model.enums.BookStatus;
import repository.BookCopyRepository;
import repository.BorrowRequestRepository;
import model.enums.RequestStatus;

import java.util.List;

public class StaffService {

    private final BorrowRequestRepository requestRepository;
    private final BookCopyRepository bookCopyRepository;

    public StaffService() {
        this.requestRepository = new BorrowRequestRepository();
        this.bookCopyRepository = new BookCopyRepository();
    }

    // View all pending requests
    public List<BorrowRequest> viewPendingRequests() {
        return requestRepository.findPending();
    }

    // Approve borrow request
    // public void approveRequest(String requestId) {

    //     BorrowRequest request = requestRepository.findById(requestId);

    //     if (request == null) {
    //         return;
    //     }

    //     bookCopyRepository.updateStatus(
    //             request.getCopyId(),
    //             BookStatus.READY_FOR_PICKUP
    //     );

    //     requestRepository.updateStatus(requestId, RequestStatus.APPROVED);
    // }

    // // Mark book as borrowed when student picks it up
    // public void checkoutBook(String copyId) {
    //     bookCopyRepository.updateStatus(copyId, BookStatus.BORROWED);
    // }

    // // Return book
    // public void returnBook(String copyId) {
    //     bookCopyRepository.updateStatus(copyId, BookStatus.AVAILABLE);
    // }
}
