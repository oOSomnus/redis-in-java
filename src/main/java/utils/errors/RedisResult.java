package utils.errors;

public class RedisResult {
    private final String value;
    private final RedisError error;
    private final boolean isOk;

    private RedisResult(String value, RedisError error, boolean isOk) {
        this.value = value;
        this.error = error;
        this.isOk = isOk;
    }

    public static RedisResult ok(String value) {
        return new RedisResult(value, null, true);
    }

    public static RedisResult err(RedisError error) {
        return new RedisResult(null, error, false);
    }

    public boolean isOk() {
        return isOk;
    }

    public String getValue() {
        return value;
    }

    public RedisError getError() {
        return error;
    }
}
