package controller.feature;

import model.Book;
import service.BookService;
import util.*;

import java.util.List;

public class BookListFeature {

    private final BookService bookService;

    private boolean searchMode = false;
    private String searchField;
    private String searchKeyword;

    public BookListFeature(BookService bookService) {
        this.bookService = bookService;
    }

    public void start() {

        int page = 1;
        int size = 5;

        while (true) {
            ScreenUtil.clear();

            int total = searchMode
                    ? bookService.countSearchResults(searchField, searchKeyword)
                    : bookService.getTotalBooks();

            PaginationState state = new PaginationState(page, size, total);

            List<Book> books = searchMode
                    ? bookService.searchBooks(searchField, searchKeyword, page, size)
                    : bookService.getBooks(page, size);

            String title = searchMode
                    ? "Search Results (" + searchField + ": " + searchKeyword + ")"
                    : "Book List";

            DisplayHelper.printSection(title);

            TableRenderer.render(
                    new String[] { "ID", "Title", "Author" },
                    books.stream().map(b -> new String[] {
                            b.getId(),
                            b.getTitle(),
                            b.getAuthor()
                    }).toList());

            PaginationRenderer.render(state);

            int choiceIndex = 1;

            Integer prevOption = null;
            Integer nextOption = null;
            Integer viewOption;
            Integer searchOption;
            Integer clearSearchOption = null;
            Integer addOption;
            Integer deleteOption;
            Integer backOption;

            if (state.hasPrevious()) {
                System.out.println(choiceIndex + ". Previous Page");
                prevOption = choiceIndex++;
            }

            if (state.hasNext()) {
                System.out.println(choiceIndex + ". Next Page");
                nextOption = choiceIndex++;
            }

            System.out.println(choiceIndex + ". View Book Details");
            viewOption = choiceIndex++;

            System.out.println(choiceIndex + ". Search Books");
            searchOption = choiceIndex++;

            if (searchMode) {
                System.out.println(choiceIndex + ". Clear Search");
                clearSearchOption = choiceIndex++;
            }

            System.out.println(choiceIndex + ". Add Book");
            addOption = choiceIndex++;

            System.out.println(choiceIndex + ". Delete Book");
            deleteOption = choiceIndex++;

            System.out.println(choiceIndex + ". Back");
            backOption = choiceIndex;

            int choice = InputHelper.readInt("Choose option", 1, backOption);

            if (prevOption != null && choice == prevOption) {
                page--;
                continue;
            }

            if (nextOption != null && choice == nextOption) {
                page++;
                continue;
            }

            if (choice == viewOption) {
                String id = InputHelper.readString("Book ID");
                new BookDetailsFeature(bookService).start(id);
                continue;
            }

            if (choice == searchOption) {
                activateSearch();
                page = 1;
                continue;
            }

            if (clearSearchOption != null && choice == clearSearchOption) {
                clearSearch();
                page = 1;
                continue;
            }

            if (choice == addOption) {
                addBook();
                continue;
            }

            if (choice == deleteOption) {
                deleteBook();
                continue;
            }

            if (choice == backOption) {
                return;
            }
        }
    }

    private void activateSearch() {

        ScreenUtil.clear();
        DisplayHelper.printSection("Search Books");

        System.out.println("1. Title");
        System.out.println("2. Author");
        System.out.println("3. ISBN");

        int c = InputHelper.readInt("Search by", 1, 3);

        searchField = c == 1 ? "TITLE" : c == 2 ? "AUTHOR" : "ISBN";
        searchKeyword = InputHelper.readString("Enter keyword");

        searchMode = true;
    }

    private void clearSearch() {
        searchMode = false;
        searchField = null;
        searchKeyword = null;
    }

    private void addBook() {

        String isbn = InputHelper.readString("ISBN");
        String title = InputHelper.readString("Title");
        String author = InputHelper.readString("Author");

        bookService.addBook(isbn, title, author);
        DisplayHelper.success("Book added");
        ScreenUtil.pause();
    }

    private void deleteBook() {

        String id = InputHelper.readString("Book ID");
        bookService.deleteBook(id);

        DisplayHelper.success("Book deleted");
        ScreenUtil.pause();
    }
}
