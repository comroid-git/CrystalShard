package de.comroid.crystalshard.api.model;

import java.io.IOException;
import java.util.Formattable;
import java.util.FormattableFlags;
import java.util.Formatter;
import java.util.logging.Level;

import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.api.entity.user.GuildMember;
import de.comroid.crystalshard.api.entity.user.User;

import static de.comroid.crystalshard.util.Util.isFlagSet;

public interface Mentionable extends Formattable {
    String getMentionTag();

    @Override
    default void formatTo(final Formatter formatter, final int flags, final int width, final int precision) {
        final Appendable out = formatter.out();
        String str;

        if (isFlagSet(FormattableFlags.ALTERNATE, flags)) {
            if (this instanceof GuildMember)
                str = ((GuildMember) this).getNickname()
                        .orElseGet(((User) this)::getDiscriminatedName);
            else if (this instanceof User)
                str = ((User) this).getUsername();
            else str = this.toString();
        } else str = getMentionTag();

        String append = "";
        final int add = width - str.length();
        for (int i = 0; i < add; i++)
            //noinspection StringConcatenationInLoop
            append += ' ';

        if (isFlagSet(flags, FormattableFlags.LEFT_JUSTIFY))
            str += append;
        else str = append + str;

        if (precision > -1)
            str = str.substring(0, precision);

        try {
            out.append(isFlagSet(flags, FormattableFlags.UPPERCASE) ? str.toUpperCase() : str);
        } catch (IOException IOex) {
            Snowflake.SNOWFLAKE_COMMON_LOGGER
                    .at(Level.SEVERE)
                    .withCause(IOex)
                    .log("Could not write to Formatter: %s", formatter);
        }
    }
}
