package de.kaleidox.crystalshard.api.util;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.simple.SimpleLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

import static java.lang.System.arraycopy;

public class Log {
    public final static PrintStream out = new PrivacyPrintStream();

    private static Collection<char[]> silenced = new HashSet<>();
    private static ConcurrentHashMap<Class, Logger> loggers = new ConcurrentHashMap<>();

    @SuppressWarnings({"TypeParameterExplicitlyExtendsObject", "ConstantConditions"})
    @Deprecated // todo Add exceptionlogger
    public static <X extends Object> X exceptionally(Throwable throwable) {
        get().error(throwable);
        return null;
    }

    public static void silence(String str) {
        silenced.add(str.toCharArray());
    }

    public static Logger get() {
        StackTraceElement[] trace = new RuntimeException().getStackTrace();
        int i = 0;
        while (trace[i].getClassName().equals(Log.class.getName())) i++;
        try {
            return get(Class.forName(trace[i].getClassName()));
        } catch (ClassNotFoundException e) {
            return get(Log.class);
        }
    }

    public static Logger get(Class klasse) {
        if (loggers.containsKey(klasse)) return loggers.get(klasse);

        Logger self = new SimpleLogger(
                klasse.getName(),
                Level.ALL,
                true,
                false,
                true,
                true,
                "yyyy-MM-dd HH:mm:ss.SSSZ",
                null,
                PropertiesUtil.getProperties(),
                out
        );

        loggers.put(klasse, self);
        return self;
    }

    public static class PrivacyPrintStream extends PrintStream {
        public PrivacyPrintStream() {
            super(new PrivacyOutputStream(true), true);
        }

        private static class PrivacyOutputStream extends OutputStream {
            private final boolean flushOnNewline;

            private char[] buf = new char[0];

            public PrivacyOutputStream(boolean flushOnNewline) {
                this.flushOnNewline = flushOnNewline;
            }

            @Override
            public void write(int i) {
                appendBuf((char) i);
                if ((char) i == '\n' && flushOnNewline) ;
            }

            @Override
            public void flush() {
                int index, sub, begin;
                for (char[] mute : silenced) {
                    index = 0;
                    sub = 0;

                    while (index < buf.length) {
                        while (index < buf.length && buf[index] != mute[0]) ++index;
                        if (index == buf.length) break;
                        begin = index;
                        while ((index < buf.length && sub < mute.length) && buf[index] == mute[sub]) {
                            ++index;
                            ++sub;
                        }
                        if (sub >= mute.length) Arrays.fill(buf, begin, index, '*');
                    }
                }
                for (char p : buf) System.out.print(p);
                buf = new char[0];
            }

            private void appendBuf(char... app) {
                char[] sub = new char[buf.length];
                arraycopy(buf, 0, sub, 0, buf.length);
                buf = new char[sub.length + app.length];
                arraycopy(sub, 0, buf, 0, sub.length);
                arraycopy(app, 0, buf, sub.length, app.length);
            }
        }
    }
}

