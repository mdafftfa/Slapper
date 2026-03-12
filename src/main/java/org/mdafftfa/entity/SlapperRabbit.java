package org.mdafftfa.entity;

import cn.nukkit.level.format.IChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class SlapperRabbit extends SlapperEntity {

    public SlapperRabbit(IChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected String getEntityType() {
        return "Rabbit";
    }

    @Override
    public float getWidth() {
        return 0.402f;
    }

    @Override
    public float getHeight() {
        return 0.402f;
    }

    @Override
    public @NotNull String getIdentifier() {
        return SlapperEntity.RABBIT;
    }

}
