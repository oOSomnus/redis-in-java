package handlers;

import utils.StringUtils;

import java.util.List;

public class EchoHandler implements RedisHandler {
    @Override
    public String handle(List<String> command) {
        return StringUtils.toRESPBulkString(command.getFirst());
    }
}
