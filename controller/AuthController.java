package controller;

import model.User;
import model.Student;
import model.Staff;
import service.UserService;
import util.DisplayHelper;
import util.InputHelper;
import util.ScreenUtil;

public class AuthController {

    private final UserService userService;

    public AuthController() {
        this.userService = new UserService();
    }

    // Entry point for authentication flow
    public void start() {

        while (true) {
            ScreenUtil.clear();
            DisplayHelper.printHeader("WELCOME TO LIBRARY MANAGEMENT SYSTEM");
            DisplayHelper.printSection("Login");

            String name = InputHelper.readString("Username");
            String password = InputHelper.readString("Password");

            User user = userService.login(name, password);

            if (user == null) {
                DisplayHelper.error("Invalid credentials");
                continue;
            }

            viewDashboard(user);
        }
    }

    // Route user to respective panel
    private void viewDashboard(User user) {

        if (user instanceof Student) {
            new StudentController((Student) user).start();
            return;
        }

        if (user instanceof Staff) {
            new StaffController((Staff) user).start();
        }
    }
}
