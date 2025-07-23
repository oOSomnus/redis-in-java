package handlers;

import storage.KVStorage;
import storage.StorageValue;
import utils.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class SetHandler implements RedisHandler {
    @Override
    public String handle(List<String> arguments) {
        String k = arguments.get(0);
        String v = arguments.get(1);
        StorageValue storageValue = new StorageValue(v);
        if(arguments.size() > 2 && arguments.get(2).equalsIgnoreCase("px")) {
            int time2Live = Integer.parseInt(arguments.get(3));
            System.out.println("time to live: " + time2Live);
            storageValue.exp = Instant.now().plus(time2Live, ChronoUnit.MILLIS);
        }
        KVStorage.getInstance().set(k, storageValue);
        return StringUtils.toRESPBulkString("OK");
    }
}
