package me.ikexing.casualcraft.events;


import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;

public class CCEntityRemoveEvent extends EntityEvent {

    public CCEntityRemoveEvent(Entity entity) {
        super(entity);
    }

}
