package handlers;

import storage.KVStorage;
import storage.StorageValue;
import utils.StringUtils;

import java.util.List;

public class GetHandler implements RedisHandler {
    @Override
    public String handle(List<String> arguments) {
        KVStorage instance = KVStorage.getInstance();
        StorageValue sv = instance.get(arguments.getFirst());
        if(sv == null){
            return StringUtils.toRESPBulkString(null);
        }
        System.out.println("handle get: s="+ sv.getStringValue());
        return StringUtils.toRESPBulkString(sv.getStringValue());
    }
}
