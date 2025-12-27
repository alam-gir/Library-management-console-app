package util;

public class DisplayHelper {

    // Print application header
    public static void printHeader(String title) {
        System.out.println("========================================");
        System.out.println(" " + title);
        System.out.println("========================================");
    }

    // Print section title
    public static void printSection(String title) {
        System.out.println("----------------------------------------");
        System.out.println(" " + title);
        System.out.println("----------------------------------------");
    }

    // Print success message
    public static void success(String message) {
        System.out.println("[✔] " + message);
    }

    // Print error message
    public static void error(String message) {
        System.out.println("[✖] " + message);
    }

    // Print info message
    public static void info(String message) {
        System.out.println("[i] " + message);
    }

    // Print empty state
    public static void empty(String message) {
        System.out.println("[!] " + message);
    }
    
    // Print empty line
    public static void emptyLine() {
        System.out.println();
    }

    // 1) Basic → (bool, successMsg, failMsg)
    public static void result(boolean ok, String success, String fail){
        if(ok) success(success);
        else   error(fail);
    }

    // 2) Result (bool) with default messages
    public static void result(boolean ok){
        if(ok) success("Operation successful");
        else   error("Operation failed");
    }

    // 3) Custom result message only when success
    public static void resultSuccess(boolean ok, String successMsg){
        if(ok) success(successMsg);
        else   error("Failed");
    }

    // 4) Custom result message only when failed
    public static void resultFail(boolean ok, String failMsg){
        if(!ok) error(failMsg);
        else    success("Success");
    }
}
