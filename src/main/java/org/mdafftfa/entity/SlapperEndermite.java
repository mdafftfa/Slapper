package org.mdafftfa.entity;

import cn.nukkit.level.format.IChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class SlapperEndermite extends SlapperEntity {

    public SlapperEndermite(IChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected String getEntityType() {
        return "Endermite";
    }

    @Override
    public float getWidth() {
        return 0.4f;
    }

    @Override
    public float getHeight() {
        return 0.3f;
    }

    @Override
    public @NotNull String getIdentifier() {
        return SlapperEntity.ENDERMITE;
    }

}
