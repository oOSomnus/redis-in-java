package dispatcher;

import dispatcher.Registry.HandlerRegistry;
import handlers.RedisHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandDispatcher {
    private static final Map<String, RedisHandler> HANDLER_MAPPING;

    static {
        Map<String, RedisHandler> initMapping = new HashMap<>();
        HandlerRegistry reg = new HandlerRegistry();
        reg.scanAndRegister("handlers");
        Map<String, Object> objectMap = reg.getHandlerMap();
        for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
            String key = entry.getKey();
            RedisHandler value = (RedisHandler) entry.getValue();
            initMapping.put(key, value);
        }
        HANDLER_MAPPING = initMapping;
    }

    public static String dispatch(List<String> commandList) {
        if (commandList == null || commandList.isEmpty()) {
            return null;
        }
        String command = commandList.getFirst().toLowerCase();
        if (HANDLER_MAPPING.containsKey(command)) {
            return HANDLER_MAPPING.get(command).handle(commandList.subList(1, commandList.size()));
        } else {
            System.out.println("Unknown command: " + command);
            return null;
        }
    }
}
