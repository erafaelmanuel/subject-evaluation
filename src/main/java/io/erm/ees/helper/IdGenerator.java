package io.erm.ees.helper;

import java.util.Locale;

public class IdGenerator {

    public static long random(Range range) {
        return (long) (range.value() * Math.random());
    }

    public static long random(Range range, Prefix prefix) {
        long pref = (prefix.value() + "").length() > 7 ?
                Long.parseLong((prefix.value() + "").substring(0, 6)):prefix.value();
        switch (range) {
            case BIG:
                return Long.parseLong(String.format(Locale.ENGLISH, "%d%012d", pref, random(range)));
            case NORMAL:
                return Long.parseLong(String.format(Locale.ENGLISH, "%d%08d", pref, random(range)));
            case SMALL:
                return Long.parseLong(String.format(Locale.ENGLISH, "%d%05d", pref, random(range)));
            default:
                return Long.parseLong(String.format(Locale.ENGLISH, "%d%05d", pref, random(range)));
        }
    }

    public enum Range {
        BIG(999999999999L), NORMAL(99999999L), SMALL(99999);

        private long value;

        Range(long value) {
            this.value=value;
        }

        public long value() {
            return value;
        }
    }

    @FunctionalInterface
    public interface Prefix {
        long value();
    }
}
