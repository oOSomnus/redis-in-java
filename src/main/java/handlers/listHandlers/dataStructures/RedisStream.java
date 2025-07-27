package handlers.listHandlers.dataStructures;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RedisStream {
    SortedMap<StreamId, Map<String, String>> map;

    public RedisStream() {
        this.map = new TreeMap<>();
    }

    /* =================== Redis Stream Methods =================== */

    private synchronized boolean verifyId(String id) {
        try {
            StreamId inputId = new StreamId(id);
            StreamId largestId = getLargestId();
            return inputId.compareTo(largestId) > 0;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private synchronized StreamId getLargestId() {
        if (this.map.isEmpty()) {
            return new StreamId("0-0");
        } else {
            return this.map.lastKey();
        }
    }

    /**
     * put into map
     *
     * @param id
     * @param value
     * @return success
     */
    public synchronized boolean put(String id, Map<String, String> value) {
        if (!verifyId(id)) {
            return false;
        }
        map.put(new StreamId(id), value);
        return true;
    }


    /* =================== Stream Id =================== */
    private class StreamId implements Comparable<StreamId> {
        private final Long millis;
        private final Long seqs;

        public StreamId(String rawId) {
            int index = rawId.indexOf('-');
            if (index == -1 || index != rawId.lastIndexOf('-')) {
                throw new IllegalArgumentException("Invalid stream id: " + rawId);
            }
            try {
                long millis = Long.parseLong(rawId.substring(0, index));
                long seqs = Long.parseLong(rawId.substring(index + 1));
                this.millis = millis;
                this.seqs = seqs;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("Invalid stream id: " + rawId);
            }
        }

        @Override
        public int compareTo(StreamId id) {
            int cmp = Long.compare(this.millis, id.millis);
            if (cmp != 0) return cmp;
            return Long.compare(this.seqs, id.seqs);
        }
        
    }

}
