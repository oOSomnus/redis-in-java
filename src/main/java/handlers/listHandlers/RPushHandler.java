package handlers.listHandlers;

import handlers.RedisHandler;
import storage.KVStorage;
import storage.StorageValue;
import storage.StorageValueType;
import utils.StringUtils;

import java.util.List;

public class RPushHandler implements RedisHandler {
    @Override
    public String handle(List<String> arguments) {
        String k = arguments.get(0);
        String v = arguments.get(1);
        KVStorage storage = KVStorage.getInstance();
        StorageValue sv = storage.get(k);
        if(sv != null && sv.getStorageValueType() != StorageValueType.LIST){
            System.out.println("Type is not a list");
            return null;
        }
        if(sv == null){
            StorageValue finalSv = new StorageValue();
            Integer size = finalSv.setList(v);
            storage.set(k, finalSv);
            return StringUtils.toRESPInteger(size);
        }
            Integer size = sv.setList(v);
            return StringUtils.toRESPInteger(size);
    }
}
