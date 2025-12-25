package model;

import model.enums.RequestStatus;
import java.time.LocalDate;

public class BorrowRequest {

    private String id;
    private String studentId;
    private String copyId;
    private RequestStatus status;
    private LocalDate requestDate;

    public BorrowRequest() {}

    public BorrowRequest(String id, String studentId, String copyId,
                         RequestStatus status, LocalDate requestDate) {
        this.id = id;
        this.studentId = studentId;
        this.copyId = copyId;
        this.status = status;
        this.requestDate = requestDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCopyId() {
        return copyId;
    }

    public void setCopyId(String copyId) {
        this.copyId = copyId;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }
}
