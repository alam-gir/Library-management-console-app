package util;

import java.util.ArrayList;
import java.util.List;

public class MenuRenderer {

    // Render menu with numbered options
    public static void show(String title, String... options) {

        DisplayHelper.printSection(title);

        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }

        System.out.println();
    }

    public static int dynamic(String title, String... options) {

        DisplayHelper.printSection(title);

        List<String> visible = new ArrayList<>();
        for (String op : options) {
            if (op != null && !op.isBlank()) visible.add(op);
        }

        for (int i = 0; i < visible.size(); i++) {
            System.out.println((i + 1) + ". " + visible.get(i));
        }

        System.out.println();
        return InputHelper.readInt("Choose option", 1, visible.size());
    }
}
