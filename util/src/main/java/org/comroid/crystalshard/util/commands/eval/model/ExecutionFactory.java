package org.comroid.crystalshard.util.commands.eval.model;

public class ExecutionFactory {
    private final StringBuilder code;
    private String originalCode;

    public ExecutionFactory() {
        this.code = new StringBuilder();
        this.addPolyfills();
        this.addRunnerWrapper();
    }

    public Execution build(String[] lines) throws ClassNotFoundException {
        boolean verbose = this.addCode(lines);
        return new Execution(this.code, originalCode, verbose);
    }

    public Execution _safeBuild(String[] lines) {
        boolean verbose = this.safeAddCode(lines);
        return new Execution(this.code, originalCode, verbose);
    }

    private void addPolyfills() {

        this.code.append("var sys = Java.type('java.lang.System');\r\n");
        /*
           this.code
                .append(Polyfill.timeout);
         */
    }

    private boolean addCode(String[] lines) throws ClassNotFoundException {
        this.code.append("function run(context){\r\n")
                .append("\tfor(var key in context){")
                .append("\t\tif(context.hasOwnProperty(key))\r\n")
                .append("\t\t\teval('var ' + key + ' = context[key]')\r\n")
                .append("\t}\r\n")
                .append("\treturn eval(\"");
        StringBuilder code = new StringBuilder();

        boolean append;
        boolean verbose = false;
        for (String line : lines) {
            append = !line.trim().startsWith("```");
            if (!verbose) {
                verbose = line.startsWith("```verbose");
            }
            if (line.startsWith("import ")) {
                append = false;

                String classname = line.substring("import ".length(), line.length() - ((line.lastIndexOf(';') == line.length()) ? 2 : 1));
                Class<?> aClass = Class.forName(classname);

                code.append('\n')
                        .append("var ")
                        .append(aClass.getSimpleName())
                        .append(" = Java.type('")
                        .append(classname)
                        .append("');")
                        .append("\r\n");
            }

            if (append) {
                code.append("\r\n")
                        .append(line.replaceAll("\"", "'"));
            }
        }
        this.originalCode = code.toString().trim();
        this.code
                .append(this.originalCode.replaceAll("\\r\\n", "\"+\""))
                .append("\");}");
        return verbose;
    }

    private boolean safeAddCode(String[] lines) {
        this.code.append("function run(context){\r\n")
                .append("\tfor(var key in context){")
                .append("\t\tif(context.hasOwnProperty(key))\r\n")
                .append("\t\t\teval('var ' + key + ' = context[key]')\r\n")
                .append("\t}\r\n")
                .append("\treturn eval(\"");
        StringBuilder code = new StringBuilder();

        boolean append;
        boolean verbose = false;
        for (String line : lines) {
            append = !line.contains("```");
            if (!verbose) {
                verbose = line.startsWith("```verbose");
            }
            if (line.startsWith("import ")) {
                append = false;

                String classname = line.substring("import ".length(), line.length() - ((line.lastIndexOf(';') == line.length()) ? 2 : 1));
                String simpleClassName;

                try {
                    Class<?> aClass = Class.forName(classname);
                    simpleClassName = aClass.getSimpleName();
                } catch (ClassNotFoundException e) {
                    simpleClassName = "__CLASS_NOT_FOUND_" + classname + "__";
                }
                code.append('\n')
                        .append("\tvar ")
                        .append(simpleClassName)
                        .append(" = Java.type('")
                        .append(classname)
                        .append("');")
                        .append("\r\n");
            }

            if (append) {
                code.append("\r\n")
                        .append(line.replaceAll("\"", "'"));
            }
        }
        this.originalCode = code.toString();
        this.code
                .append(this.originalCode.replaceAll("\\r\\n", ";"))
                .append("\");}");
        return verbose;
    }

    private void addRunnerWrapper() {
        this.code
                .append("(function(context){\r\n")
                .append("context.execTime = 0;\r\n")
                .append("var t0 = new Date().getTime();\r\n")
                .append("var result = run.apply(context, arguments);")
                .append("var t1 = new Date().getTime();\r\n")
                .append("context.execTime = t1 - t0;\r\n")
                .append("return result;")
                .append("})(this);");
    }

    public static class Execution {
        private StringBuilder code;
        private String originalCode;
        private boolean verbose;

        public Execution(StringBuilder code, String originalCode, boolean verbose) {
            this.code = code;
            this.originalCode = originalCode;
            this.verbose = verbose;
        }

        public boolean isVerbose() {
            return this.verbose;
        }

        public String getOriginalCode() {
            return originalCode;
        }

        @Override
        public String toString() {
            return "\t" + this.code.toString().trim();
        }
    }


}
