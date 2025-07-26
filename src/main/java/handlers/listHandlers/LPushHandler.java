package handlers.listHandlers;

import dispatcher.Registry.HandlerName;
import handlers.RedisHandler;
import storage.KVStorage;
import storage.StorageValue;
import storage.StorageValueType;
import utils.StringUtils;

import java.util.List;

/**
 * @author yihangz
 */
@HandlerName("lpush")
public class LPushHandler implements RedisHandler {
    @Override
    public String handle(List<String> arguments) {
        String k = arguments.getFirst();
        List<String> listElements = arguments.subList(1, arguments.size());
        KVStorage storage = KVStorage.getInstance();
        StorageValue currSv = storage.get(k);
        if (currSv != null && currSv.getStorageValueType() != StorageValueType.LIST) {
            System.out.println("Type is not a list");
            return null;
        }
        currSv = storage.getWithExpectedType(k, StorageValueType.LIST);
        if (currSv == null) {
            return StringUtils.toRESPBulkString(null);
        }
        Integer size = currSv.lPushElementsToList(listElements);
        return StringUtils.toRESPInteger(size);
    }
}
