package util.pagination;

import util.*;
import java.util.*;

public class PaginatedListView<T> {

    private String title = "List";
    private int pageSize = 5;
    private String backLabel = "Back";

    private DataProvider<T> provider;
    private TotalProvider totalProvider;

    private SearchProvider<T> searchProvider;
    private SearchCountProvider searchCountProvider;

    private String[] columns;
    private RowMapper<T> mapper;

    private boolean searchMode = false;
    private String searchKeyword = null;

    private final LinkedHashMap<String, Runnable> actionsNoId = new LinkedHashMap<>();
    private final LinkedHashMap<String, java.util.function.Consumer<String>> actionsWithId = new LinkedHashMap<>();

    public PaginatedListView<T> title(String t) {
        this.title = t;
        return this;
    }

    public PaginatedListView<T> pageSize(int s) {
        this.pageSize = s;
        return this;
    }

    public PaginatedListView<T> back(String label) {
        this.backLabel = label;
        return this;
    }

    public PaginatedListView<T> fetch(DataProvider<T> p) {
        this.provider = p;
        return this;
    }

    public PaginatedListView<T> count(TotalProvider c) {
        this.totalProvider = c;
        return this;
    }

    public PaginatedListView<T> search(SearchProvider<T> p) {
        this.searchProvider = p;
        return this;
    }

    public PaginatedListView<T> searchCount(SearchCountProvider c) {
        this.searchCountProvider = c;
        return this;
    }

    public PaginatedListView<T> columns(String... c) {
        this.columns = c;
        return this;
    }

    public PaginatedListView<T> map(RowMapper<T> m) {
        this.mapper = m;
        return this;
    }

    public PaginatedListView<T> action(String label, Runnable r) {
        actionsNoId.put(label, r);
        return this;
    }

    public PaginatedListView<T> action(String label, java.util.function.Consumer<String> r) {
        actionsWithId.put(label, r);
        return this;
    }

    public void run(){

    final int[] page = {1};

    mainLoop:
    while(true){

        ScreenUtil.clear();
        DisplayHelper.printSection(title);

        boolean searching = searchMode && searchKeyword != null;

        int total = searching ? searchCountProvider.count(searchKeyword)
                             : totalProvider.count();

        PaginationState state = new PaginationState(page[0], pageSize, total);

        List<T> data = searching ? searchProvider.search(searchKeyword, page[0], pageSize)
                                : provider.fetch(page[0], pageSize);

        if(!data.isEmpty()){
            TableRenderer.render(columns,
                data.stream().map(mapper::map).toList()
            );
            PaginationRenderer.render(state);
        } else {
            DisplayHelper.info("No records found.");
        }

        //---------------------------------------------------
        // Build unified ordered menu
        //---------------------------------------------------
        List<String> labels = new ArrayList<>();
        List<Runnable> tasks = new ArrayList<>();

        if(state.hasPrevious()){
            labels.add("Previous Page");
            tasks.add(() -> page[0]--);
        }

        if(state.hasNext()){
            labels.add("Next Page");
            tasks.add(() -> page[0]++);
        }

        if(!data.isEmpty()){
            actionsWithId.forEach((txt,fn)->{
                labels.add(txt);
                tasks.add(() -> {
                    String id = InputHelper.readString("Enter ID");
                    fn.accept(id);
                });
            });

            actionsNoId.forEach((txt,fn)->{
                labels.add(txt);
                tasks.add(() -> {
                    fn.run();
                });
            });
        }

        if(searchProvider != null){
            if(searching){
                labels.add("Clear Search");
                tasks.add(() -> { searchKeyword=null; searchMode=false; page[0]=1; });
            } else {
                labels.add("Search");
                tasks.add(() -> {
                    searchKeyword = InputHelper.readString("Keyword");
                    searchMode=true; page[0]=1;
                });
            }
        }

        labels.add(backLabel);
        tasks.add(() -> { /* exit loop */ });

        //---------------------------------------------------
        int choice = MenuRenderer.dynamic("Options", labels.toArray(new String[0]));

        if(choice == labels.size()) break mainLoop;   // Last option = Back
        tasks.get(choice-1).run();
    }
}

}
