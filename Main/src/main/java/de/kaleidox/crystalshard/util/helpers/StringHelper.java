package de.kaleidox.crystalshard.util.helpers;

public class StringHelper extends NullHelper {
    public static boolean isNumeric(String s) {
        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }
}
