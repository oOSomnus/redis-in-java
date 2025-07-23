package utils;

public class StringUtils {
    public static String toSimpleString(String input) {
        return "+" + input + "\r\n";
    }

    public static String toRESPBulkString(String input) {
        if (input == null){
            System.out.println("return null bulk string");
            return "$-1\r\n";
        }
        return "$" + input.length() + "\r\n" + input + "\r\n";
    }
}
