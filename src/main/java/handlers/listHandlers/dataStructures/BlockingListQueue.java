package handlers.listHandlers.dataStructures;

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

    public String bLPop(int timeout) {
        try {
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
                        condition.await();
                        notifyLock.unlock();
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

    private boolean bLPopGetLock(int timeout) {
        if (timeout == 0) {
            blockingLock.lock();
            return true;
        } else {
            try {
                return blockingLock.tryLock(timeout, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public Integer addAll(List<String> elements) {
        listLock.lock();
        list.addAll(elements);
        int currSize = list.size();
        listLock.unlock();
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
        notifyLock.unlock();
    }

}
