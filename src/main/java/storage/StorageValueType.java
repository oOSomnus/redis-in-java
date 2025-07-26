package storage;

public enum StorageValueType {
    STRING("string"),
    LIST("list"),
    STREAM("stream");
    private final String value;

    StorageValueType(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}
