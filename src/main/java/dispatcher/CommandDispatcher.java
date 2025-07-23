package dispatcher;

import handlers.*;
import handlers.listHandlers.RPushHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandDispatcher {
    private static final Map<String, RedisHandler> handlerMapping;

    static{
        Map<String, RedisHandler> initMapping = new HashMap<>();
        initMapping.put("echo", new EchoHandler());
        initMapping.put("get", new GetHandler());
        initMapping.put("set", new SetHandler());
        initMapping.put("ping", new PingHandler());
        initMapping.put("rpush", new RPushHandler());
        handlerMapping = initMapping;
    }

    public static String dispatch(List<String> commandList){
        if(commandList == null || commandList.isEmpty()){
            return null;
        }
        String command = commandList.getFirst().toLowerCase();
        if(handlerMapping.containsKey(command)) {
            return handlerMapping.get(command).handle(commandList.subList(1, commandList.size()));
        }else{
            System.out.println("Unknown command: " + command);
            return null;
        }
    }
}
