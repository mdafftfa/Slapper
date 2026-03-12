package org.mdafftfa.entity;

import cn.nukkit.level.format.IChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class SlapperLavaSlime extends SlapperEntity {

    public SlapperLavaSlime(IChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected String getEntityType() {
        return "LavaSlime";
    }

    @Override
    public float getWidth() {
        return 0.51f + 4 * 0.51f;
    }

    @Override
    public float getHeight() {
        return 0.51f + 4 * 0.51f;
    }

    @Override
    public @NotNull String getIdentifier() {
        return SlapperEntity.MAGMA_CUBE;
    }

}
