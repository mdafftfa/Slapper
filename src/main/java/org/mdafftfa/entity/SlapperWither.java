package org.mdafftfa.entity;

import cn.nukkit.level.format.IChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class SlapperWither extends SlapperEntity {

    public SlapperWither(IChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected String getEntityType() {
        return "Wither";
    }

    @Override
    public float getWidth() {
        return 1.0f;
    }

    @Override
    public float getHeight() {
        return 3.0f;
    }

    @Override
    public @NotNull String getIdentifier() {
        return SlapperEntity.WITHER;
    }

}
