package storage;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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

    public Integer setSingleElementList(String element){
        if(this.storageValueType == StorageValueType.LIST && this.listVal != null){
            this.listVal.add(element);
        }else{
            this.storageValueType = StorageValueType.LIST;
            this.listVal = new ArrayList<>();
            this.listVal.add(element);
        }
        return this.listVal.size();
    }

    public Integer setMultipleELementList(List<String> elements){
        if(this.storageValueType == StorageValueType.LIST && this.listVal != null){
            this.listVal.addAll(elements);
        }else{
            this.storageValueType = StorageValueType.LIST;
            this.listVal = new ArrayList<>();
            this.listVal.addAll(elements);
        }
        return this.listVal.size();
    }

}
