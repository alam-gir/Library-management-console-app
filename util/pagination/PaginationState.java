package util.pagination;

public class PaginationState {

    private final int currentPage;
    private final int pageSize;
    private final int totalRecords;

    public PaginationState(int currentPage, int pageSize, int totalRecords) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalRecords = totalRecords;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public int getTotalPages() {
        if (totalRecords == 0) {
            return 1;
        }
        return (int) Math.ceil((double) totalRecords / pageSize);
    }

    public int getStartIndex() {
        if (totalRecords == 0) {
            return 0;
        }
        return (currentPage - 1) * pageSize + 1;
    }

    public int getEndIndex() {
        int end = currentPage * pageSize;
        return Math.min(end, totalRecords);
    }

    public boolean hasNext() {
        return currentPage < getTotalPages();
    }

    public boolean hasPrevious() {
        return currentPage > 1;
    }
}
