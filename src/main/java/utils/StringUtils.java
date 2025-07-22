package utils;

public class StringUtils {
    public static String toSimpleString(String input) {
        return "+" + input + "\r\n";
    }

    public static String toRESPBulkString(String input) {
        return "$" + input.length() + "\r\n" + input + "\r\n";
    }
}
