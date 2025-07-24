package storage;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yihangz
 */
public class StorageValue {
    private String stringVal;
    private List<String> listVal;
    private Instant exp; //null for unexp data
    private StorageValueType storageValueType;
    public StorageValueType getStorageValueType() {
        return storageValueType;
    }
    public void setStringValue(String value) {
        this.stringVal = value;
        this.exp = null;
        this.storageValueType = StorageValueType.STRING;
    }
    public String getStringValue() {
        return stringVal;
    }

    public void setExpiry(Instant exp) {
        this.exp = exp;
    }

    public Instant getExpiry() {
        return exp;
    }

    public Integer pushElementsToList(List<String> elements){
        if(this.storageValueType == StorageValueType.LIST && this.listVal != null){
            this.listVal.addAll(elements);
        }else{
            this.storageValueType = StorageValueType.LIST;
            this.listVal = new ArrayList<>();
            this.listVal.addAll(elements);
        }
        return this.listVal.size();
    }

    public Integer lPushElementsToList(List<String> elements){
        List<String> reversedElements = elements.reversed();
        if(this.storageValueType == StorageValueType.LIST && this.listVal != null){
            this.listVal.addAll(0, reversedElements);
        }else{
            this.storageValueType = StorageValueType.LIST;
            this.listVal = new ArrayList<>();
            this.listVal.addAll(0, reversedElements);
        }
        return this.listVal.size();
    }

    /**
     * left pop from list
     * @return leftmost element, or null when not list or empty
     */
    public String lPopElementFromList(){
        if(this.storageValueType == StorageValueType.LIST && this.listVal != null){
            return this.listVal.removeFirst();
        }
        return null;
    }

    /**
     * get list value
     * @return list value | null when type is not list or list isn't initialized
     */
    public List<String> getListValue(){
        if(this.storageValueType == StorageValueType.LIST && this.listVal != null) {
            return this.listVal;
        }
        return null;
    }

}
