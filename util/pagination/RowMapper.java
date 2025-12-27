package util.pagination;

@FunctionalInterface
public interface RowMapper<T> {
    String[] map(T item);
}
