package handlers.listHandlers;

import handlers.RedisHandler;
import storage.KVStorage;
import storage.StorageValue;
import utils.StringUtils;

import java.util.List;

public class LPopHandler implements RedisHandler {
    @Override
    public String handle(List<String> arguments) {
        if(arguments.size() != 1) {
            System.out.println("LPopHandler|arg num wrong");
            return null;
        }
        KVStorage storage = KVStorage.getInstance();
        StorageValue sv = storage.get(arguments.getFirst());
        if(sv == null) {
            return StringUtils.toRESPBulkString(null);
        }
        return StringUtils.toRESPBulkString(sv.lPopElementFromList());
    }
}
