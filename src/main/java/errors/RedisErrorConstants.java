package errors;

public class RedisErrorConstants {
    public static final String STREAM_ID_EMPTY_ERROR = "ERR The ID specified in XADD must be greater than 0-0";
    public static final String STREAM_ID_ERROR = "ERR The ID specified in XADD is equal or smaller than the target stream top item";
}
