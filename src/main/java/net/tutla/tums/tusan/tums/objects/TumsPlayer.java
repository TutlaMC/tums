package net.tutla.tums.tusan.tums.objects;


import net.minecraft.world.entity.player.Player;
import net.tutla.tums.tusan.Variable;

import java.util.HashMap;

public class TumsPlayer extends Variable {
    public Player main;
    public TumsPlayer(String name, Player player) {
        super(name, player, null);
        main = player;
        setProps();
    }

    public void setProps(){
        if (main != null){
            properties.put("display_name", main.getDisplayName());
            properties.put("uuid", main.getUUID());

            // Health & Inventory
            properties.put("health", main.getHealth());

            // Position
            properties.put("x", main.getX());
            properties.put("y", main.getY());
            properties.put("z", main.getY());

            properties.put("yaw", main.getYRot());
            properties.put("pitch", main.getXRot());

            properties.put("javaclass", main);
        }

    }
}
