package handlers.basicHandlers;

import dispatcher.Registry.HandlerName;
import handlers.RedisHandler;
import utils.StringUtils;

import java.util.List;

/**
 * @author yihangz
 */
@HandlerName("ping")
public class PingHandler implements RedisHandler {
    @Override
    public String handle(List<String> arguments) {
        return StringUtils.toSimpleString("PONG");
    }
}
