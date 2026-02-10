package org.mdafftfa.entity;

import cn.nukkit.level.format.IChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class SlapperPolarBear extends SlapperEntity {

    public SlapperPolarBear(IChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected String getEntityType() {
        return "PolarBear";
    }

    @Override
    public float getWidth() {
        return 1.3f;
    }

    @Override
    public float getHeight() {
        return 1.4f;
    }

    @Override
    public @NotNull String getIdentifier() {
        return SlapperEntity.POLAR_BEAR;
    }

}
