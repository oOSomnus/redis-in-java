package handlers.listHandlers.dataStructures;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author yihangz
 */
public class BlockingListQueue {
    private final Deque<String> deque = new ConcurrentLinkedDeque<>();
    /**
     * fifo semaphore
     */
    private final Semaphore permits = new Semaphore(0, true);

    /**
     * blocking left pop
     *
     * @param timeoutNanos time 2 wait
     * @return popped element | null
     * @throws InterruptedException
     */
    public String bLPop(long timeoutNanos) {
        try {
            if (timeoutNanos == 0) {
                permits.acquire();
            } else if (!permits.tryAcquire(timeoutNanos, TimeUnit.NANOSECONDS)) {
                return null;
            }
            return deque.pollFirst();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * non-blocking left pop
     */
    public String lPop() {
        if (!permits.tryAcquire()) {
            return null;
        }
        return deque.pollFirst();
    }

    /**
     * batch right insert
     *
     * @return result length
     */
    public int addAll(List<String> elements) {
        elements.forEach(deque::addLast);
        int size = permits.availablePermits() + elements.size();
        permits.release(elements.size());
        return size;
    }

    /**
     * batch add at specified index
     *
     * @param elements
     * @return
     */
    public int addFrontAll(List<String> elements) {
        elements.forEach(deque::addFirst);
        int size = permits.availablePermits() + elements.size();
        permits.release(elements.size());
        return size;
    }

    public int size() {
        return permits.availablePermits();
    }

    /**
     * snapshot
     */
    public List<String> getList() {
        return List.copyOf(deque);
    }

}
