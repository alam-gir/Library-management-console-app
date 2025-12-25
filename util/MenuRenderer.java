package util;

public class MenuRenderer {

    // Render menu with numbered options
    public static void show(String title, String... options) {

        DisplayHelper.printSection(title);

        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }

        System.out.println();
    }
}
