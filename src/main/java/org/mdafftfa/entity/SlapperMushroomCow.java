package org.mdafftfa.entity;

import cn.nukkit.level.format.IChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class SlapperMushroomCow extends SlapperEntity {

    public SlapperMushroomCow(IChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected String getEntityType() {
        return "MushroomCow";
    }

    @Override
    public float getWidth() {
        return 0.9f;
    }

    @Override
    public float getHeight() {
        return 1.3f;
    }

    @Override
    public @NotNull String getIdentifier() {
        return SlapperEntity.MOOSHROOM;
    }

}
