package de.comroid.crystalshard.util.commands.eval.model;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import static java.lang.System.nanoTime;


public class EvalFactory {
    private final ScriptEngineManager mgr = new ScriptEngineManager();
    private final ScriptEngine engine = mgr.getEngineByName("JavaScript");
    private final Bindings bindings = engine.createBindings();
    private final ExecutionFactory builder = new ExecutionFactory();

    public EvalFactory(BindingFactory bindings) {
        this.bindings.putAll(bindings.getBindings());
        engine.setBindings(this.bindings, ScriptContext.GLOBAL_SCOPE);
    }

    public Eval prepare(String[] lines) throws ClassNotFoundException {
        ExecutionFactory.Execution code = this.builder.build(lines);
        return new Eval(this.engine, code);
    }

    public static class Eval {
        private ExecutionFactory.Execution code;
        private ScriptEngine engine;
        private float execTime;
        private float evalTime;
        private long start;

        Eval(ScriptEngine engine, ExecutionFactory.Execution code) {
            this.engine = engine;
            this.code = code;
        }

        public long getStartTime() {
            return start;
        }

        public boolean isVerbose() {
            return code.isVerbose();
        }

        public Object run() throws ScriptException {
            this.start = nanoTime();
            Object result = this.engine.eval(this.code.toString());
            this.evalTime = nanoTime() - start;
            Object execTime = this.engine.getContext().getAttribute("execTime");
            this.execTime = Float.parseFloat(execTime != null ? execTime.toString() : "0");
            return result != null ? result : "";
        }

        public float getExecTime() {
            return this.execTime;
        }

        public double getEvalTime() {
            return this.evalTime;
        }

        public String getUserCode() {
            return this.code.getOriginalCode();
        }

        public String getFullCode() {
            return this.code.toString();
        }

        public String getDisplayCode() {
            return isVerbose() ? getFullCode() : getUserCode();
        }
    }
}
