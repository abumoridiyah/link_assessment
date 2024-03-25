package com.link.Assessment.enums;

import java.util.HashMap;
import java.util.Map;

public enum SortDirections {
    DESCENDING("DESCENDING"),
    ASCENDING("ASCENDING");


    public final String label;
    private static final Map<String, SortDirections> map = new HashMap<>();


    static {
        for (SortDirections e : values()) {
            map.put(e.label, e);
        }
    }

    private SortDirections(String label) {
        this.label = label;

    }

    public static SortDirections valueOfName(String label) {
        return map.get(label);
    }
}
