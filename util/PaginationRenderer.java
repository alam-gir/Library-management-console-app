package util;

public class PaginationRenderer {

    // Print pagination info
    public static void render(PaginationState state) {

        System.out.println("----------------------------------------");

        if (state.getTotalRecords() == 0) {
            System.out.println("Showing 0 records");
        } else {
            System.out.println(
                    "Showing " +
                    state.getStartIndex() + "–" +
                    state.getEndIndex() +
                    " of " + state.getTotalRecords() + " records"
            );
            System.out.println(
                    "Page " +
                    state.getCurrentPage() +
                    " of " +
                    state.getTotalPages()
            );
        }

        System.out.println("----------------------------------------");
    }
}
