package util;

import java.util.List;
import java.util.function.Function;

public class PaginationHelper<T> {

    private final int pageSize;

    public PaginationHelper(int pageSize) {
        this.pageSize = pageSize;
    }

    // Start paginated view
    public void start(Function<Integer, List<T>> dataProvider,
                      Function<T, String> itemRenderer,
                      String title) {

        int currentPage = 1;

        while (true) {
            ScreenUtil.clear();
            DisplayHelper.printSection(title + " : Page " + currentPage);

            List<T> items = dataProvider.apply(currentPage);

            if (items.isEmpty()) {
                DisplayHelper.empty("No data found");
            } else {
                for (T item : items) {
                    System.out.println(itemRenderer.apply(item));
                }
            }

            DisplayHelper.emptyLine();

            boolean showPrev = currentPage > 1;
            boolean showNext = items.size() == pageSize;

            int optionIndex = 1;

            if (showPrev) {
                System.out.println(optionIndex++ + ". Previous Page");
            }

            if (showNext) {
                System.out.println(optionIndex++ + ". Next Page");
            }

            System.out.println(optionIndex + ". Back");

            int choice = InputHelper.readInt("Choose option", 1, optionIndex);

            if (showPrev && choice == 1) {
                currentPage--;
                continue;
            }

            if (showPrev && showNext && choice == 2) {
                currentPage++;
                continue;
            }

            if (!showPrev && showNext && choice == 1) {
                currentPage++;
                continue;
            }

            return;
        }
    }
}
