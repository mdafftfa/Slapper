package org.mdafftfa.entity;

import cn.nukkit.level.format.IChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class SlapperSlime extends SlapperEntity {

    public SlapperSlime(IChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected String getEntityType() {
        return "Slime";
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
        return SlapperEntity.SLIME;
    }

}
