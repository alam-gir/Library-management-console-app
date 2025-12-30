package repository;

import java.util.List;

import model.Staff;

public class StaffRepository {

    private final String FILE_PATH = "data/staffs.txt";
    private final FileRepository fileRepository;

    public StaffRepository() {
        this.fileRepository = new FileRepository();
    }

    // Find staff-specific data by user ID
    public Staff findByUserId(String userId) {

        List<String> row = fileRepository.readAll(FILE_PATH);
        
        for (String r : row) {
            String[] parts = r.split("\\|");

            if (parts[1].equals(userId)) {
                return mapToStaff(r);
            }
        }

        return null;
    }

    // Convert file row to Staff object staff-only fields
    private Staff mapToStaff(String row) {

        String[] parts = row.split("\\|");

        Staff staff = new Staff();
        staff.setId(parts[0]);
        staff.setEmployeeID(parts[1]);
        staff.setDesignation(parts[2]);
        staff.setDepartment(parts[3]);

        return staff;
    }
}
