package org.mdafftfa.events;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.Nullable;

public class SlapperCreationEvent extends EntityEvent implements Cancellable {

    public static final int CAUSE_COMMAND = 0;
    public static final int CAUSE_API = 1;

    private final String type;
    private final Player executor;
    private final int cause;

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    public SlapperCreationEvent(Entity entity, String type, @Nullable Player executor, int cause) {
        this.entity = entity;
        this.type = type;
        this.executor = executor;
        this.cause = cause;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    public Player getExecutor() {
        return executor;
    }

    public boolean hasExecutor() {
        return executor != null;
    }

    public int getCause() {
        return cause;
    }

    public String getType() {
        return type;
    }
}