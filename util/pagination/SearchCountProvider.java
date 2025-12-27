package util.pagination;

@FunctionalInterface
public interface SearchCountProvider {
    int count(String keyword);
}
