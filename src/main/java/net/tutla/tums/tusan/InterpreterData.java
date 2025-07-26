package net.tutla.tums.tusan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterpreterData {

    public final Map<String, Variable> vars;
    public final Map<String, Interpreter> funcs;
    public final Map<String, Variable> local;
    public final List<Runnable> asyncTasks;
    public final Map<String, Interpreter> events;

    public InterpreterData(Map<String, Variable> vars, Map<String, Interpreter> funcs, Map<String, Variable> local, List<Runnable> asyncTasks) {
        this.vars = vars;
        this.funcs = funcs;
        this.local = local;
        this.asyncTasks = asyncTasks;
        this.events = get_event_names();
    }

    public HashMap<String, Interpreter> get_event_names(){ // this temporary until i map out all the fabric events
        return new HashMap<String, Interpreter>();
    }
}