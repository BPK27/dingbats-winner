package data_structs;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class FeaturesMap extends TreeMap<String, List<List<String>>> {
    public void put(String key, List<String> list) {
        List<List<String>> current = get(key);
        if (current == null) {
            current = new ArrayList <List<String>>();
            super.put(key, current);
        }
        current.add(list);
    }
}