package handlers.listHandlers;

import handlers.RedisHandler;
import storage.KVStorage;
import storage.StorageValue;
import storage.StorageValueType;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

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
        if(start > end || sv == null || sv.getStorageValueType() != StorageValueType.LIST){
            return StringUtils.toRESPList(new ArrayList<>());
        }
        List<String> currList = sv.getListValue();
        if(currList == null || start >= currList.size()){
            return StringUtils.toRESPList(new ArrayList<>());
        }
        if(end >= currList.size()){
            end = currList.size()-1;
        }
        return StringUtils.toRESPList(currList.subList(start, end+1));
    }
}
