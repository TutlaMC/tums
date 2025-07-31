package net.tutla.tums.tusan.tums.objects;

import net.minecraft.entity.player.PlayerEntity;
import net.tutla.tums.tusan.Variable;

import java.util.HashMap;

public class TumsPlayer extends Variable {
    public HashMap<String, Object> playerProps = new HashMap<>();
    public PlayerEntity main;
    public TumsPlayer(String name, PlayerEntity player) {
        super(name, player, null);
        this.main = player;
        setProps();
    }

    public void setProps(){
        playerProps.put("display_name", main.getDisplayName());
        playerProps.put("uuid", main.getUuid());

        // Health & Inventory
        playerProps.put("health", main.getHealth());

        // Position
        playerProps.put("x", main.getX());
        playerProps.put("y", main.getY());
        playerProps.put("z", main.getY());

        playerProps.put("yaw", main.getYaw());
        playerProps.put("pitch", main.getPitch());
    }
}
