package storage;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class KVStorage {
    private static final KVStorage instance = new KVStorage();
    private Map<String, StorageValue> kvStore = new HashMap<>();

    private KVStorage() {
    }

    public static KVStorage getInstance() {
        return instance;
    }

    public void set(String key, StorageValue sv) {
        kvStore.put(key, sv);
    }

    /**
     * get, return null when expired or doesn't exist
     * @param key
     * @return value
     */
    public String get(String key) {
        System.out.println("getting key: " + key);
        StorageValue storageValue = kvStore.get(key);
        if(storageValue == null){
            return null;
        }
        if(storageValue.exp != null && storageValue.exp.isBefore(Instant.now())){
            System.out.println("expired! InstantNow: " + Instant.now().toString() + "Exp: " + storageValue.exp.toString());
            return null;
        }
        return storageValue.value;

    }
}
