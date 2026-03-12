package org.mdafftfa.entity;

import cn.nukkit.level.format.IChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class SlapperPrimedTNT extends SlapperEntity {

    public SlapperPrimedTNT(IChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected String getEntityType() {
        return "PrimedTNT";
    }

    @Override
    public float getWidth() {
        return 0.98f;
    }

    @Override
    public float getHeight() {
        return 0.7f;
    }

    @Override
    public @NotNull String getIdentifier() {
        return SlapperEntity.TNT_MINECART;
    }

}
