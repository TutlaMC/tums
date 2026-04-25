package net.tutla.tums.tusan.tums.objects;


import net.minecraft.world.entity.Entity;
import net.tutla.tums.tusan.Variable;

import java.util.Objects;

public class TumsEntity extends Variable {
    public Entity main;
    public TumsEntity(String name, Entity entity) {
        super(name, entity, null);
        this.main = entity;
        setProps();
    }

    public void setProps(){
        properties.put("name", Objects.requireNonNull(main.getDisplayName()).getString());
        properties.put("nametag", main.getCustomName());

        // Health & Inventory

        // Position
        properties.put("x", main.getX());
        properties.put("y", main.getY());
        properties.put("z", main.getY());
        properties.put("yaw", main.getYRot());
        properties.put("pitch", main.getXRot());

        properties.put("javaclass", main);
    }
}
