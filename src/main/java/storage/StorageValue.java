package storage;

import java.time.Instant;

public class StorageValue {
    public String value;
    public Instant exp; //null for unexp data

    public StorageValue(String value) {
        this.value = value;
        this.exp = null;
    }
    public StorageValue(String value, Instant exp){
        this.value = value;
        this.exp = exp;
    }
}
