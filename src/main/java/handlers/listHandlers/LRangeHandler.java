package handlers.listHandlers;

import dispatcher.Registry.HandlerName;
import handlers.RedisHandler;
import storage.KVStorage;
import storage.StorageValue;
import storage.StorageValueType;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
/**
 * @author yihangz
 */
@HandlerName("lrange")
public class LRangeHandler implements RedisHandler {
    @Override
    public String handle(List<String> arguments) {
        if (arguments.size() != 3) {
            System.out.println("LRangeHandler|Invalid number of arguments");
            return null;
        }
        String key = arguments.get(0);
        int start = Integer.parseInt(arguments.get(1));
        int end = Integer.parseInt(arguments.get(2));
        KVStorage storage = KVStorage.getInstance();
        StorageValue sv = storage.get(key);
        if(sv == null || sv.getStorageValueType() != StorageValueType.LIST){
            return StringUtils.toRESPList(new ArrayList<>());
        }
        List<String> currList = sv.getListValue();
        if(currList == null){
            return StringUtils.toRESPList(new ArrayList<>());
        }
        start = transferNegativeIndex(start,currList.size());
        end = transferNegativeIndex(end,currList.size());
        if(start > end || start >= currList.size()){
            return StringUtils.toRESPList(new ArrayList<>());
        }
        if(end >= currList.size()){
            end = currList.size()-1;
        }
        return StringUtils.toRESPList(currList.subList(start, end+1));
    }

    private int transferNegativeIndex(int index, int size){
        if(index >= 0) {
            return index;
        }
        int pos = index+size;
        return Math.max(pos, 0);
    }
}
