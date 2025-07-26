package net.tutla.tums;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;
import net.minecraft.text.Text;
import net.tutla.tums.tusan.interpreter.Interpreter;

import java.util.HashMap;
import java.util.List;

public class Tums implements ModInitializer {
    public HashMap<Event, List<Interpreter>> EventMappings;


    @Override
    public void onInitialize() {
        Interpreter interpreter = new Interpreter();
        interpreter.setup(null,null,"print 1+7*(7/2)",null);
        interpreter.compile();

        
    }
}
