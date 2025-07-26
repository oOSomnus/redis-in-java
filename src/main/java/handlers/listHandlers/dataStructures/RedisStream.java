package handlers.listHandlers.dataStructures;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class RedisStream {
    Map<String, Map<String, String>> map;

    public RedisStream() {
        this.map = new ConcurrentSkipListMap<>();
    }

    public void put(String id, Map<String, String> value) {
        map.put(id, value);
    }
}
