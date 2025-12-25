package repository;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileRepository {

    // Reads all data rows from a file
    public List<String> readAll(String filePath) {
        List<String> records = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {

            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                if (!line.trim().isEmpty()) {
                    records.add(line);
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + filePath);
        }

        return records;
    }

    // Reads paginated data rows from a file
    public List<String> readPage(String filePath, int pageNumber, int pageSize) {
        List<String> records = new ArrayList<>();

        if (pageNumber < 1 || pageSize < 1) {
            return records;
        }

        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = startIndex + pageSize;

        int currentIndex = 0;

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {

            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {

                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                if (currentIndex >= startIndex && currentIndex < endIndex) {
                    records.add(line);
                }

                if (currentIndex >= endIndex) {
                    break;
                }

                currentIndex++;
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + filePath);
        }

        return records;
    }

    // read a record by ID
    public String readById(String filePath, String id) {

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {

            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {

                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|");

                if (parts.length > 0 && parts[0].equals(id)) {
                    return line;
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + filePath);
        }

        return null;
    }

    // Appends a new record to the file.
    public void save(String filePath, String record) {

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(filePath, true))) {

            writer.newLine();
            writer.write(record);

        } catch (IOException e) {
            System.out.println("Error writing to file: " + filePath);
        }
    }

    // Rewrites the file completely (keeps header).
    public void overwrite(String filePath, List<String> records) {

        try {
            List<String> allLines = Files.readAllLines(Paths.get(filePath));

            if (allLines.isEmpty()) {
                return;
            }

            String header = allLines.get(0);

            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
                writer.write(header);

                for (String record : records) {
                    writer.newLine();
                    writer.write(record);
                }
            }

        } catch (IOException e) {
            System.out.println("Error overwriting file: " + filePath);
        }
    }

    // Delete a record by ID ( ID is the first column).
    public void deleteById(String filePath, String id) {

        List<String> records = readAll(filePath);
        List<String> updatedRecords = new ArrayList<>();

        for (String record : records) {
            String[] parts = record.split("\\|");
            if (!parts[0].equals(id)) {
                updatedRecords.add(record);
            }
        }

        overwrite(filePath, updatedRecords);
    }

    // Update a record by ID.
    public void updateById(String filePath, String id, String newRecord) {

        List<String> records = readAll(filePath);
        List<String> updatedRecords = new ArrayList<>();

        for (String record : records) {
            String[] parts = record.split("\\|");

            if (parts[0].equals(id)) {
                updatedRecords.add(newRecord);
            } else {
                updatedRecords.add(record);
            }
        }

        overwrite(filePath, updatedRecords);
    }
}
