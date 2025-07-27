package handlers.listHandlers.dataStructures;

import utils.errors.RedisError;
import utils.errors.RedisErrorConstants;
import utils.errors.RedisResult;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RedisStream {
    SortedMap<StreamId, Map<String, String>> map;

    public RedisStream() {
        this.map = new TreeMap<>();
    }

    /* =================== Redis Stream Methods =================== */

    /**
     * verify id
     *
     * @param id
     * @return result
     */
    private synchronized RedisResult verifyId(String id) {
        try {
            StreamId inputId = new StreamId(id);
            if (inputId.compareTo(new StreamId("0-0")) <= 0) {
                return RedisResult.err(RedisError.getRedisError(RedisErrorConstants.STREAM_ID_EMPTY_ERROR));
            }
            StreamId largestId = getLargestId();
            if (inputId.compareTo(largestId) > 0) {
                return RedisResult.ok(null);
            }
            return RedisResult.err(RedisError.getRedisError(RedisErrorConstants.STREAM_ID_ERROR));

        } catch (IllegalArgumentException e) {
            return RedisResult.err(RedisError.getRedisError("Invalid Argument"));
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
    public synchronized RedisResult put(String id, Map<String, String> value) {
        RedisResult result = verifyId(id);
        if (!result.isOk()) {
            return result;
        }
        map.put(new StreamId(id), value);
        return RedisResult.ok(id);
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

        @Override
        public String toString() {
            return this.millis + "-" + this.seqs;
        }
    }

}
