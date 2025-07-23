package handlers;

import storage.KVStorage;
import utils.StringUtils;

import java.util.List;

public class GetHandler implements RedisHandler {
    @Override
    public String handle(List<String> arguments) {
        KVStorage instance = KVStorage.getInstance();
        String s = instance.get(arguments.getFirst()).getStringValue();
        System.out.println("handle get: s="+ s);
        return StringUtils.toRESPBulkString(s);
    }
}
