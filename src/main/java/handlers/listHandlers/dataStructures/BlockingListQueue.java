package handlers.listHandlers.dataStructures;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingListQueue {
    private final List<String> list;
    private final Lock listLock = new ReentrantLock();
    private final Lock blockingLock = new ReentrantLock(true);
    private final Lock notifyLock = new ReentrantLock();
    private final Condition condition = notifyLock.newCondition();

    public BlockingListQueue() {
        list = new LinkedList<>();
    }

    public String bLPop(long timeout) {
        try {
            int nano = Instant.now().getNano();
            long terminalTime = timeout + nano;
            System.out.println("currentNano: " + nano + " terminalTime: " + terminalTime);
            if (bLPopGetLock(timeout)) {
                System.out.println("bLPop| got the blocking lock");
                String val = null;
                while (val == null) {
                    System.out.println("bLPop| val is null, checking the list");
                    if (!list.isEmpty()) {
                        listLock.lock();
                        System.out.println("bLPop| list isn't empty");
                        if (!list.isEmpty()) {
                            val = list.removeFirst();
                        }
                        System.out.println("bLPop| got the value");
                        listLock.unlock();
                    } else {
                        System.out.println("bLPop| list is empty, waiting...");
                        notifyLock.lock();
                        long remain = Long.MAX_VALUE;
                        if (timeout == 0) {
                            System.out.println("bLPop| infinite waiting...");
                            condition.await();
                        } else {
                            System.out.println("bLPop| finite waiting...");
                            remain = condition.awaitNanos(terminalTime - Instant.now().getNano());
                            System.out.println("bLPop| remain: " + remain);
                        }
                        notifyLock.unlock();
                        if (timeout != 0 && remain <= 0) {
                            System.out.println("bLPop| terminal time reached");
                            blockingLock.unlock();
                            return null;
                        }
                        System.out.println("bLPop| return from wait");
                    }
                }
                blockingLock.unlock();
                System.out.println("bLPop| return value " + val);
                return val;
            } else {
                return null;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean bLPopGetLock(long timeout) {
        if (timeout == 0) {
            blockingLock.lock();
            return true;
        } else {
            try {
                return blockingLock.tryLock(timeout, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public Integer addAll(List<String> elements) {
        System.out.println("addAll| getting lock...");
        listLock.lock();
        list.addAll(elements);
        System.out.println("addAll| add completed");
        int currSize = list.size();
        listLock.unlock();
        System.out.println("addAll| lock released");
        notifyBlockingThread();
        return currSize;
    }

    public Integer addAll(Integer pos, List<String> elements) {
        listLock.lock();
        list.addAll(pos, elements);
        int currSize = list.size();
        listLock.unlock();
        notifyBlockingThread();
        return currSize;
    }

    public String lPop() {
        listLock.lock();
        String val = null;
        if (!list.isEmpty()) {
            val = list.removeFirst();
        }
        listLock.unlock();
        return val;
    }

    public List<String> getList() {
        return Collections.unmodifiableList(list);
    }

    public Integer size() {
        return list.size();
    }

    private void notifyBlockingThread() {
        notifyLock.lock();
        condition.signal();
        System.out.println("notifyBlockingThread| notified");
        notifyLock.unlock();
    }

}
