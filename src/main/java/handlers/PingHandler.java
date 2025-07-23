package handlers;

import utils.StringUtils;

import java.util.List;

public class PingHandler implements RedisHandler {
    @Override
    public String handle(List<String> arguments) {
        return StringUtils.toSimpleString("PONG");
    }
}
