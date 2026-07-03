package util;

import java.util.Scanner;

/**
 * Utility class for reading validated input from the console.
 */
public class Menu {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Reads an integer within the range [min, max] from the user.
     */
    public static int readInt(String prompt, int min, int max) {
        int val;
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                val = Integer.parseInt(input);
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.format("Lỗi: Giá trị nhập phải nằm trong khoảng từ %d đến %d!\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Vui lòng nhập số nguyên hợp lệ!");
            }
        }
    }

    /**
     * Reads a double within the range [min, max] from the user.
     */
    public static double readDouble(String prompt, double min, double max) {
        double val;
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                val = Double.parseDouble(input);
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.format("Lỗi: Giá trị nhập phải nằm trong khoảng từ %,.1f đến %,.1f!\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Vui lòng nhập số thực hợp lệ!");
            }
        }
    }

    /**
     * Reads a string matching a specific regular expression.
     */
    public static String readString(String prompt, String pattern, String errorMsg) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.matches(pattern)) {
                return input;
            }
            System.out.println("❌ Lỗi: " + errorMsg);
        }
    }

    /**
     * Reads a non-empty string.
     */
    public static String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("❌ Lỗi: Giá trị không được để trống!");
        }
    }
}
