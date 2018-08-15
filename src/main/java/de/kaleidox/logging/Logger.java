package de.kaleidox.logging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.kaleidox.util.JsonHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class represents a Logging framework.
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "unused", "WeakerAccess", "UnusedReturnValue", "FieldCanBeLocal"})
public class Logger {
    private static LoggingLevel level;
    private static List<Class> ignored;
    private static String suffix;
    private static String prefix;
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
     * Posts an exception with {@link LoggingLevel#ERROR}.
     * This method is useful for usage in {@link java.util.concurrent.CompletableFuture#exceptionally(Function)}.
     *
     * @param throwable The throwable to post.
     * @param <T> A type variable for the return-type.
     * @return null
     * @see java.util.concurrent.CompletableFuture#exceptionally(Function)
     */
    public <T> T exception(Throwable throwable) {
        if (!ignored.contains(loggingClass)) {
            StringBuilder sb = new StringBuilder()
                    .append("An exception has occurred: ")
                    .append(throwable.getMessage())
                    .append("\n");

            List.of(throwable.getStackTrace())
                    .forEach(line -> sb.append("\t").append(line));

            customExceptionHandlers.forEach(handler -> handler.apply(throwable));
            post(LoggingLevel.ERROR, sb.toString());
        }

        return null;
    }

    private void post(LoggingLevel level, String message) {
        if (!ignored.contains(loggingClass)) {
            if (level != LoggingLevel.ERROR) {
                customHandlers.forEach(handler -> handler.apply(level, message));
            }
            System.out.println(
                    String.format(
                            "%s %s %s",
                            newFix(level, -1),
                            message,
                            newFix(level, 1)
                    ));
        }
    }

    private String newFix(LoggingLevel level, int x) {
        String fix = configuration.get(x < 0 ? "prefix" : "suffix").asText();

        fix = fix.replace("%t", new Timestamp(System.currentTimeMillis()).toString());
        fix = fix.replace("%c", loggingClass.getName());
        fix = fix.replace("%s", "Class \"" + loggingClass.getSimpleName() + "\"");
        fix = fix.replace("%l", level.getName());

        return fix.equals("null") ? "" : fix;
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
                    System.out.println("[WARN] No logger configuration file \"logger.json\" at resources root found. " +
                            "Using default configuration ...");
                }
            } else {
                // file does not exist, go for defaults
                node = createDefaultConfig();
                System.out.println("[WARN] No logger configuration file \"logger.json\" at resources root found. " +
                        "Using default configuration ...");
            }
            configuration = node;
            try {
                configStream.close();
            } catch (IOException ignored) {
                System.out.println("test");
            }
        } else {
            configuration = createDefaultConfig();
            System.out.println("[WARN] No logger configuration file \"logger.json\" at resources root found. " +
                    "Using default configuration ...");
        }

        level = LoggingLevel.ofName(configuration.get("level").asText()).orElse(LoggingLevel.INFO);
        ignored = createIgnoredList(configuration.get("ignored"));
        prefix = configuration.get("prefix").asText();
        suffix = configuration.get("suffix").asText();
    }

    /**
     * A static method to catch exceptions.
     * This method posts the given exception from a static logger for {@link StaticException}.
     * This method can be used for {@link java.util.concurrent.CompletableFuture#exceptionally(Function)}.
     *
     * @param throwable The exception to log.
     * @param <T> A type variable for the return.
     * @return null
     * @see java.util.concurrent.CompletableFuture#exceptionally(Function)
     */
    public static <T> T get(Throwable throwable) {
        staticLogger.exception(throwable);
        return null;
    }

    private static ObjectNode createDefaultConfig() {
        ObjectNode data = JsonNodeFactory.instance.objectNode();

        data.set("level", JsonHelper.nodeOf("debug"));
        data.set("ignored", JsonHelper.arrayNode());
        data.set("prefix", JsonHelper.nodeOf("[%l] %t - %c]"));
        data.set("suffix", JsonHelper.nodeOf(null));

        new File("resources/logging.json");

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
}
