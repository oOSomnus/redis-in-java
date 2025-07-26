package storage;

public enum StorageValueType {
    STRING("string"),
    LIST("list");

    private final String value;

    // 构造函数
    StorageValueType(String value) {
        this.value = value;
    }

    // 获取枚举值的方法
    public String getValue() {
        return value;
    }
}
