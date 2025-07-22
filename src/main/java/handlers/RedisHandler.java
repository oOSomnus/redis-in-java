package handlers;

import java.util.List;

public interface RedisHandler {
    String handle(List<String> command);
}
