package com.link.Assessment.enums;

import java.util.HashMap;
import java.util.Map;

public enum SortFields {
    ID("id");



    public final String label;
    private static final Map<String, SortFields> map = new HashMap<>();


    static {
        for (SortFields e : values()) {
            map.put(e.label, e);
        }
    }

    /*//*/
    private SortFields(String label) {
        this.label = label;

    }

    public static SortFields valueOfName(String label) {
        return map.get(label);
    }
}
