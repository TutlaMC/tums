package net.tutla.tums.tusan.interpreter;

import net.tutla.tums.tusan.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterpreterData {
    public final Map<String, Object> vars;
    public final Map<String, Interpreter> funcs;
    public final Map<String, Variable> local;
    public final List<Runnable> asyncTasks;
    public final Map<String, Interpreter> events;

    public InterpreterData(Map<String, Object> vars, Map<String, Interpreter> funcs, Map<String, Variable> local, List<Runnable> asyncTasks) {
        this.vars = vars != null ? vars : new HashMap<>();
        this.funcs = funcs != null ? funcs : new HashMap<>();
        this.local = local != null ? local : new HashMap<>();
        this.asyncTasks = asyncTasks != null ? asyncTasks : new ArrayList<>();
        this.events = getEventNames();
    }

    private HashMap<String, Interpreter> getEventNames() {
        return new HashMap<>();
    }
}