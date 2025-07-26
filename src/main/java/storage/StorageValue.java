package storage;

import handlers.listHandlers.dataStructures.BlockingListQueue;

import java.time.Instant;
import java.util.List;

/**
 * @author yihangz
 */
public class StorageValue {
    private final BlockingListQueue listVal;
    private final StorageValueType storageValueType;
    private String stringVal;
    private Instant exp; // null for unexp data

    public StorageValue(StorageValueType sv) {
        this.listVal = new BlockingListQueue();
        this.storageValueType = sv;
    }

    public StorageValueType getStorageValueType() {
        return storageValueType;
    }

    public String getStringValue() {
        return stringVal;
    }

    public void setStringValue(String value) {
        this.stringVal = value;
        this.exp = null;
    }

    public Instant getExpiry() {
        return exp;
    }

    public void setExpiry(Instant exp) {
        this.exp = exp;
    }

    public Integer pushElementsToList(List<String> elements) {
        if (this.storageValueType == StorageValueType.LIST) {
            return this.listVal.addAll(elements);
        } else {
            return null;
        }
    }

    public Integer lPushElementsToList(List<String> elements) {
        if (this.storageValueType == StorageValueType.LIST) {
            List<String> reversedElements = elements.reversed();
            return this.listVal.addAll(0, reversedElements);
        } else {
            return null;
        }
    }

    /**
     * left pop from list
     *
     * @return leftmost element, or null when not list or empty
     */
    public String lPopElementFromList() {
        if (this.storageValueType == StorageValueType.LIST) {
            return this.listVal.lPop();
        } else {
            return null;
        }
    }

    /**
     * get list value
     *
     * @return list value | null when type is not list or list isn't initialized
     */
    public List<String> getListValue() {
        if (this.storageValueType == StorageValueType.LIST) {
            return this.listVal.getList();
        }
        return null;
    }

    public String bLPopElement(long timeout) {
        System.out.println("bLPopElement...");
        if (this.storageValueType != StorageValueType.LIST) {
            return null;
        }
        return this.listVal.bLPop(timeout);
    }
}
