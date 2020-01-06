package org.comroid.crystalshard.api.model.message;

import java.util.function.Function;

public enum TextDecoration implements Function<String, String> {
    NONE {
        @Override
        public String apply(String str) {
            return str;
        }
    },

    ITALICS {
        @Override
        public String apply(String str) {
            return "_" + str + "_";
        }
    },

    BOLD {
        @Override
        public String apply(String str) {
            return "**" + str + "**";
        }
    },

    UNDERLINE {
        @Override
        public String apply(String str) {
            return "__" + str + "__";
        }
    },

    STRIKETHROUGH {
        @Override
        public String apply(String str) {
            return "~~" + str + "~~";
        }
    },

    CODE_SHORT {
        @Override
        public String apply(String str) {
            return "`" + str + "`";
        }
    },

    CODE_LONG {
        @Override
        public String apply(String str) {
            return "```" + str + "```";
        }

        @Override
        public String apply(String str, String language) {
            return "```" + language + "\n" + str + "\n```";
        }
    },

    SPOILER {
        @Override
        public String apply(String str) {
            return "||" + str + "||";
        }
    },

    QUOTE {
        @Override
        public String apply(String str) {
            return "> " + str;
        }
    };

    public String apply(String str, String extraArgs) {
        return apply(str);
    }
}
