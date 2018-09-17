package de.kaleidox.util;

public class Median {
    private int    count  = 0;
    private long[] values = new long[0];
    
    public long get() {
        if (values.length == 0) return 0;
        long val = 0;
        for (long sub : values) {
            val = val + sub;
        }
        return val / count;
    }
    
    public void add(long val) {
        long[] nArr = new long[values.length + 1];
        System.arraycopy(values, 0, nArr, 0, values.length);
        nArr[count] = val;
        values = nArr;
        count++;
    }
}
