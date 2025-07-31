package net.tutla.tums.tusan.tums.objects;

import net.minecraft.entity.player.PlayerEntity;
import net.tutla.tums.tusan.Variable;

import java.util.HashMap;

public class TumsPlayer extends Variable {
    public PlayerEntity main;
    public TumsPlayer(String name, PlayerEntity player) {
        super(name, player, null);
        main = player;
        setProps();
    }

    public void setProps(){
        if (main != null){
            properties.put("display_name", main.getDisplayName());
            properties.put("uuid", main.getUuid());

            // Health & Inventory
            properties.put("health", main.getHealth());

            // Position
            properties.put("x", main.getX());
            properties.put("y", main.getY());
            properties.put("z", main.getY());

            properties.put("yaw", main.getYaw());
            properties.put("pitch", main.getPitch());

            properties.put("javaclass", main);
        }

    }
}
