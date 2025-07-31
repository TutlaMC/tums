package net.tutla.tums;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.ActionResult;
import net.tutla.tums.tusan.interpreter.Interpreter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class Tums implements ModInitializer {
    public static InterpreterRegistry register = new InterpreterRegistry();

    public InterpreterRegistry getRegister(){
        return register;
    }

    @Override
    public void onInitialize() {
        Path gameDir = FabricLoader.getInstance().getGameDir();
        Path scriptsDir = gameDir.resolve("tums/scripts");
        Path tumsDir = gameDir.resolve("tums");
        try {
            Files.createDirectories(tumsDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try (Stream<Path> stream = Files.list(scriptsDir)) {
            stream
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        Interpreter interpreter = new Interpreter();
                        interpreter.setup(null,null,null, path);
                        try{
                            interpreter.compile();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        register.add(interpreter.data);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (!world.isClient) {
                System.out.println("Player used block: " + hitResult.getBlockPos());
            }
            return ActionResult.PASS;
        });
    }
}
