package storage;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yihangz
 */
public class KVStorage {
    private static final KVStorage instance = new KVStorage();
    private final Map<String, StorageValue> kvStore = new ConcurrentHashMap<>();

    private KVStorage() {
    }

    public static KVStorage getInstance() {
        return instance;
    }

    public StorageValue set(String key, StorageValue sv) {
        return kvStore.put(key, sv);
    }

    /**
     * get, return null when expired or doesn't exist
     *
     * @param key
     * @return value
     */
    public StorageValue get(String key) {
        System.out.println("getting key: " + key);
        StorageValue storageValue = kvStore.get(key);
        if (storageValue == null) {
            return null;
        }
        if (storageValue.getExpiry() != null && storageValue.getExpiry().isBefore(Instant.now())) {
            System.out.println("expired! InstantNow: " + Instant.now().toString() + "Exp: " + storageValue.getExpiry().toString());
            return null;
        }
        return storageValue;

    }
}
