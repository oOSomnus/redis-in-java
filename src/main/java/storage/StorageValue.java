package storage;

import errors.RedisResult;
import handlers.listHandlers.dataStructures.BlockingListQueue;
import handlers.listHandlers.dataStructures.RedisStream;
import storage.typeCheckAspect.StorageOperation;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * @author yihangz
 */
public class StorageValue {
    private final StorageValueType storageValueType;
    private final BlockingListQueue listVal;
    private final RedisStream streamVal;
    private String stringVal;
    private Instant exp; // null for unexp data

    public StorageValue(StorageValueType sv) {
        this.listVal = new BlockingListQueue();
        this.streamVal = new RedisStream();
        this.storageValueType = sv;
    }

    //--------------- type operation ----------------//
    public StorageValueType getStorageValueType() {
        return storageValueType;
    }

    //--------------- string operation ----------------//
    @StorageOperation(StorageValueType.STRING)
    public String getStringValue() {
        return stringVal;
    }

    @StorageOperation(StorageValueType.STRING)
    public void setStringValue(String value) {
        this.stringVal = value;
        this.exp = null;
    }

    //--------------- expiry operation ----------------//
    public Instant getExpiry() {
        return exp;
    }

    public void setExpiry(Instant exp) {
        this.exp = exp;
    }

    //--------------- list operation ----------------//
    @StorageOperation(StorageValueType.LIST)
    public Integer pushElementsToList(List<String> elements) {
        return this.listVal.addAll(elements);
    }

    @StorageOperation(StorageValueType.LIST)
    public Integer lPushElementsToList(List<String> elements) {
        return this.listVal.addFrontAll(elements);
    }

    /**
     * left pop from list
     *
     * @return leftmost element, or null when not list or empty
     */
    @StorageOperation(StorageValueType.LIST)
    public String lPopElementFromList() {
        return this.listVal.lPop();
    }

    /**
     * get list value
     *
     * @return list value | null when type is not list or list isn't initialized
     */
    @StorageOperation(StorageValueType.LIST)
    public List<String> getListValue() {
        return this.listVal.getList();
    }

    @StorageOperation(StorageValueType.LIST)
    public String bLPopElement(long timeout) {
        System.out.println("bLPopElement...");
        return this.listVal.bLPop(timeout);
    }

    //--------------- stream operation ----------------//
    @StorageOperation(StorageValueType.STREAM)
    public RedisResult putStream(String id, Map<String, String> vals) {
        RedisResult result = this.streamVal.put(id, vals);
        return result;
    }
}
