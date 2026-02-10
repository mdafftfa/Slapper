package org.mdafftfa.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityFlag;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.format.IChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.mdafftfa.SlapperInterface;

public class SlapperEntity extends Entity implements SlapperInterface {

    public static String TAG = "Slapper Entity";

    public SlapperEntity(IChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    protected String getEntityType() {
        return "";
    }

    @Override
    public @NotNull String getIdentifier() {
        return Entity.AGENT;
    }

    @Override
    public void setDataFromEntity(Entity entity) {
        SlapperEntity prevSlapper = (SlapperEntity) entity;
        this.setNameTag(prevSlapper.getNameTag());
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.namedTag.putBoolean(TAG, true);
        this.namedTag.putString("SlapperType", getEntityType());

        this.setDataFlag(EntityFlag.CAN_SHOW_NAME, true);
        this.setDataFlag(EntityFlag.ALWAYS_SHOW_NAME, true);
        this.setNameTagAlwaysVisible(true);
    }

    @Override
    public boolean onUpdate(int currentTick) {
        // Untuk debugging
//        if (currentTick % 100 == 0) { // Log setiap 100 tick
//            Server.getInstance().getLogger().info(
//                    "Slapper Entity - NameTag: '" + this.getNameTag() +
//                            "', Visible: " + this.isNameTagAlwaysVisible() +
//                            ", HasFlag: " + this.getDataFlag(EntityFlag.ALWAYS_SHOW_NAME)
//            );
//
//            Server.getInstance().getLogger().info("Bool: "+ this.namedTag.getBoolean(TAG));
//            Server.getInstance().getLogger().info("SlapperClass: "+ this.namedTag.getBoolean(TAG));
//        }


//        Server.getInstance().getLogger().info("oke");
        return super.onUpdate(currentTick);
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        this.close();
        return super.attack(source);
    }
}