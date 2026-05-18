package net.tutla.tums;

import net.tutla.tums.tusan.interpreter.Interpreter;
import net.tutla.tums.tusan.lang.Tusan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class TumsAPI {
    public static InterpreterRegistry getRegister(){
        return Tums.getRegister();
    }
    public static Tusan blank(){
        return new Tusan();
    }

    public static Tusan getGlobalTusan(){
        return Tums.getTusan();
    }



    public static void runAllScripts(Path scriptsDir, Tusan tusan){
        try (Stream<Path> stream = Files.list(scriptsDir)) {
            stream.filter(Files::isRegularFile)
                    .forEach((p) -> {runScript(p, tusan);});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void runScript(Path script, Tusan tusan){
        Interpreter interpreter = new Interpreter(tusan);
        interpreter.setup(null,null,null, script);

        try{
            interpreter.compile(); // where every script in the dir gets executed
        } catch (Exception e){
            e.printStackTrace();
        }

        getRegister().add(interpreter.data); // why are we doing this?
    }

    public static void executeEvent(String name, HashMap<String, Object> variables){
        List<Interpreter> callback = Tums.getRegister().registry.events.get(name.toUpperCase());
        for (Interpreter executor : callback){
            Interpreter exec  = executor.clone();
            exec.tokenManager.changeTokensParent(exec);
            exec.data.vars.putAll(variables);
            executor.compile();
        }
    }
}
