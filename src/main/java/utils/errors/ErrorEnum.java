package utils.errors;

public enum ErrorEnum {
    INVALID_STREAM_ID("ERR The ID specified in XADD is equal or smaller than the target stream top item");

    private final String value;

    ErrorEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
