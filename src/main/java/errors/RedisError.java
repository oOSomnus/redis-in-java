package errors;

public class RedisError extends Exception {
    private final String message;

    private RedisError(String message) {
        this.message = message;
    }

    public static RedisError getRedisError(String message) {
        return new RedisError(message);
    }

    public String getMessage() {
        return this.message;
    }
}
