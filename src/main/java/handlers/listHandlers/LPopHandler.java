package handlers.listHandlers;

import dispatcher.Registry.HandlerName;
import handlers.RedisHandler;
import storage.KVStorage;
import storage.StorageValue;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

@HandlerName("lpop")
public class LPopHandler implements RedisHandler {
    @Override
    public String handle(List<String> arguments) {
        if (arguments.size() < 1) {
            System.out.println("LPopHandler|arg num wrong");
            return null;
        }
        int num2pop = 1;
        if (arguments.size() > 1) {
            num2pop = Integer.parseInt(arguments.get(1));
        }
        KVStorage storage = KVStorage.getInstance();
        StorageValue sv = storage.get(arguments.getFirst());
        if (sv == null) {
            return StringUtils.toRESPBulkString(null);
        }
        List<String> svList = new ArrayList<>();
        for (int i = 0; i < num2pop; i++) {
            String s = sv.lPopElementFromList();
            if (s == null) {
                break;
            }
            svList.add(s);
        }
        if (svList.isEmpty()) {
            return StringUtils.toRESPBulkString(null);
        }
        if (arguments.size() == 1) {
            return StringUtils.toRESPBulkString(svList.getFirst());
        }
        return StringUtils.toRESPList(svList);
    }
}
