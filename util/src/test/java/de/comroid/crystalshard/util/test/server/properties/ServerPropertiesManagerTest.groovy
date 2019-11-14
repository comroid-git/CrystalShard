package de.comroid.crystalshard.util.test.server.properties

import de.comroid.javacord.util.server.properties.PropertyGroup
import de.comroid.javacord.util.server.properties.ServerPropertiesManager

import org.junit.After
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals

class ServerPropertiesManagerTest {
    private File propertiesFile

    @Before
    void setup() throws IOException {
        propertiesFile = File.createTempFile("serverProperties", ".json")
        ServerPropertiesManager manager = new ServerPropertiesManager(propertiesFile)

        manager.register("bot.traits", 419)
                .setDisplayName("Traits")
                .setDescription("These are traits")
                .setValue(100).toInt(420)
        manager.getProperty("bot.traits").getValue(200)

        manager.register("bot.name", "William")
                .setDisplayName("Name")
                .setDescription("These are names")
                .setValue(100).toString("Alfred")
        manager.getProperty("bot.name").getValue(200)

        manager.register("bot.emoji", "\uD83C\uDF61")
                .setDisplayName("Emoji")
                .setDescription("These are emojis")
                .setValue(100).toString("\uD83D\uDD12")
        manager.getProperty("bot.emoji").getValue(200)

        manager.close()
    }

    @Test(timeout = 10000L)
    void testSerialization() throws IOException {
        ServerPropertiesManager deserializer = new ServerPropertiesManager(propertiesFile)

        PropertyGroup traitsProperty = deserializer.register("bot.traits", 419)
        assertEquals 420, traitsProperty.getValue(100).asInt()
        assertEquals 419, traitsProperty.getValue(200).asInt()
        assertEquals 419, traitsProperty.getDefaultValue().asInt()
        assertEquals "Traits", traitsProperty.getDisplayName()
        assertEquals "These are traits", traitsProperty.getDescription()

        PropertyGroup nameProperty = deserializer.register("bot.name", "William")
        assertEquals "Alfred", nameProperty.getValue(100).asString()
        assertEquals "William", nameProperty.getValue(200).asString()
        assertEquals "William", nameProperty.getDefaultValue().asString()
        assertEquals "Name", nameProperty.getDisplayName()
        assertEquals "These are names", nameProperty.getDescription()

        PropertyGroup emojiProperty = deserializer.register("bot.emoji", "\uD83C\uDF61")
        assertEquals "\uD83D\uDD12", emojiProperty.getValue(100).asString()
        assertEquals "\uD83C\uDF61", emojiProperty.getValue(200).asString()
        assertEquals "\uD83C\uDF61", emojiProperty.getDefaultValue().asString()
        assertEquals "Emoji", emojiProperty.getDisplayName()
        assertEquals "These are emojis", emojiProperty.getDescription()
    }

    @After
    void cleanup() {
        propertiesFile.delete()
    }
}
