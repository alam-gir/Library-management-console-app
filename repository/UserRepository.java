package repository;

import model.User;
import model.Student;
import model.Staff;
import model.enums.Role;

public class UserRepository {

    private final String FILE_PATH = "data/users.txt";
    private final FileRepository fileRepository;

    public UserRepository() {
        this.fileRepository = new FileRepository();
    }

    // Find a user by system ID.
    public User findById(String id) {

        String row = fileRepository.readById(FILE_PATH, id);

        if (row == null) {
            return null;
        }

        return mapToUser(row);
    }

    // Authenticate user using name and password. Returns User if credentials match, otherwise null.
    public User authenticate(String name, String password) {

        for (String row : fileRepository.readAll(FILE_PATH)) {

            String[] parts = row.split("\\|");

            String storedName = parts[1];
            String storedPassword = parts[2];

            if (storedName.equals(name) && storedPassword.equals(password)) {
                return mapToUser(row);
            }
        }

        return null;
    }

    // Convert a file row into a User object.
    private User mapToUser(String row) {

        String[] parts = row.split("\\|");

        String id = parts[0];
        String name = parts[1];
        String password = parts[2];
        Role role = Role.valueOf(parts[3]);

        if (role == Role.STUDENT) {
            return new Student(id, name, password, null, null);
        }

        if (role == Role.STAFF) {
            return new Staff(id, name, password, null, null, null);
        }

        return null;
    }
}
