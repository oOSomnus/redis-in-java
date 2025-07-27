package handlers.streamHandlers;

import dispatcher.Registry.HandlerName;
import errors.RedisResult;
import handlers.RedisHandler;
import storage.KVStorage;
import storage.StorageValue;
import storage.StorageValueType;
import utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@HandlerName("xadd")
public class XAddHandler implements RedisHandler {
    @Override
    public String handle(List<String> arguments) {
        if (arguments.size() < 2) {
            return null;
        }
        String key = arguments.getFirst();
        String id = arguments.get(1);
        Map<String, String> tmpMap = new HashMap<String, String>();
        for (int i = 2; i < arguments.size(); i += 2) {
            String k = arguments.get(i);
            String v = arguments.get(i + 1);
            tmpMap.put(k, v);
        }
        StorageValue sv = KVStorage.getInstance().getWithExpectedType(key, StorageValueType.STREAM);
        if (sv == null) {
            return StringUtils.toRESPBulkString(null);
        }
        RedisResult result = sv.putStream(id, tmpMap);
        if (!result.isOk()) {
            return StringUtils.toSimpleError(result.getError());
        }
        return StringUtils.toRESPBulkString(result.getValue());
    }
}
