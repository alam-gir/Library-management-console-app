package util.pagination;

@FunctionalInterface
public interface DataProvider<T> {
    java.util.List<T> fetch(int page, int size);
}
