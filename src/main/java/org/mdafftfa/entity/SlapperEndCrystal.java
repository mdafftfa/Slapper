package org.mdafftfa.entity;

import cn.nukkit.level.format.IChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class SlapperEndCrystal extends SlapperEntity {

    public SlapperEndCrystal(IChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected String getEntityType() {
        return "EndCrystal";
    }

    @Override
    public float getWidth() {
        return 0.98f;
    }

    @Override
    public float getHeight() {
        return 0.98f;
    }

    @Override
    public @NotNull String getIdentifier() {
        return SlapperEntity.ENDER_CRYSTAL;
    }

}
