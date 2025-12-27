package controller.feature;

import model.Book;
import service.BookService;
import util.pagination.PaginatedListView;

public class BookListFeature {

    private final BookService service = new BookService();

    public void start(){

        new PaginatedListView<Book>()
            .title("📚 Book List")
            .pageSize(6)
            .fetch(service::getBooks)
            .count(service::getTotalBooks)
            .search(service::searchBooks)
            .searchCount(service::countSearchResults)
            .columns("ID","Title","Author")
            .map(b -> new String[]{ b.getId(), b.getTitle(), b.getAuthor() })
            .action("View Details", id -> new BookDetailsFeature(service))
            .action("Delete Book", service::deleteBook)
            .action("Add Book", service::addBook)
            .back("Back")
            .run();
    }
}
