package de.kaleidox.logging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.kaleidox.crystalshard.main.exception.LowStackTraceable;
import de.kaleidox.util.helpers.JsonHelper;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class represents a Logging framework.
 */
@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "unused", "WeakerAccess"})
public class Logger {
    private final static LoggingLevel DEFAULT_LEVEL = LoggingLevel.DEBUG;
    private final static List<Class> DEFAULT_IGNORED = new ArrayList<>();
    private final static String DEFAULT_PREFIX = "[%l] %t - %c:";
    private final static String DEFAULT_SUFFIX = null;
    private final static List<String> DEFAULT_BLANKED = new ArrayList<>();

    private static LoggingLevel level;
    private static List<Class> ignored = new ArrayList<>();
    private static List<String> blanked = new ArrayList<>();
    private static JsonNode configuration;
    private static boolean hasInit = false;
    private static Logger staticLogger = new Logger(StaticException.class);
    private static List<CustomHandler> customHandlers = new ArrayList<>();
    private static List<CustomExceptionHandler> customExceptionHandlers = new ArrayList<>();

    private final Class loggingClass;

    /**
     * Creates a new logger instance for an object, using it's class.
     *
     * @param loggingObject The object to create the logger for.
     */
    public Logger(Object loggingObject) {
        this(loggingObject.getClass());
    }

    /**
     * Creates a new logger instance for a class.
     *
     * @param loggingClass The class to create the logger for.
     */
    public Logger(Class loggingClass) {
        this.loggingClass = loggingClass;

        if (!hasInit) {
            initLogging();
        }
    }

    public void traceElseInfo(Object traceMessage, Object infoMessage) {
        if (level.getSeverity() >= LoggingLevel.TRACE.getSeverity()) {
            post(LoggingLevel.TRACE, traceMessage.toString());
        } else info(infoMessage);
    }

    /**
     * Posts a log message with {@link LoggingLevel#INFO}.
     *
     * @param message The message to post. {@link Object#toString()} is invoked on this.
     */
    public void info(Object message) {
        if (level.getSeverity() >= LoggingLevel.INFO.getSeverity()) {
            post(LoggingLevel.INFO, message.toString());
        }
    }

    /**
     * Posts a log message with {@link LoggingLevel#WARN}.
     *
     * @param message The message to post. {@link Object#toString()} is invoked on this.
     */
    public void warn(Object message) {
        if (level.getSeverity() >= LoggingLevel.WARN.getSeverity()) {
            post(LoggingLevel.WARN, message.toString());
        }
    }

    /**
     * Posts a log message with {@link LoggingLevel#DEBUG}.
     *
     * @param message The message to post. {@link Object#toString()} is invoked on this.
     */
    public void debug(Object message) {
        if (level.getSeverity() >= LoggingLevel.DEBUG.getSeverity()) {
            post(LoggingLevel.DEBUG, message.toString());
        }
    }

    /**
     * Posts a log message with {@link LoggingLevel#ERROR}.
     *
     * @param message The message to post. {@link Object#toString()} is invoked on this.
     */
    public void error(Object message) {
        if (level.getSeverity() >= LoggingLevel.ERROR.getSeverity()) {
            post(LoggingLevel.ERROR, message.toString());
        }
    }

    /**
     * Posts a log message with {@link LoggingLevel#TRACE}.
     *
     * @param message The message to post. {@link Object#toString()} is invoked on this.
     */
    public void trace(Object message) {
        if (level.getSeverity() >= LoggingLevel.TRACE.getSeverity()) {
            post(LoggingLevel.TRACE, message.toString());
        }
    }

    /**
     * Posts a log message with {@link LoggingLevel#DEEP_TRACE}.
     *
     * @param message The message to post. {@link Object#toString()} is invoked on this.
     */
    public void deeptrace(Object message) {
        if (level.getSeverity() >= LoggingLevel.DEEP_TRACE.getSeverity()) {
            post(LoggingLevel.DEEP_TRACE, message.toString());
        }
    }

    /**
     * Posts an exception with {@link LoggingLevel#ERROR}.
     * This method is useful for usage in {@link java.util.concurrent.CompletableFuture#exceptionally(Function)}.
     *
     * @param throwable The throwable to post.
     * @param <T>       A type variable for the return-type.
     * @return null
     * @see java.util.concurrent.CompletableFuture#exceptionally(Function)
     */
    public <T> T exception(Throwable throwable) {
        return exception(throwable, null);
    }

    /**
     * Posts an exception with {@link LoggingLevel#ERROR}.
     * This method is useful for logging caught exceptions with a custom message.
     *
     * @param throwable     The throwable to post.
     * @param customMessage A custom message to post instead of {@link Throwable#getMessage()}. May be null.
     * @param <T>           A type variable for the return-type.
     * @return null
     */
    public <T> T exception(Throwable throwable, String customMessage) {
        if (!ignored.contains(loggingClass)) {
            StringBuilder sb = new StringBuilder()
                    .append("An exception has occurred: ")
                    .append(customMessage == null ? throwable.getMessage() : customMessage)
                    .append("\nException in thread \"")
                    .append(Thread.currentThread().getName())
                    .append("\" ")
                    .append(throwable.getClass().getName());

            if (throwable instanceof LowStackTraceable) {
                if (((LowStackTraceable) throwable).lowStackTrace()) {
                    sb.append("\n\t").append(throwable.getStackTrace()[0]);
                }
            } else {
                List.of(throwable.getStackTrace())
                        .forEach(line -> sb.append("\n\t").append(line));
            }

            customExceptionHandlers.forEach(handler -> handler.apply(throwable));
            post(LoggingLevel.ERROR, sb.toString());
        }

        return null;
    }

    /**
     * Checks a list of items for nonnull values.
     *
     * @param items The items to check.
     */
    public void nonNullChecks(Object... items) {
        for (Object x : items) {
            if (!Objects.nonNull(x)) {
                exception(
                        new NullPointerException("NonNullCheck failed: " + x)
                );
            }
        }
    }

    private void post(LoggingLevel level, String message) {
        if (!ignored.contains(loggingClass)) {
            if (level != LoggingLevel.ERROR) {
                customHandlers.forEach(handler -> handler.apply(level, message));
            }
            String format = String.format("%s %s %s", newFix(level, -1), message, newFix(level, 1));
            for (String string : blanked) {
                int i1 = format.indexOf(string);
                if (i1 > -1) {
                    format = format.substring(0, i1) +
                            "*****" + format.substring(i1 + string.length());
                }
            }
            System.out.println(format);
        }
    }

    private String newFix(LoggingLevel level, int x) {
        String fix = configuration.get(x < 0 ? "prefix" : "suffix").asText();

        fix = fix.replace("%t", new Timestamp(System.currentTimeMillis()).toString());
        fix = fix.replace("%c", loggingClass.getName());
        fix = fix.replace("%s", "Class \"" + loggingClass.getSimpleName() + "\"");
        fix = fix.replace("%l", level.getName());
        fix = fix.replace("%r", Thread.currentThread().getName());

        /*
        if (x > 0) {
            String[] split = fix.split("\t");
            for (int i = 0; i < split.length; i++) {
                StringBuilder subfix = new StringBuilder(split[i]);
                if (subfix.toString().contains("\t")) {
                    int tabCounterPos = subfix.indexOf("\t");
                    char check2 = subfix.charAt(tabCounterPos + 2);
                    char check1 = subfix.charAt(tabCounterPos + 1);
                    int extraTabs;
                    if (Character.isDigit(check2))
                        extraTabs = Integer.parseInt(subfix.substring(tabCounterPos + 1, tabCounterPos + 2));
                    else if (Character.isDigit(check1))
                        extraTabs = Integer.parseInt(String.valueOf(subfix.charAt(tabCounterPos + 1)));
                    else
                        extraTabs = 0;
                    int indentSpaces = extraTabs * 4;
                    int indentFromL = subfix.substring(tabCounterPos).length();
                    int addActualSpaces = indentSpaces - indentFromL;
                    while (addActualSpaces > 0) {
                        subfix.append(" ");
                        addActualSpaces--;
                    }
                    subfix.append("\t");
                }
            }
        }
        */

        return fix.equals("null") ? "" : fix;
    }

    /**
     * Registers the given CustomHandler for handling any post message.
     *
     * @param handler The handler to register.
     */
    public static void registerCustomHandler(CustomHandler handler) {
        customHandlers.add(handler);
    }

    /**
     * Registers the given CustomExceptionHandler for handling exceptions.
     *
     * @param handler The handler to register.
     */
    public static void registerCustomExceptionHandler(CustomExceptionHandler handler) {
        customExceptionHandlers.add(handler);
    }

    /**
     * Sets the logging level to the given level.
     * Changes are not stored and get lost on any restart.
     *
     * @param level The level to set to.
     */
    public static void setLevel(LoggingLevel level) {
        ((ObjectNode) configuration).set("level", JsonHelper.nodeOf(level.getName()));
        Logger.level = level;
    }

    /**
     * Sets the ignored classes.
     * Changes are not stored and get lost on any restart.
     *
     * @param ignoredClasses A list of ignored classes.
     */
    public static void setIgnored(Class... ignoredClasses) {
        ((ObjectNode) configuration).set("ignored", JsonHelper.arrayNode(
                Stream.of(ignoredClasses)
                        .map(Class::getName)
                        .collect(Collectors.toList())
                        .toArray(new Object[ignoredClasses.length])
        ));
        Logger.ignored = List.of(ignoredClasses);
    }

    /**
     * Sets the prefix of the logger.
     * Changes are not stored and get lost on any restart.
     *
     * <p>The prefix may contain three different formatting pieces:</p>
     * <p>{@code %l} - Gets replaced with the Level of the message.</p>
     * <p>{@code %t} - Gets replaced with the current Timestamp of the message.</p>
     * <p>{@code %c} - Gets replaced with the class name obtained by {@link Class#getName()}.</p>
     * <p>{@code %c} - Gets replaced with the class name obtained by {@link Class#getSimpleName()}.</p>
     *
     * @param prefix The prefix to set.
     */
    public static void setPrefix(String prefix) {
        ((ObjectNode) configuration).set("prefix", JsonHelper.nodeOf(prefix));
    }

    /**
     * Sets the suffix of the logger.
     * Changes are not stored and get lost on any restart.
     *
     * <p>The suffix may contain three different formatting pieces:</p>
     * <p>{@code %l} - Gets replaced with the Level of the message.</p>
     * <p>{@code %t} - Gets replaced with the current Timestamp of the message.</p>
     * <p>{@code %c} - Gets replaced with the class name obtained by {@link Class#getName()}.</p>
     * <p>{@code %c} - Gets replaced with the class name obtained by {@link Class#getSimpleName()}.</p>
     *
     * @param suffix The suffix to set.
     */
    public static void setSuffix(String suffix) {
        ((ObjectNode) configuration).set("suffix", JsonHelper.nodeOf(suffix));
    }

    private static void initLogging() {
        hasInit = true;
        JsonNode node;
        InputStream configStream = Logger.class.getResourceAsStream("/logging.json");
        if (configStream != null) {
            Scanner s = new Scanner(configStream).useDelimiter("\\A");
            if (s.hasNext()) {
                try {
                    String content = s.next();
                    ObjectMapper mapper = new ObjectMapper();
                    node = mapper.readTree(content);
                } catch (IOException ignored) {
                    node = createDefaultConfig();
                }
            } else {
                // file does not exist, go for defaults
                node = createDefaultConfig();
            }
            configuration = node;
            try {
                configStream.close();
            } catch (IOException ignored) {
                System.out.println("test");
            }
        } else {
            configuration = createDefaultConfig();
        }

        level = LoggingLevel.ofName(configuration.get("level").asText()).orElse(LoggingLevel.INFO);
        ignored = createIgnoredList(configuration.get("ignored"));
        String prefix = configuration.get("prefix").asText();
        String suffix = configuration.get("suffix").asText();
    }

    /**
     * A static method to catch exceptions.
     * This method posts the given exception from a static logger for {@link StaticException}.
     * This method can be used for {@link java.util.concurrent.CompletableFuture#exceptionally(Function)}.
     *
     * @param throwable The exception to log.
     * @param <T>       A type variable for the return.
     * @return null
     * @see java.util.concurrent.CompletableFuture#exceptionally(Function)
     */
    public static <T> T get(Throwable throwable) {
        staticLogger.exception(throwable);
        return null;
    }

    private static ObjectNode createDefaultConfig() {
        System.out.println("[INFO] No logger configuration file \"logger.json\" at resources root found. " +
                "Using default configuration or code set preferences...");
        ObjectNode data = JsonNodeFactory.instance.objectNode();

        data.set("level", JsonHelper.nodeOf(DEFAULT_LEVEL.getName()));
        data.set("ignored", JsonHelper.arrayNode(DEFAULT_IGNORED.stream().map(Class::getName).toArray()));
        data.set("prefix", JsonHelper.nodeOf(DEFAULT_PREFIX));
        data.set("suffix", JsonHelper.nodeOf(DEFAULT_SUFFIX));
        data.set("blanked", JsonHelper.arrayNode(DEFAULT_BLANKED.toArray()));

        return data;
    }

    private static List<Class> createIgnoredList(JsonNode data) {
        List<Class> list = new ArrayList<>();

        for (JsonNode clazz : data) {
            try {
                list.add(Class.forName(clazz.asText()));
            } catch (ClassNotFoundException e) {
                throw new NullPointerException(e.getMessage());
            }
        }

        return list;
    }

    public static void addBlankedWord(String word) {
        blanked.add(word);
    }
}
