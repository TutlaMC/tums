package net.tutla.tums;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.tutla.tums.tusan.interpreter.Interpreter;
import net.tutla.tums.tusan.lang.Tusan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Tums implements ModInitializer {
    private final Tusan tusan = new Tusan(); // primary language

    public static InterpreterRegistry register = new InterpreterRegistry();
    public static InterpreterRegistry getRegister(){
        return register;
    }

    Path gameDir = FabricLoader.getInstance().getGameDir();
    Path scriptsDir = gameDir.resolve("tums/scripts");
    Path tumsDir = gameDir.resolve("tums");

    @Override
    public void onInitialize() {
        resolveTumsDir();
        runAllScripts(scriptsDir);
    }

    public void resolveTumsDir(){
        try {
            Files.createDirectories(tumsDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void runAllScripts(Path scriptsDir){
        try (Stream<Path> stream = Files.list(scriptsDir)) {
                stream.filter(Files::isRegularFile)
                    .forEach(this::runScript);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runScript(Path script){
        Interpreter interpreter = new Interpreter(tusan);
        interpreter.setup(null,null,null, script);

        try{
            interpreter.compile(); // where every script in the dir gets executed
        } catch (Exception e){
            e.printStackTrace();
        }

        register.add(interpreter.data); // why are we doing this?
    }

}
