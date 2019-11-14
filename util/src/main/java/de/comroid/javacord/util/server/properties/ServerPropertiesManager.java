package de.comroid.javacord.util.server.properties;

import java.awt.Color;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import de.comroid.javacord.util.commands.Command;
import de.comroid.javacord.util.commands.CommandGroup;
import de.comroid.javacord.util.commands.CommandHandler;
import de.comroid.javacord.util.ui.embed.DefaultEmbedFactory;
import de.comroid.javacord.util.ui.messages.paging.PagedEmbed;
import de.comroid.util.interfaces.Initializable;
import de.comroid.util.markers.Value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.jetbrains.annotations.Nullable;

import static java.nio.charset.StandardCharsets.UTF_8;
import static de.comroid.util.Util.nodeOf;
import static de.comroid.util.Util.objectNode;

public final class ServerPropertiesManager implements Initializable, Closeable {
    private final Map<String, PropertyGroup> properties;
    private final File propertiesFile;
    private Supplier<EmbedBuilder> embedSupplier;

    public ServerPropertiesManager(File propertiesFile) throws IOException {
        if (!propertiesFile.exists()) propertiesFile.createNewFile();
        this.propertiesFile = propertiesFile;

        properties = new ConcurrentHashMap<>();

        init();

        Runtime.getRuntime()
                .addShutdownHook(new Thread(() -> {
                    try {
                        close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));
    }

    public PropertyGroup register(String name, Object defaultValue) {
        return register(name, defaultValue, name, "No description provided.");
    }

    public PropertyGroup register(String name, Object defaultValue, String displayName, String description) {
        properties.compute(name, (k, v) -> {
            if (v == null) return new PropertyGroup(name, defaultValue, displayName, description);
            else if (!v.getDefaultValue().equals(defaultValue) && name.equals(v.getName()))
                v = new PropertyGroup(v.getName(), defaultValue, displayName, description);
            return v;
        });

        PropertyGroup property = getProperty(name);
        assert property != null;

        return property;
    }

    public PropertyGroup getProperty(String name) {
        return properties.get(name);
    }

    public void usePropertyCommand(
            @Nullable Supplier<EmbedBuilder> embedSupplier,
            CommandHandler commandHandler
    ) {
        this.embedSupplier = (embedSupplier == null ? DefaultEmbedFactory.INSTANCE : embedSupplier);

        commandHandler.registerCommands(this);
    }

    @SuppressWarnings("ConstantConditions")
    @CommandGroup(name = "Basic Commands", description = "All commands for basic interaction with the bot")
    @Command(aliases = "property",
            usage = "property [<Property Name> [New Value]]",
            description = "Change or read the value of properties",
            requiredDiscordPermissions = PermissionType.MANAGE_SERVER,
            enablePrivateChat = false)
    public Object propertyCommand(Command.Parameters param) {
        Server server = param.getServer().orElseThrow(AssertionError::new);
        User user = param.getCommandExecutor().flatMap(MessageAuthor::asUser).orElse(null);
        String[] args = param.getArguments();

        if (user == null)
            return null;

        switch (args.length) {
            case 0: // list all props
                PagedEmbed pagedEmbed = new PagedEmbed(
                        param.getTextChannel(),
                        () -> embedSupplier.get()
                                .setDescription("Set a property to `#default#` to revert it back to its default value.")
                );

                properties.forEach((propName, propGroup) -> pagedEmbed.addField(
                        propGroup.getDisplayName().orElseGet(propGroup::getName),
                        "`" + propName + "` -> `" + propGroup.getValue(server).asString() + "`" +
                                "\n\t" + propGroup.getDescription() +
                                "\n")
                );

                return pagedEmbed;
            case 1: // print one property value
                PropertyGroup propertyGet = getProperty(args[0]);

                if (propertyGet == null) return embedSupplier.get()
                        .setColor(Color.RED)
                        .setDescription("Unknown property: `" + args[0] + "`");

                return embedSupplier.get()
                        .addField(
                                propertyGet.getDisplayName().orElseGet(propertyGet::getName),
                                "`" + args[0] + "` -> `" + propertyGet.getValue(server).asString() + "`" +
                                        "\n\t" + propertyGet.getDescription()
                        );
            case 2: // change one property
                PropertyGroup propertySet = getProperty(args[0]);
                Value value = propertySet.getValue(server);

                if (propertySet == null) return embedSupplier.get()
                        .setColor(Color.RED)
                        .setDescription("Unknown property: `" + args[0] + "`");

                Object setTo = extractValue(args[1]);

                if (setTo.equals("#default#"))
                    setTo = propertySet.getDefaultValue().asString();

                value.setter().toObject(setTo);

                return embedSupplier.get()
                        .setDescription("Changed property `" + args[0] + "` to new value: `" + value.asString() + "`")
                        .addField(
                                propertySet.getDisplayName().orElseGet(propertySet::getName),
                                "`" + args[0] + "` -> `" + propertySet.getValue(server).asString() + "`" +
                                        "\n\t" + propertySet.getDescription()
                        );
        }

        return null;
    }

    @Override
    public void init() throws IOException {
        readData();
    }

    @Override
    public void close() throws IOException {
        storeData();
    }

    public void storeData() throws IOException {
        ObjectNode node = objectNode();
        ArrayNode array = node.putArray("entries");

        properties.forEach((name, group) -> {
            ObjectNode data = array.addObject();
            data.set("name", nodeOf(name));
            data.set("default", nodeOf(group.getDefaultValue().asString()));
            data.set("displayName", nodeOf(group.getDisplayName()));
            data.set("description", nodeOf(group.getDescription()));
            group.serialize(data.putArray("items"));
        });

        if (propertiesFile.exists()) propertiesFile.delete();
        propertiesFile.createNewFile();
        FileOutputStream stream = new FileOutputStream(propertiesFile);
        stream.write(node.toString().getBytes(UTF_8));
        stream.close();
    }

    private Object extractValue(String val) {
        if (val.matches("\\d+")) {
            // is number without decimals
            long longVal = Long.parseLong(val);

            if (longVal <= Byte.MAX_VALUE) return (byte) longVal;
            else if (longVal <= Short.MAX_VALUE) return (short) longVal;
            else if (longVal <= Integer.MAX_VALUE) return (int) longVal;
            else return longVal;
        } else if (val.matches("\\d+\\.\\d+")) {
            // is number with decimals
            return Double.parseDouble(val);
        } else if (val.toLowerCase().matches("(true)|(false)|(yes)|(no)|(on)|(off)")) {
            // is boolean
            switch (val.toLowerCase()) {
                case "true":
                case "yes":
                case "on":
                    return true;
                case "false":
                case "no":
                case "off":
                    return false;
                default:
                    throw new AssertionError("Unrecognized string: " + val);
            }
        } else {
            // is plain string
            return val;
        }
    }

    private void readData() throws IOException {
        int c = 0;
        JsonNode node = new ObjectMapper().readTree(new FileInputStream(propertiesFile));

        if (node != null && node.size() != 0) {
            for (JsonNode entry : node.get("entries")) {
                PropertyGroup group = register(
                        entry.get("name").asText(),
                        entry.get("default").asText(),
                        entry.path("displayName").asText(entry.get("name").asText()),
                        entry.path("description").asText("No description provided.")
                );

                for (JsonNode item : entry.get("items")) {
                    String typeVal = item.get("type").asText();
                    try {
                        Class<?> type = Class.forName(typeVal);
                        Value.Setter setValue = group.setValue(item.get("id").asLong());
                        String val = item.get("val").asText();

                        if (type == String.class) setValue.toString(val);
                        else setValue.toObject(type.getMethod("valueOf", String.class).invoke(null, val));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new AssertionError("Illegal structure for " + typeVal + "#valueOf", e);
                    } catch (NoSuchMethodException e) {
                        throw new AssertionError("Wrong class forName: " + typeVal + "; method valueOf not found", e);
                    } catch (ClassNotFoundException e) {
                        throw new AssertionError("Wrong class forName: " + typeVal + "; class not found", e);
                    }
                }
            }
        }
    }
}
