package handlers.basicHandlers;

import dispatcher.Registry.HandlerName;
import handlers.RedisHandler;
import storage.KVStorage;
import storage.StorageValue;
import utils.StringUtils;

import java.util.List;

@HandlerName("type")
public class TypeHandler implements RedisHandler {
    @Override
    public String handle(List<String> arguments) {
        if (arguments.size() != 1) {
            return StringUtils.toRESPBulkString(null);
        }
        String key = arguments.get(0);
        StorageValue sv = KVStorage.getInstance().get(key);
        if (sv == null) {
            return StringUtils.toSimpleString("none");
        }
        return StringUtils.toSimpleString(sv.getStorageValueType().getValue());
    }
}
