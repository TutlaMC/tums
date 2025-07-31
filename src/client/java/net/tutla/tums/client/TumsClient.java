package net.tutla.tums.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.tutla.tums.Tums;
import net.tutla.tums.tusan.Utils;
import net.tutla.tums.tusan.interpreter.Interpreter;
import net.tutla.tums.tusan.tums.objects.TumsEntity;
import net.tutla.tums.tusan.tums.objects.TumsPlayer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

import java.util.HashMap;
import java.util.List;

public class TumsClient implements ClientModInitializer {
    private boolean mouseCallbackSet = false;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> { // minecraft why did u make me take 2 hours to figure ts out
            if (!mouseCallbackSet && client.getWindow() != null) {
                long handle = client.getWindow().getHandle();

                GLFWMouseButtonCallbackI oldC = GLFW.glfwSetMouseButtonCallback(handle, null);

                GLFW.glfwSetMouseButtonCallback(handle, (window, button, action, mods) -> {
                    if (oldC != null) {
                        oldC.invoke(window, button, action, mods);
                    }
                    if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                        if (action == GLFW.GLFW_PRESS){
                            List<Interpreter> callback = Tums.register.registry.events.get("LEFT_CLICK");
                            for (Interpreter executor : callback){
                                executor.compile();
                            }
                        } else if (action == GLFW.GLFW_RELEASE){
                            List<Interpreter> callback = Tums.register.registry.events.get("LEFT_RELEASE");
                            for (Interpreter executor : callback){
                                executor.compile();
                            }
                        }

                    }
                });

                mouseCallbackSet = true;
            }
        });

        AttackEntityCallback.EVENT.register((player, world, hand, target, hitResult) -> {
            if (world.isClient) {
                HashMap<String, Object> variables = new HashMap<>();
                if (target instanceof PlayerEntity otherPlayer) {
                    variables.put("event_player", new TumsPlayer("event_player", otherPlayer));
                    variables.put("event_entity", new TumsEntity("event_entity", target));
                } else {
                    variables.put("event_player", new TumsPlayer("event_player", null));
                    variables.put("event_entity", new TumsEntity("event_entity", target));
                }
                Utils.executeEvent("attack", variables);
            }
            return ActionResult.PASS;
        });
    }
}