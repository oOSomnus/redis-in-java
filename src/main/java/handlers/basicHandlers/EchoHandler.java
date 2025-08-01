package handlers.basicHandlers;

import dispatcher.Registry.HandlerName;
import handlers.RedisHandler;
import utils.StringUtils;

import java.util.List;

/**
 * @author yihangz
 */
@HandlerName("echo")
public class EchoHandler implements RedisHandler {
    @Override
    public String handle(List<String> arguments) {
        return StringUtils.toRESPBulkString(arguments.getFirst());
    }
}
