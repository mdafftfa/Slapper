package org.mdafftfa.entities;

import cn.nukkit.entity.EntityCreature;
import cn.nukkit.level.format.IChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class SlapperEntity extends EntityCreature {

    public SlapperEntity(IChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public @NotNull String getIdentifier() {
        return EntityCreature.BEE;
    }

}
