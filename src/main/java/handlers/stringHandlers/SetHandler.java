package handlers.stringHandlers;

import dispatcher.Registry.HandlerName;
import handlers.RedisHandler;
import storage.KVStorage;
import storage.StorageValue;
import storage.StorageValueType;
import utils.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @author yihangz
 */
@HandlerName("set")
public class SetHandler implements RedisHandler {
    @Override
    public String handle(List<String> arguments) {
        String k = arguments.get(0);
        String v = arguments.get(1);

        KVStorage storage = KVStorage.getInstance();
        if (storage.get(k) != null && storage.get(k).getStorageValueType() == StorageValueType.LIST) {
            return StringUtils.toRESPBulkString(null);
        }
        StorageValue sv = storage.get(k);
        if (sv == null) {
            boolean success = storage.initialize(k, StorageValueType.STRING);
            if (!success) {
                return StringUtils.toRESPBulkString(null);
            }
        }
        sv = storage.get(k);
        sv.setStringValue(v);
        if (arguments.size() > 2 && arguments.get(2).equalsIgnoreCase("px")) {
            int time2Live = Integer.parseInt(arguments.get(3));
            System.out.println("time to live: " + time2Live);
            sv.setExpiry(Instant.now().plus(time2Live, ChronoUnit.MILLIS));
        }
        return StringUtils.toRESPBulkString("OK");
    }
}
