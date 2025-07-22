package handlers;

import storage.KVStorage;
import utils.StringUtils;

import java.util.List;

public class GetHandler implements RedisHandler {
    @Override
    public String handle(List<String> command) {
        KVStorage instance = KVStorage.getInstance();
        String s = instance.get(command.getFirst());
        return StringUtils.toRESPBulkString(s);
    }
}
