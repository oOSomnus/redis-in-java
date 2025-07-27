package handlers.listHandlers.dataStructures;

import errors.RedisError;
import errors.RedisErrorConstants;
import errors.RedisResult;

import java.util.AbstractMap;
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
        String autoId = autoGenerateId(id);
        RedisResult result = verifyId(autoId);
        if (!result.isOk()) {
            return result;
        }
        map.put(new StreamId(autoId), value);
        return RedisResult.ok(autoId);
    }

    private Map.Entry<String, String> separateByDash(String id) {
        int index = id.indexOf('-');
        if (index == -1 || index != id.lastIndexOf('-')) {
            throw new IllegalArgumentException("Invalid stream id: " + id);
        }
        String first = id.substring(0, index);
        String second = id.substring(index + 1);
        return new AbstractMap.SimpleEntry<>(first, second);
    }

    /**
     * auto-generate id; remain same if not need
     *
     * @param id
     * @return
     */
    private synchronized String autoGenerateId(String id) {
        Map.Entry<String, String> entry = separateByDash(id);
        String millis = entry.getKey();
        String seqs = entry.getValue();
        if (seqs.equals("*")) {
            return millis + "-" + autoGenerateSeqs(millis);
        } else {
            return id;
        }
    }

    /**
     * generate seqs based on millis
     *
     * @param millis
     * @return auto genned seqs
     */
    private String autoGenerateSeqs(String millis) {
        long longMillis = Long.parseLong(millis);
        String upperMillis = String.valueOf((longMillis + 1));
        StreamId upperId = new StreamId(upperMillis + "-0");
        SortedMap<StreamId, Map<String, String>> headMap = this.map.headMap(upperId);
        String result;
        if (!headMap.isEmpty()) {
            StreamId lastId = headMap.lastKey();
            if (lastId.getMillis() != longMillis) {
                result = "0";
            } else {
                result = Long.toString(lastId.getSeqs() + 1);
            }
        } else {
            if ("0".equals(millis)) {
                result = "1";
            } else {
                result = "0";
            }
        }
        return result;
    }

    /* =================== Stream Id =================== */
    private class StreamId implements Comparable<StreamId> {
        private final Long millis;
        private final Long seqs;

        public StreamId(String id) {
            try {
                Map.Entry<String, String> parsedRes = separateByDash(id);
                long millis = Long.parseLong(parsedRes.getKey());
                long seqs = Long.parseLong(parsedRes.getValue());
                this.millis = millis;
                this.seqs = seqs;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("Invalid stream id: " + id);
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

        public Long getMillis() {
            return this.millis;
        }

        public Long getSeqs() {
            return this.seqs;
        }
    }

}
