package util;

import java.util.ArrayList;
import java.util.List;

public class TableRenderer {

    private static final int MAX_COL_WIDTH = 30;
    private static final int PADDING = 1;

    // Render table with headers and rows
    public static void render(String[] headers, List<String[]> rows) {

        if (headers == null || headers.length == 0) {
            DisplayHelper.empty("No table headers defined");
            return;
        }

        List<String[]> safeRows = rows == null ? new ArrayList<>() : rows;

        int[] colWidths = calculateColumnWidths(headers, safeRows);

        printBorder(colWidths);
        printRow(headers, colWidths);
        printBorder(colWidths);

        if (safeRows.isEmpty()) {
            printEmptyRow(colWidths);
        } else {
            for (String[] row : safeRows) {
                printRow(row, colWidths);
            }
        }

        printBorder(colWidths);
    }

    // Calculate width for each column
    private static int[] calculateColumnWidths(String[] headers, List<String[]> rows) {

        int colCount = headers.length;
        int[] widths = new int[colCount];

        for (int i = 0; i < colCount; i++) {
            widths[i] = Math.min(headers[i].length(), MAX_COL_WIDTH);
        }

        for (String[] row : rows) {
            for (int i = 0; i < colCount && i < row.length; i++) {
                int cellLength = row[i] == null ? 0 : row[i].length();
                widths[i] = Math.min(
                        Math.max(widths[i], cellLength),
                        MAX_COL_WIDTH
                );
            }
        }

        return widths;
    }

    // Print table border
    private static void printBorder(int[] colWidths) {

        StringBuilder sb = new StringBuilder();
        sb.append("+");

        for (int width : colWidths) {
            sb.append("-".repeat(width + (PADDING * 2)));
            sb.append("+");
        }

        System.out.println(sb);
    }

    // Print single row
    private static void printRow(String[] cells, int[] colWidths) {

        StringBuilder sb = new StringBuilder();
        sb.append("|");

        for (int i = 0; i < colWidths.length; i++) {

            String cell = "";
            if (cells != null && i < cells.length && cells[i] != null) {
                cell = truncate(cells[i], colWidths[i]);
            }

            sb.append(" ".repeat(PADDING));
            sb.append(padRight(cell, colWidths[i]));
            sb.append(" ".repeat(PADDING));
            sb.append("|");
        }

        System.out.println(sb);
    }

    // Print empty row message
    private static void printEmptyRow(int[] colWidths) {

        String message = "No records found";
        int totalWidth = 0;

        for (int w : colWidths) {
            totalWidth += w + (PADDING * 2) + 1;
        }

        totalWidth -= 1;

        String text = truncate(message, totalWidth - 2);

        System.out.println("| " + padRight(text, totalWidth - 2) + "|");
    }

    // Truncate long text
    private static String truncate(String text, int maxWidth) {

        if (text.length() <= maxWidth) {
            return text;
        }

        if (maxWidth <= 3) {
            return "...";
        }

        return text.substring(0, maxWidth - 3) + "...";
    }

    // Right pad text
    private static String padRight(String text, int width) {
        return String.format("%-" + width + "s", text);
    }
}
