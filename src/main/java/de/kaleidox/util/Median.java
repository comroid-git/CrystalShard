package de.kaleidox.util;

public class Median {
    private int count = 0;
    private long[] values = new long[0];

    public double get() {
        long val = 0;
        for (long sub : values) {
            val = val + sub;
        }
        return (double) val / count;
    }

    public void add(long val) {
        long[] nArr = new long[values.length+1];
        System.arraycopy(values, 0, nArr, 0, values.length);
        nArr[count] = val;
        values = nArr;
        count++;
    }
}
