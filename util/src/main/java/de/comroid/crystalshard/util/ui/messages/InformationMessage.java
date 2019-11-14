package de.comroid.crystalshard.util.ui.messages;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import de.comroid.util.Util;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.Messageable;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.util.logging.ExceptionLogger;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("WeakerAccess")
public class InformationMessage {
    private final static ConcurrentHashMap<Messageable, InformationMessage> selfMap = new ConcurrentHashMap<>();

    private Messageable messageable;
    private ArrayList<InformationField> fields = new ArrayList<>();
    private AtomicReference<Message> myMessage = new AtomicReference<>();

    public InformationMessage(Messageable messageable) {
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

    public InformationMessage editField(String name, @Nullable String newTitle, String newText) {
        return editField(name, newTitle, newText, false);
    }

    public InformationMessage editField(String name, @Nullable String newTitle, String newText, boolean newInline) {
        Optional<InformationField> complex = Util.findComplex(fields, name, InformationField::getName);

        if (complex.isPresent()) {
            InformationField field = complex.get();

            if (newTitle != null)
                field.setName(newTitle);
            field.setValue(newText);
            field.setInline(newInline);
        } else {
            throw new NullPointerException("Could not find field: " + name);
        }

        return this;
    }

    public InformationMessage removeField(String name) {
        Optional<InformationField> complex = Util.findComplex(fields, name, InformationField::getName);

        if (complex.isPresent()) {
            InformationField field = complex.get();

            fields.remove(field);
        } else {
            throw new NullPointerException("Could not find field: " + name);
        }

        return this;
    }

    public void refresh() {
        EmbedBuilder embed = new EmbedBuilder();

        for (InformationField field : fields) {
            embed.addField(
                    field.getName(),
                    field.getValue(),
                    field.isInline()
            );
        }

        if (myMessage.get() != null) {
            myMessage.get()
                    .delete()
                    .thenRunAsync(() -> messageable
                            .sendMessage(embed)
                            .thenAcceptAsync(myMessage::set)
                            .exceptionally(ExceptionLogger.get())
                    )
                    .exceptionally(ExceptionLogger.get());
        } else {
            messageable.sendMessage(embed)
                    .thenAcceptAsync(msg -> myMessage.set(msg))
                    .exceptionally(ExceptionLogger.get());
        }
    }

    @SuppressWarnings("FinalStaticMethod")
    public final static InformationMessage get(Messageable messageable) {
        return selfMap.getOrDefault(messageable, new InformationMessage(messageable));
    }

    class InformationField extends EmbedFieldRepresentative {
        private String descr;

        InformationField(String descr, String name, String value, boolean inline) {
            super(name, value, inline);
            this.descr = descr;
        }

        void setName(String name) {
            super.name = name;
        }

        void setValue(String value) {
            super.value = value;
        }

        void setInline(boolean inline) {
            super.inline = inline;
        }
    }
}
