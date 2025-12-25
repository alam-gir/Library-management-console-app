package util;

import repository.FileRepository;

import java.util.ArrayList;
import java.util.List;

public class IdGenerator {

    private static final String FILE_PATH = "data/id_counters.txt";
    private static final FileRepository fileRepository = new FileRepository();

    // Generate next ID for a given entity
    public static synchronized String generate(String entity) {

        List<String> rows = fileRepository.readAll(FILE_PATH);
        List<String> updatedRows = new ArrayList<>();

        int nextNumber = 1;
        boolean found = false;

        for (String row : rows) {

            String[] parts = row.split("\\|");

            if (parts[0].equals(entity)) {
                nextNumber = Integer.parseInt(parts[1]) + 1;
                updatedRows.add(entity + "|" + nextNumber);
                found = true;
            } else {
                updatedRows.add(row);
            }
        }

        if (!found) {
            updatedRows.add(entity + "|1");
            nextNumber = 1;
        }

        fileRepository.overwrite(FILE_PATH, updatedRows);

        return entityPrefix(entity) + nextNumber;
    }

    // Map entity name to ID prefix
    private static String entityPrefix(String entity) {

        switch (entity) {
            case "USER":
                return "U";
            case "BOOK":
                return "B";
            case "BOOK_COPY":
                return "C";
            case "REQUEST":
                return "R";
            case "NOTIFICATION":
                return "N";
            default:
                return "X";
        }
    }
}
