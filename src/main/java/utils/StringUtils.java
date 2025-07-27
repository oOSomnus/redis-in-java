package utils;

import errors.RedisError;

import java.util.List;

/**
 * @author yihangz
 */
public class StringUtils {
    public static String toSimpleString(String input) {
        return "+" + input + "\r\n";
    }

    public static String toRESPBulkString(String input) {
        if (input == null) {
            System.out.println("return null bulk string");
            return "$-1\r\n";
        }
        return "$" + input.length() + "\r\n" + input + "\r\n";
    }

    public static String toRESPList(List<String> input) {
        String output = "*" + input.size() + "\r\n";
        for (String s : input) {
            output = output + toRESPBulkString(s);
        }
        return output;
    }

    public static String toRESPInteger(Integer input) {
        return ":" + input.toString() + "\r\n";
    }

    public static String toSimpleError(RedisError error) {
        return "-" + error.getMessage() + "\r\n";
    }
}
