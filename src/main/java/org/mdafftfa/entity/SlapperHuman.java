package org.mdafftfa.entity;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.data.EntityFlag;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.format.IChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mdafftfa.SlapperInterface;

public class SlapperHuman extends EntityHuman implements SlapperInterface {

    public static String TAG = "Slapper Human";

    public SlapperHuman(IChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public @NotNull String getIdentifier() {
        return super.getIdentifier();
    }

    @Override
    public void setDataFromEntity(Entity entity) {
        SlapperHuman prevSlapper = (SlapperHuman) entity;
        this.setNameTag(prevSlapper.getNameTag());
        this.setSkin(prevSlapper.getSkin());
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        this.namedTag.putBoolean(TAG, true);
        this.namedTag.putString("SlapperClass", "SlapperHuman");

        this.setDataFlag(EntityFlag.CAN_SHOW_NAME, true);
        this.setDataFlag(EntityFlag.ALWAYS_SHOW_NAME, true);
        this.setNameTagAlwaysVisible(true);
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        this.close();
        return super.attack(source);
    }
}
