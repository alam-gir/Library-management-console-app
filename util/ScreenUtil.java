package util;

public class ScreenUtil {

    // Clear terminal screen (best effort)
    public static void clear() {
        for (int i = 0; i < 10; i++) {
            System.out.println();
        }
    }

    // Pause screen until user presses Enter
    public static void pause() {
        System.out.println();
        System.out.print("Press Enter to continue...");
        InputHelper.inputNextLine();
    }
}
