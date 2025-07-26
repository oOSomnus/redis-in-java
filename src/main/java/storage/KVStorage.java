package storage;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yihangz
 */
public class KVStorage {
    private static final KVStorage instance = new KVStorage();
    private final Map<String, StorageValue> kvStore = new HashMap<>();

    private KVStorage() {
    }

    public static KVStorage getInstance() {
        return instance;
    }

    public synchronized boolean initialize(String key, StorageValueType svt) {
        if (instance.get(key) != null && instance.get(key).getStorageValueType() != svt) {
            return false;
        }
        if (instance.get(key) == null) {
            kvStore.put(key, new StorageValue(svt));
        }
        return true;
    }

    /**
     * get, return null when expired or doesn't exist
     *
     * @param key
     * @return value
     */
    public synchronized StorageValue get(String key) {
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

    /**
     * get key, if not exists, create; if type incorrect, get null
     *
     * @param key key
     * @param svt expected type
     * @return null | sv
     */
    public synchronized StorageValue getWithExpectedType(String key, StorageValueType svt) {
        if (this.get(key) == null) {
            boolean success = this.initialize(key, svt);
            if (!success) {
                return null;
            }
        }
        return this.get(key);
    }
}
