package de.kaleidox.crystalshard.util.discord.messages;

import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.MessageReciever;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.helpers.ListHelper;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("WeakerAccess")
public class InformationMessage {
    private final static ConcurrentHashMap<MessageReciever, InformationMessage> selfMap = new ConcurrentHashMap<>();

    private MessageReciever messageable;
    private ArrayList<InformationField> fields = new ArrayList<>();
    private AtomicReference<Message> myMessage = new AtomicReference<>();

    private InformationMessage(MessageReciever messageable) {
        this.messageable = messageable;

        selfMap.putIfAbsent(messageable, this);
    }

    public InformationMessage addField(String name, String title, String text) {
        return addField(name, title, text, false);
    }

    public InformationMessage addField(String name, String title, String text, boolean inline) {
        fields.add(
                new InformationField(
                        name,
                        title,
                        text,
                        inline
                )
        );

        return this;
    }

    public InformationMessage editField(String name, String newText) {
        return editField(name, null, newText, false);
    }

    public InformationMessage editField(String name, String newTitle, String newText) {
        return editField(name, newTitle, newText, false);
    }

    public InformationMessage editField(String name, String newTitle, String newText, boolean newInline) {
        Optional<InformationField> complex = ListHelper.findComplex(fields, name, InformationField::getName);

        if (complex.isPresent()) {
            InformationField field = complex.get();

            if (newTitle != null)
                field.setTitle(newTitle);
            field.setText(newText);
            field.setInline(newInline);
        } else {
            throw new NullPointerException("Could not find field: " + name);
        }

        return this;
    }

    public InformationMessage removeField(String name) {
        Optional<InformationField> complex = ListHelper.findComplex(fields, name, InformationField::getName);

        if (complex.isPresent()) {
            InformationField field = complex.get();

            fields.remove(field);
        } else {
            throw new NullPointerException("Could not find field: " + name);
        }

        return this;
    }

    public void refresh() {
        Embed.Builder embed = this.messageable
                .getDiscord()
                .getUtilities()
                .getDefaultEmbed()
                .getBuilder();

        for (InformationField field : fields) {
            embed.addField(
                    field.title,
                    field.text,
                    field.inline
            );
        }

        if (myMessage.get() != null) {
            myMessage.get()
                    .delete()
                    .thenRunAsync(() -> messageable
                            .sendMessage(embed.build())
                            .thenAcceptAsync(myMessage::set)
                            .exceptionally(Logger::get)
                    )
                    .exceptionally(Logger::get);
        } else {
            messageable.sendMessage(embed.build())
                    .thenAcceptAsync(msg -> myMessage.set(msg))
                    .exceptionally(Logger::get);
        }
    }

    @SuppressWarnings("FinalStaticMethod")
    public final static InformationMessage getInstance(MessageReciever messageable) {
        return selfMap.getOrDefault(messageable, new InformationMessage(messageable));
    }

    class InformationField {
        private String name, title, text;
        private boolean inline;

        InformationField(String name, String title, String text, boolean inline) {
            this.name = name;
            this.title = title;
            this.text = text;
            this.inline = inline;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public boolean getInline() {
            return inline;
        }

        public void setInline(boolean inline) {
            this.inline = inline;
        }
    }
}
