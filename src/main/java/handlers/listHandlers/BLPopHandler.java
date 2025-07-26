package handlers.listHandlers;

import dispatcher.Registry.HandlerName;
import handlers.RedisHandler;
import storage.KVStorage;
import storage.StorageValue;
import storage.StorageValueType;
import utils.StringUtils;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@HandlerName("blpop")
public class BLPopHandler implements RedisHandler {

    private final ReentrantLock lock = new ReentrantLock(true);

    @Override
    public String handle(List<String> arguments) {
        if (arguments.size() != 2) {
            System.out.println("BLPopHandler|Invalid number of arguments");
            return null;
        }
        long time2wait = Math.round(Double.parseDouble(arguments.get(1)) * Math.pow(10, 9));
        String key = arguments.get(0);
        KVStorage storage = KVStorage.getInstance();
        StorageValue sv;
        lock.lock();
        sv = storage.get(key);
        if (sv == null) {
            boolean success = storage.initialize(key, StorageValueType.LIST);
            if (!success) {
                return null;
            }
            sv = storage.get(key);
        }
        lock.unlock();
        String result = sv.bLPopElement(time2wait);
        System.out.println("BLPopHandler|result=" + result);
        if (result == null) {
            System.out.println("BLPopHandler|Invalid result");
            return StringUtils.toRESPBulkString(null);
        }
        System.out.println("BLPopHandler|BLPop result: " + result);
        String finalResult = StringUtils.toRESPList(List.of(key, result));
        System.out.println("BLPopHandler|finalResult=" + finalResult);
        return finalResult;
    }
}
