package utils;

import java.util.Dictionary;
import java.util.Hashtable;

public final class Tables {

    public static final Dictionary<Integer, Integer[]> table1 = new Hashtable<Integer, Integer[]>(){{
        put(41, new Integer[]{50, 100});
        put(43, new Integer[]{50, 100});
        put(1, new Integer[]{75, 150});
    }};
}
