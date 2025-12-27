package util.pagination;

@FunctionalInterface
public interface SearchProvider<T> {
    java.util.List<T> search(String keyword, int page, int size);
}
