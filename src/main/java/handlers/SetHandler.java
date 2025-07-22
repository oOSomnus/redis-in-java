package handlers;

import storage.KVStorage;
import utils.StringUtils;

import java.util.List;

public class SetHandler implements RedisHandler {
    @Override
    public String handle(List<String> command) {
        String k = command.get(0);
        String v = command.get(1);
        KVStorage.getInstance().set(k, v);
        return StringUtils.toRESPBulkString("OK");
    }
}
