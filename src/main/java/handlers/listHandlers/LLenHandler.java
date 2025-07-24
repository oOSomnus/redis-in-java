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
@HandlerName("llen")
public class LLenHandler implements RedisHandler {
    @Override
    public String handle(List<String> arguments) {
        if(arguments.size() != 1){
            System.out.println("LLenHandler|arg size wrong");
            return null;
        }
        KVStorage storage = KVStorage.getInstance();
        StorageValue sv = storage.get(arguments.getFirst());
        if(sv == null || sv.getStorageValueType() != StorageValueType.LIST){
            return StringUtils.toRESPInteger(0);
        }
        return StringUtils.toRESPInteger(sv.getListValue().size());
    }
}
