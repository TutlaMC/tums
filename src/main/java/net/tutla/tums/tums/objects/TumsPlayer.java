package net.tutla.tums.tums.objects;


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
            updateProperty("display_name", main.getDisplayName());
            updateProperty("uuid", main.getUUID());

            // Health & Inventory
            updateProperty("health", main.getHealth());

            // Position
            updateProperty("x", main.getX());
            updateProperty("y", main.getY());
            updateProperty("z", main.getY());

            updateProperty("yaw", main.getYRot());
            updateProperty("pitch", main.getXRot());

            updateProperty("javaclass", main);
        }

    }
}
