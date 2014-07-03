package data_structs;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class FeatureDescMap extends TreeMap<String, List<String>> {
    public void put(String key, String list) {
        List<String> current = get(key);
        if (current == null) {
            current = new ArrayList <String>();
            super.put(key, current);
        }
        current.add(list);
    }
}