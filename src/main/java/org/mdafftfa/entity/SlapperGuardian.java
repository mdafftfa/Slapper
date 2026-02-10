package org.mdafftfa.entity;

import cn.nukkit.level.format.IChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class SlapperGuardian extends SlapperEntity {

    public SlapperGuardian(IChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected String getEntityType() {
        return "Guardian";
    }

    @Override
    public float getWidth() {
        return 0.85f;
    }

    @Override
    public float getHeight() {
        return 0.85f;
    }

    @Override
    public @NotNull String getIdentifier() {
        return SlapperEntity.GUARDIAN;
    }

}
