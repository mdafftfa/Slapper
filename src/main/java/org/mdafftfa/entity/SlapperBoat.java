package org.mdafftfa.entity;

import cn.nukkit.level.format.IChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class SlapperBoat extends SlapperEntity {

    public SlapperBoat(IChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected String getEntityType() {
        return "Boat";
    }

    @Override
    public float getWidth() {
        return 1.3f;
    }

    @Override
    public float getHeight() {
        return 0.5f;
    }

    @Override
    public @NotNull String getIdentifier() {
        return SlapperEntity.BOAT;
    }

}
