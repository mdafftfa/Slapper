package org.mdafftfa.entity;

import cn.nukkit.level.format.IChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class SlapperWitherSkeleton extends SlapperEntity {

    public SlapperWitherSkeleton(IChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected String getEntityType() {
        return "WitherSkeleton";
    }

    @Override
    public float getWidth() {
        return 0.7f;
    }

    @Override
    public float getHeight() {
        return 2.4f;
    }

    @Override
    public @NotNull String getIdentifier() {
        return SlapperEntity.WITHER_SKELETON;
    }

}
