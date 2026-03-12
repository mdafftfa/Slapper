package org.mdafftfa;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.level.LevelSaveEvent;
import cn.nukkit.level.Level;

import org.mdafftfa.entity.SlapperEntity;
import org.mdafftfa.entity.SlapperHumanEntity;
import org.mdafftfa.entity.SlapperLoaderEntity;

public class SlapperListener implements Listener {

    Slapper plugin;
    String prefix;

    public SlapperListener(Slapper plugin) {
        this.plugin = plugin;
        this.prefix = plugin.prefix;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onLevelSafe(LevelSaveEvent event) {
        Level level = event.getLevel();
        for (Entity entity : level.getEntities()) {
            if (entity.namedTag.getBoolean(SlapperHumanEntity.TAG) && entity instanceof SlapperHumanEntity) {
                SlapperLoaderEntity slapper = new SlapperLoaderEntity(entity.getChunk(), Entity.getDefaultNBT(entity));
                slapper.writeSlapperDataFromEntity(entity);
                entity.close();
                slapper.spawnToAll();
            }

            if (entity.namedTag.getBoolean(SlapperEntity.TAG) && entity instanceof SlapperEntity) {
                SlapperLoaderEntity slapper = new SlapperLoaderEntity(entity.getChunk(), Entity.getDefaultNBT(entity));
                slapper.writeSlapperDataFromEntity(entity);
                entity.close();
                slapper.spawnToAll();
            }

        }
    }

}
