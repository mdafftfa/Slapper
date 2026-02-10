package org.mdafftfa.entity;

import cn.nukkit.level.format.IChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class SlapperEnderman extends SlapperEntity {

    public SlapperEnderman(IChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected String getEntityType() {
        return "Enderman";
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getHeight() {
        return 2.9f;
    }

    @Override
    public @NotNull String getIdentifier() {
        return SlapperEntity.ENDERMAN;
    }

}
