package storage;

import java.util.HashMap;
import java.util.Map;

public class KVStorage {
    private static final KVStorage instance = new KVStorage();
    private Map<String, String> kvStore = new HashMap<>();

    private KVStorage() {
    }

    public static KVStorage getInstance() {
        return instance;
    }

    public void set(String key, String val) {
        kvStore.put(key, val);
    }

    public String get(String key) {
        return kvStore.get(key);
    }
}
