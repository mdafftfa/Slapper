package org.mdafftfa.entity;

import cn.nukkit.entity.EntityID;
import cn.nukkit.level.format.IChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class SlapperEvoker extends SlapperEntity {

    public SlapperEvoker(IChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected String getEntityType() {
        return "Evoker";
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getHeight() {
        return 1.9f;
    }

    @Override
    public @NotNull String getIdentifier() {
        return EntityID.EVOCATION_ILLAGER;
    }

}
